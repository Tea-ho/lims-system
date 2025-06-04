package com.lims.lims_study.application.auth.service;

import com.lims.lims_study.application.auth.dto.LoginRequestDto;
import com.lims.lims_study.application.auth.dto.LoginResponseDto;
import com.lims.lims_study.application.user.dto.UserCreateDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.application.user.service.UserApplicationService;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.service.UserDomainService;
import com.lims.lims_study.global.config.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final UserApplicationService userApplicationService;
    private final UserDomainService userDomainService;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userDomainService.findByUsername(dto.getUsername())
            .orElseThrow(() -> new IllegalArgumentException("UserRepository not found: " + dto.getUsername()));
        String token = jwtProvider.generateToken(user.getUsername());
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        return response;
    }

    public UserResponseDto register(UserCreateDto dto) {
        return userApplicationService.createUser(dto);
    }
}