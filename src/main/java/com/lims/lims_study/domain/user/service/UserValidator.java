package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserRepository;
import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import com.lims.lims_study.global.util.AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;
    private final AuthenticationProvider authProvider;

    public void validateForCreation(User user) {
        log.debug("Validating user for creation: {}", user.getUsername());
        
        validateNotNull(user, "사용자 정보는 필수입니다.");
        validateUsernameNotDuplicate(user.getUsername());
        validatePasswordNotEmpty(user.getPassword());
        validateAuthorities(user.getAuthorities());
    }

    public void validateForUpdate(User user) {
        log.debug("Validating user for update: {}", user.getId());
        
        validateNotNull(user, "사용자 정보는 필수입니다.");
        validateUserExists(user.getId());
        
        if (user.getPassword() != null) {
            validatePasswordNotEmpty(user.getPassword());
        }
        
        if (user.getAuthorities() != null) {
            validateAuthorities(user.getAuthorities());
        }
    }

    public User validateUserExists(Long userId) {
        log.debug("Validating user exists: {}", userId);
        
        if (userId == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자 ID는 필수입니다.");
        }
        
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public User validateUserExists(String username) {
        log.debug("Validating user exists by username: {}", username);
        
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자명은 필수입니다.");
        }
        
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public Long getCurrentUserId() {
        log.debug("Getting current user ID");
        
        String username = authProvider.getCurrentUsername();
        if (username == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
        }
        
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND))
                .getId();
    }

    private void validateNotNull(User user, String message) {
        if (user == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, message);
        }
    }

    private void validateUsernameNotDuplicate(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자명은 필수입니다.");
        }
        
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }
    }

    private void validatePasswordNotEmpty(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호는 필수입니다.");
        }
    }

    private void validateAuthorities(String authorities) {
        if (authorities == null || authorities.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "권한은 필수입니다.");
        }
        
        // 유효한 권한인지 검증
        if (!authorities.matches("^(ROLE_USER|ROLE_ADMIN|ROLE_MANAGER)$")) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "유효하지 않은 권한입니다.");
        }
    }
}
