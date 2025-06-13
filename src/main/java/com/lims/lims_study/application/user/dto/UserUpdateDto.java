package com.lims.lims_study.application.user.dto;

import lombok.Getter;
import lombok.Setter;

public class UserUpdateDto {
    private String password;
    private String authorities;

    public String getPassword() {
        return password;
    }
    public String getAuthorities() {
        return authorities;
    }
}