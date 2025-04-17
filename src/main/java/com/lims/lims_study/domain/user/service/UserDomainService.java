package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainService implements IUserService {
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public void createUser(User user) {
        userValidator.validateCreate(user);
        userMapper.insert(user);
    }

    @Override
    @Transactional
    public void updateUser(Long userId, User user) {
        User existing = userValidator.validateUserExists(userId);
        user.setId(userId);
        user.setUsername(existing.getUsername());
        user.setCreatedAt(existing.getCreatedAt());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userValidator.validateUserExists(userId);
        userMapper.delete(userId);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public List<User> searchUsers(String username) {
        return userMapper.searchByUsername(username);
    }
}