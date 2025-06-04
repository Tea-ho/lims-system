package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.application.user.dto.UserUpdateDto;
import com.lims.lims_study.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    void createUser(User user);
    void updateUser(Long userId, UserUpdateDto dto);
    void deleteUser(Long userId);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    List<User> searchUsers(String username);
}