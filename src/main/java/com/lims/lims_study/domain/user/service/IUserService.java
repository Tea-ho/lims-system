package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.domain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(Long userId);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    List<User> searchUsers(String username, String authorities, int offset, int size);
    long countBySearchCriteria(String username, String authorities);
    boolean existsById(Long userId);
    boolean existsByUsername(String username);
}
