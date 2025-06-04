package com.lims.lims_study.application.user.dto;

public class UserCreateDto {
    private String username;
    private String password;
    private String authorities;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthorities() {
        return authorities;
    }
}