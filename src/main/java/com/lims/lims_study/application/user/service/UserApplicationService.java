package com.lims.lims_study.application.user.service;

import com.lims.lims_study.global.util.AuthenticationProvider;
import com.lims.lims_study.application.user.dto.*;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.service.IUserService;
import com.lims.lims_study.global.common.PageResponse;
import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserApplicationService implements IUserApplicationService {
    private final IUserService userService;
    private final AuthenticationProvider authProvider;
    private final UserDtoMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponseDto createUser(UserCreateDto dto) {
        log.info("Creating user with username: {}", dto.getUsername());
        
        // 중복 사용자명 검증
        if (userService.existsByUsername(dto.getUsername())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }
        
        User user = new User(null, dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getAuthorities());
        User savedUser = userService.createUser(user);
        
        log.info("User created successfully with ID: {}", savedUser.getId());
        return UserResponseDto.from(savedUser);
    }

    @Override
    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto dto) {
        log.info("Updating user with ID: {}", userId);
        
        User user = userService.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        String encodedPassword = dto.getPassword() != null ? passwordEncoder.encode(dto.getPassword()) : user.getPassword();
        String authorities = dto.getAuthorities() != null ? dto.getAuthorities() : user.getAuthorities();
        
        user.updateUserInfo(encodedPassword, authorities);
        User updatedUser = userService.updateUser(user);
        
        log.info("User updated successfully with ID: {}", userId);
        return UserResponseDto.from(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        
        if (!userService.existsById(userId)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        userService.deleteUser(userId);
        log.info("User deleted successfully with ID: {}", userId);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        log.debug("Getting user with ID: {}", userId);
        
        User user = userService.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        return UserResponseDto.from(user);
    }

    @Override
    public UserResponseDto getCurrentUser() {
        log.debug("Getting current user");
        
        String username = authProvider.getCurrentUsername();
        if (username == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
        }
        
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        return UserResponseDto.from(user);
    }

    @Override
    public PageResponse<UserResponseDto> searchUsers(UserSearchDto dto) {
        log.debug("Searching users with criteria: {}", dto);
        
        List<User> users = userService.searchUsers(dto.getUsername(), dto.getAuthorities(), dto.getOffset(), dto.getSize());
        long totalCount = userService.countBySearchCriteria(dto.getUsername(), dto.getAuthorities());
        
        List<UserResponseDto> userDtos = users.stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
        
        return PageResponse.of(userDtos, dto, totalCount);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userService.existsByUsername(username);
    }
}
