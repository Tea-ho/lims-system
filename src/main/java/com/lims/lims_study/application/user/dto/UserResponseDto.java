package com.lims.lims_study.application.user.dto;

public class UserResponseDto {
    private final Long id;
    private final String username;
    private final String authorities;

    public UserResponseDto(Long id, String username, String authorities) {
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getAuthorities() { return authorities; }
}