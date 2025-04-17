package com.lims.lims_study.application.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private String password;
    private String authorities;
}