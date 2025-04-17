package com.lims.lims_study.application.user.service;

import com.lims.lims_study.application.user.dto.*;

import java.util.List;

public interface IUserApplicationService {
    UserResponseDto createUser(UserCreateDto dto);
    UserResponseDto updateUser(Long userId, UserUpdateDto dto);
    void deleteUser(Long userId);
    UserResponseDto getUser(Long userId);
    UserResponseDto getCurrentUser();
    List<UserResponseDto> searchUsers(UserSearchDto dto);
}