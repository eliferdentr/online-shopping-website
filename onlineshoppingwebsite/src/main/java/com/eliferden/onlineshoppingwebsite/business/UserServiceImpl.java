package com.eliferden.onlineshoppingwebsite.business;

import com.eliferden.onlineshoppingwebsite.dto.UserCreateDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserDetailDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserListDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserUpdateDTO;
import com.eliferden.onlineshoppingwebsite.entities.User;
import com.eliferden.onlineshoppingwebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
        if (checkByUsername(userCreateDTO.getUserName())) throw new IllegalArgumentException("Username is already taken!");
        else if (checkByEmail(userCreateDTO.getEmail())) throw new IllegalArgumentException("Email is already in use!");
        else {
            User user = new User();
            user.setUserName(userCreateDTO.getUserName());
            user.setEmail(userCreateDTO.getEmail());
            user.setPassword(encodePassword(userCreateDTO.getPassword()));
            return userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public User updateUser(UserUpdateDTO userUpdateDTO, Long userId) {
        User existingUser = findUserById(userId);
        if (userUpdateDTO.getEmail() != null && !userUpdateDTO.getEmail().equals(existingUser.getEmail())) {
            if (checkByEmail(userUpdateDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already taken!");
            }
            existingUser.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getUserName() != null && !userUpdateDTO.getUserName().equals(existingUser.getUserName())) {
            if (checkByUsername(userUpdateDTO.getUserName())) {
                throw new IllegalArgumentException("Username is already taken!");
            }
            existingUser.setUserName(userUpdateDTO.getUserName());
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
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);

    }

    @Override
    public UserDetailDTO findUserDTOById(Long userId) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFoundException("User not found."));
        UserDetailDTO userDetailDTO = new UserDetailDTO();
        userDetailDTO.setUsername(user.getUserName());
        userDetailDTO.setEmail(user.getEmail());
        return userDetailDTO;
    }

    @Override
    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        return user;
    }

    @Override
    public List<UserListDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? Collections.emptyList() :
                users.stream()
                        .map(user -> new UserListDTO(user.getId(), user.getUserName(), user.getEmail()))
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
}
