package com.lims.lims_study.application.user.service;

import com.lims.lims_study.application.user.dto.*;
import com.lims.lims_study.global.common.PageResponse;

public interface IUserApplicationService {
    UserResponseDto createUser(UserCreateDto dto);
    UserResponseDto updateUser(Long userId, UserUpdateDto dto);
    void deleteUser(Long userId);
    UserResponseDto getUser(Long userId);
    UserResponseDto getCurrentUser();
    PageResponse<UserResponseDto> searchUsers(UserSearchDto dto);
    boolean existsByUsername(String username);
}
