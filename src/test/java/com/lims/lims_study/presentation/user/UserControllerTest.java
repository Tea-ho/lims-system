package com.lims.lims_study.presentation.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.user.dto.UserCreateDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.application.user.dto.UserUpdateDto;
import com.lims.lims_study.application.user.service.IUserApplicationService;
import com.lims.lims_study.config.TestSecurityConfig;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.global.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private IUserApplicationService userApplicationService;

    @Test
    void shouldRegisterUser() throws Exception {
        UserCreateDto createDto = new UserCreateDto();
        createDto.setUsername("user");
        createDto.setPassword("password");
        createDto.setAuthorities("USER");

        User user = new User(null, createDto.getUsername(), createDto.getPassword(), createDto.getAuthorities());

        UserResponseDto responseDto = new UserResponseDto(
                1L, user.getUsername(), user.getAuthorities()
        );

        when(userApplicationService.createUser(any(UserCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(document("user-register",
                        requestFields(
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("authorities").description("권한 목록")
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 ID"),
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("authorities").description("권한 목록")
                        )
                ));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setPassword("newpassword123");
        updateDto.setAuthorities("ADMIN");

        User user = new User(1L, "user", updateDto.getPassword(), updateDto.getAuthorities());

        UserResponseDto responseDto = new UserResponseDto(
                user.getId(), user.getUsername(), user.getAuthorities()
        );

        when(userApplicationService.updateUser(eq(1L), any(UserUpdateDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andDo(document("user-update",
                        pathParameters(
                                parameterWithName("id").description("사용자 ID")
                        ),
                        requestFields(
                                fieldWithPath("password").description("새 비밀번호").optional(),
                                fieldWithPath("authorities").description("새 권한 목록").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").description("사용자 ID"),
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("authorities").description("권한 목록")
                        )
                ));
    }
}