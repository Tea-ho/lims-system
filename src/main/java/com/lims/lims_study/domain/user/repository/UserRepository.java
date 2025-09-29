package com.lims.lims_study.domain.user.repository;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.global.common.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository extends BaseRepository<User, Long> {
    
    Optional<User> findByUsername(@Param("username") String username);
    
    List<User> searchUsers(@Param("username") String username, 
                          @Param("authorities") String authorities,
                          @Param("offset") int offset, 
                          @Param("size") int size);
    
    long countBySearchCriteria(@Param("username") String username, 
                              @Param("authorities") String authorities);
    
    boolean existsByUsername(@Param("username") String username);
    
    boolean existsById(@Param("id") Long id);

    long countAllUsers();
}
