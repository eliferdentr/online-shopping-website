package com.eliferden.onlineshoppingwebsite.business;


import com.eliferden.onlineshoppingwebsite.dto.UserCreateDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserDetailDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserListDTO;
import com.eliferden.onlineshoppingwebsite.dto.UserUpdateDTO;
import com.eliferden.onlineshoppingwebsite.entities.User;

import java.util.List;

public interface UserService {

    User createUser(UserCreateDTO userCreateDTO);
    User updateUser(UserUpdateDTO userUpdateDTO, Long userId);
    void deleteUser (Long userId);
    User findUserById (Long userId);
    List<UserListDTO> findAllUsers();
    UserDetailDTO findUserDTOById(Long userId);

}
