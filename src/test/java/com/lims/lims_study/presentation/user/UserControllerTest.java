package com.lims.lims_study.presentation.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.user.dto.UserCreateDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.application.user.dto.UserSearchDto;
import com.lims.lims_study.application.user.dto.UserUpdateDto;
import com.lims.lims_study.application.user.service.IUserApplicationService;
import com.lims.lims_study.global.common.PageRequest;
import com.lims.lims_study.global.common.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
    },
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
        classes = {
            com.lims.lims_study.global.config.SecurityConfig.class,
            com.lims.lims_study.global.config.JwtAuthenticationFilter.class,
            com.lims.lims_study.global.config.JwtProvider.class
        }
    ))
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IUserApplicationService userApplicationService;

    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        userResponseDto = new UserResponseDto(
                1L,
                "testuser",
                "ROLE_USER",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    @DisplayName("사용자 생성 테스트")
    void createUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto(
                "newuser",
                "Password123!",  // 대소문자, 숫자, 특수문자 포함
                "ROLE_USER"
        );

        when(userApplicationService.createUser(any(UserCreateDto.class)))
                .thenReturn(userResponseDto);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @DisplayName("사용자 수정 테스트")
    void updateUser() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto(
                "NewPassword123!",  // 대소문자, 숫자, 특수문자 포함
                "ROLE_ADMIN"
        );

        when(userApplicationService.updateUser(eq(1L), any(UserUpdateDto.class)))
                .thenReturn(userResponseDto);

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("사용자 조회 테스트")
    void getUser() throws Exception {
        when(userApplicationService.getUser(1L))
                .thenReturn(userResponseDto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @DisplayName("현재 사용자 조회 테스트")
    void getCurrentUser() throws Exception {
        when(userApplicationService.getCurrentUser())
                .thenReturn(userResponseDto);

        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @DisplayName("사용자 검색 테스트")
    void searchUsers() throws Exception {
        PageRequest pageRequest = new PageRequest(0, 10);
        PageResponse<UserResponseDto> pageResponse = new PageResponse<>(
                Collections.singletonList(userResponseDto), pageRequest, 1L
        );

        when(userApplicationService.searchUsers(any(UserSearchDto.class)))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].username").value("testuser"));
    }

    @Test
    @DisplayName("사용자명 존재 여부 확인 테스트")
    void checkUsernameExists() throws Exception {
        when(userApplicationService.existsByUsername("testuser"))
                .thenReturn(true);

        mockMvc.perform(get("/api/users/exists")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(true));
    }
}
