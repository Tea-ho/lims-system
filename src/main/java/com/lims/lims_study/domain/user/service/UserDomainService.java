package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserRepository;
import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDomainService implements IUserService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public User createUser(User user) {
        log.debug("Creating user in domain: {}", user.getUsername());
        
        userValidator.validateForCreation(user);
        userRepository.insert(user);
        
        log.debug("User created in domain with ID: {}", user.getId());
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        log.debug("Updating user in domain: {}", user.getId());
        
        userValidator.validateForUpdate(user);
        userRepository.update(user);
        
        log.debug("User updated in domain: {}", user.getId());
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.debug("Deleting user in domain: {}", userId);
        
        userValidator.validateUserExists(userId);
        userRepository.delete(userId);
        
        log.debug("User deleted in domain: {}", userId);
    }

    @Override
    public Optional<User> findById(Long userId) {
        log.debug("Finding user by ID: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        log.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> searchUsers(String username, String authorities, int offset, int size) {
        log.debug("Searching users with criteria - username: {}, authorities: {}, offset: {}, size: {}", 
                  username, authorities, offset, size);
        return userRepository.searchUsers(username, authorities, offset, size);
    }

    @Override
    public long countBySearchCriteria(String username, String authorities) {
        log.debug("Counting users with criteria - username: {}, authorities: {}", username, authorities);
        return userRepository.countBySearchCriteria(username, authorities);
    }

    @Override
    public boolean existsById(Long userId) {
        log.debug("Checking if user exists by ID: {}", userId);
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Checking if user exists by username: {}", username);
        return userRepository.existsByUsername(username);
    }
}
