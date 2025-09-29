package com.lims.lims_study.application.user.service;

import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {
    public UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
            user.getId(), 
            user.getUsername(), 
            user.getAuthorities(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
