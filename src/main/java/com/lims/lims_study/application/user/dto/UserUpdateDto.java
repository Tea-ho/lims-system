package com.lims.lims_study.application.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]+$", 
             message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;
    
    @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN|ROLE_MANAGER)$", message = "유효하지 않은 권한입니다.")
    private String authorities;

    public UserUpdateDto(String password, String authorities) {
        this.password = password;
        this.authorities = authorities;
    }
}
