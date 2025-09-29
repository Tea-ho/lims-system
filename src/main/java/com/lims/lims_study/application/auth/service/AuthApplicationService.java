package com.lims.lims_study.application.auth.service;

import com.lims.lims_study.application.auth.dto.LoginRequestDto;
import com.lims.lims_study.application.auth.dto.LoginResponseDto;
import com.lims.lims_study.application.user.dto.UserCreateDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.application.user.service.UserApplicationService;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.service.UserDomainService;
import com.lims.lims_study.global.config.JwtProvider;
import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final UserApplicationService userApplicationService;
    private final UserDomainService userDomainService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto dto) {
        log.info("로그인 시도: {}", dto.getUsername());
        
        User user = userDomainService.findByUsername(dto.getUsername())
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        
        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            dto.getUsername(),
            null,
            Collections.singletonList(new SimpleGrantedAuthority(user.getAuthorities()))
        );
        
        // JWT 토큰 생성
        String token = jwtProvider.generateToken(authentication);
        
        log.info("로그인 성공: {}", dto.getUsername());
        
        return new LoginResponseDto(token, user.getUsername(), user.getAuthorities());
    }

    public UserResponseDto register(UserCreateDto dto) {
        log.info("회원가입 시도: {}", dto.getUsername());
        
        UserResponseDto response = userApplicationService.createUser(dto);
        
        log.info("회원가입 성공: {}", dto.getUsername());
        return response;
    }
}
