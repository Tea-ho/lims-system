package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserMapper userMapper;

    public void validateCreate(User user) {
        Optional<User> existing = userMapper.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    public User validateUserExists(Long userId) {
        return userMapper.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    public User validateUserExists(String username) {
        return userMapper.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
}