package com.lims.lims_study.application.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String username;
    private final String authorities;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    public UserResponseDto(Long id, String username, String authorities, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserResponseDto from(com.lims.lims_study.domain.user.model.User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getAuthorities(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
