package com.eliferden.onlineshoppingwebsite.business;

import com.eliferden.onlineshoppingwebsite.dto.UserCreateDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserDetailDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserListDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserUpdateDTO;
import com.eliferden.onlineshoppingwebsite.entities.User;
import com.eliferden.onlineshoppingwebsite.exceptions.ErrorMessagesForUser;
import com.eliferden.onlineshoppingwebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createUser(UserCreateDTO userCreateDTO) {
        validateCreateUserDTO(userCreateDTO); // Validate DTO before processing
        User user = new User();
        user.setUserName(userCreateDTO.getUserName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(encodePassword(userCreateDTO.getPassword()));
        return userRepository.save(user);

    }

    @Override
    @Transactional
    public User updateUser(UserUpdateDTO userUpdateDTO, Long userId) {
        User existingUser = findUserById(userId);
        validateUpdateUserDTO(userUpdateDTO, existingUser); // Validate DTO before updating

        if (userUpdateDTO.getUserName() != null && !userUpdateDTO.getUserName().equals(existingUser.getUserName())) {
            existingUser.setUserName(userUpdateDTO.getUserName());
        }
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getPassword() != null) {
            existingUser.setPassword(encodePassword(userUpdateDTO.getPassword()));
        }
        return userRepository.save(existingUser);


    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format(ErrorMessagesForUser.USER_NOT_FOUND_WITH_ID, userId));
        }
        userRepository.deleteById(userId);

    }

    @Override
    public UserDetailDTO findUserDTOById(Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFoundException(ErrorMessagesForUser.USER_NOT_FOUND));
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUsername(user.getUserName());
        userDetailDTO.setEmail(user.getEmail());
        return userDetailDTO;
    }

    @Override
    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessagesForUser.USER_NOT_FOUND));
        return user;
    }

    @Override
    public List<UserListDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserListDTO::new) // Using the new constructor
                .collect(Collectors.toList());
    }

    public boolean checkByUsername(String userName){
        return userRepository.existsByUsername(userName);
    }

    public boolean checkByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public String encodePassword (String password){
        return passwordEncoder.encode(password);
    }

    private void validateCreateUserDTO(UserCreateDTO dto) {
        if (dto.getUserName() == null || dto.getUserName().trim().isEmpty())
            throw new IllegalArgumentException(ErrorMessagesForUser.USER_NAME_EMPTY);

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty())
            throw new IllegalArgumentException(ErrorMessagesForUser.USER_EMAIL_EMPTY);
        else if (!dto.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"))
            throw new IllegalArgumentException(ErrorMessagesForUser.EMAIL_FORMAT_INVALID);

        if (dto.getPassword() == null || dto.getPassword().length() < 6)
            throw new IllegalArgumentException(ErrorMessagesForUser.PASSWORD_INVALID);

        if (checkByUsername(dto.getUserName()))
            throw new IllegalArgumentException(ErrorMessagesForUser.USERNAME_TAKEN);
        if (checkByEmail(dto.getEmail()))
            throw new IllegalArgumentException(ErrorMessagesForUser.EMAIL_IN_USE);
    }

    private void validateUpdateUserDTO(UserUpdateDTO dto, User existingUser) {
        if (dto.getUserName() != null && !dto.getUserName().equals(existingUser.getUserName())) {
            if (dto.getUserName().trim().isEmpty())
                throw new IllegalArgumentException(ErrorMessagesForUser.USER_NAME_EMPTY);
            if (checkByUsername(dto.getUserName()))
                throw new IllegalArgumentException(ErrorMessagesForUser.USERNAME_TAKEN);
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(existingUser.getEmail())) {
            if (dto.getEmail().trim().isEmpty())
                throw new IllegalArgumentException(ErrorMessagesForUser.USER_EMAIL_EMPTY);
            else if (!dto.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"))
                throw new IllegalArgumentException(ErrorMessagesForUser.EMAIL_FORMAT_INVALID);
            if (checkByEmail(dto.getEmail()))
                throw new IllegalArgumentException(ErrorMessagesForUser.EMAIL_IN_USE);
        }

        if (dto.getPassword() != null && dto.getPassword().length() < 6)
            throw new IllegalArgumentException(ErrorMessagesForUser.PASSWORD_INVALID);
    }


}
