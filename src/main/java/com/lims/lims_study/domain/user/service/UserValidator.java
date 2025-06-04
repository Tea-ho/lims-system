package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserRepository;
import com.lims.lims_study.global.util.AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRepositoryMapper;
    private final AuthenticationProvider authProvider;

    public void checkDuplicateUser(User user){
        Optional<User> existing = userRepositoryMapper.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
    }

    public void checkPasswordBlank(User user){
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    public User validateUserExists(Long userId) {
        return userRepositoryMapper.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserRepository not found: " + userId));
    }

    public User validateUserExists(String username) {
        return userRepositoryMapper.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("UserRepository not found: " + username));
    }

    public Long getCurrentUserId() {
        String username = authProvider.getCurrentUsername();
        return userRepositoryMapper.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username))
                .getId();
    }
}