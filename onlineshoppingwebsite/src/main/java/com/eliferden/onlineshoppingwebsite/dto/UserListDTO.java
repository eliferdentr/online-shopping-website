package com.eliferden.onlineshoppingwebsite.dto;

import com.eliferden.onlineshoppingwebsite.entities.User;

public class UserListDTO {
    private Long id;
    private String username;
    private String email;

    public UserListDTO(User user) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.email = user.getEmail();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
