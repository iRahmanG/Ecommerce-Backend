package com.example.ecommerce.dto;

import com.example.ecommerce.entity.Role;

public class RegisterRequest {

    private String username;
    private String password;
    private Role role;
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Role getRole(){
        return role;
    }
}
