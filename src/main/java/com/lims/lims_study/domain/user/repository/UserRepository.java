package com.lims.lims_study.domain.user.repository;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> searchByUsername(String username);
}