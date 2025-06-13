package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.application.user.dto.UserUpdateDto;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainService implements IUserService {
    private final UserRepository userRepositoryMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(User user) {
        userValidator.checkDuplicateUser(user);
        userValidator.checkPasswordBlank(user);
        userRepositoryMapper.insert(user);
    }

    @Override
    public void updateUser(Long userId, UserUpdateDto dto) {
        User existing = userValidator.validateUserExists(userId);
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        existing.updateUserInfo(encodedPassword, dto.getAuthorities());
        userRepositoryMapper.update(existing);
    }

    @Override
    public void deleteUser(Long userId) {
        userValidator.validateUserExists(userId);
        userRepositoryMapper.delete(userId);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepositoryMapper.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepositoryMapper.findByUsername(username);
    }

    @Override
    public List<User> searchUsers(String username) {
        return userRepositoryMapper.searchByUsername(username);
    }


}