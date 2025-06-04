package com.lims.lims_study.presentation.auth;

import com.lims.lims_study.application.auth.service.AuthApplicationService;
import com.lims.lims_study.application.auth.dto.LoginRequestDto;
import com.lims.lims_study.application.auth.dto.LoginResponseDto;
import com.lims.lims_study.application.user.dto.UserCreateDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response = authApplicationService.login(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserCreateDto dto) {
        UserResponseDto response = authApplicationService.register(dto);
        return ResponseEntity.ok(response);
    }
}