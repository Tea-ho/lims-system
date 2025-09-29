package com.lims.lims_study.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String username;
    private String authorities;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime loginTime;

    public LoginResponseDto(String token, String username, String authorities) {
        this.token = token;
        this.username = username;
        this.authorities = authorities;
        this.loginTime = LocalDateTime.now();
    }
}
