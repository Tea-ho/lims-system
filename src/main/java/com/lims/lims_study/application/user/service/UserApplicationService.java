package com.lims.lims_study.application.user.service;

import com.lims.lims_study.global.util.AuthenticationProvider;
import com.lims.lims_study.application.user.dto.*;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserApplicationService implements IUserApplicationService {
    private final IUserService userService;
    private final AuthenticationProvider authProvider;
    private final UserDtoMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserCreateDto dto) {
        User user = new User(null, dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getAuthorities());
        userService.createUser(user);
        return dtoMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserUpdateDto dto) {
        User user = new User(userId, null, passwordEncoder.encode(dto.getPassword()), dto.getAuthorities());
        userService.updateUser(userId, user);
        User updated = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return dtoMapper.toResponseDto(updated);
    }

    @Override
    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return dtoMapper.toResponseDto(user);
    }

    @Override
    public UserResponseDto getCurrentUser() {
        String username = authProvider.getCurrentUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return dtoMapper.toResponseDto(user);
    }

    @Override
    public List<UserResponseDto> searchUsers(UserSearchDto dto) {
        List<User> users = userService.searchUsers(dto.getUsername());
        return users.stream()
                .map(dtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}