package com.lims.lims_study.domain.user.repository;

import com.lims.lims_study.domain.user.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface UserMapper {
    void insert(User user);
    void update(User user);
    void delete(Long id);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> searchByUsername(String username);
}