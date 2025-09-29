package com.lims.lims_study.presentation.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.user.dto.*;
import com.lims.lims_study.application.user.service.IUserApplicationService;
import com.lims.lims_study.config.TestSecurityConfig;
import com.lims.lims_study.config.TestConfig;
import com.lims.lims_study.global.config.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

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
import com.lims.lims_study.config.TestConfig;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import({TestSecurityConfig.class, TestConfig.class})
class UserRepositoryControllerTestRepository {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private IUserApplicationService userApplicationService;

    @Test
    void shouldRegisterUser() throws Exception {
        UserCreateDtoTest createDto = new UserCreateDtoTest();
        createDto.setUsername("testuser123");
        createDto.setPassword("TestPass123!@*");
        createDto.setAuthorities("ROLE_USER");

        LocalDateTime now = LocalDateTime.now();

        UserResponseDto responseDto = new UserResponseDto(
                1L, 
                createDto.getUsername(), 
                createDto.getAuthorities(),
                now,
                now
        );

        when(userApplicationService.createUser(any(UserCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andDo(document("user-register",
                        requestFields(
                                fieldWithPath("username").description("사용자 이름"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("authorities").description("권한 목록")
                        ),
                        responseFields(
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data.id").description("사용자 ID"),
                                fieldWithPath("data.username").description("사용자 이름"),
                                fieldWithPath("data.authorities").description("권한 목록"),
                                fieldWithPath("data.createdAt").description("생성 시간"),
                                fieldWithPath("data.updatedAt").description("수정 시간"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("timestamp").description("응답 시간")
                        )
                ));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserUpdateDtoTest updateDto = new UserUpdateDtoTest();
        updateDto.setPassword("NewPass123!@*");
        updateDto.setAuthorities("ROLE_ADMIN");

        LocalDateTime now = LocalDateTime.now();

        UserResponseDto responseDto = new UserResponseDto(
                1L, 
                "testuser123", 
                updateDto.getAuthorities(),
                now.minusDays(1),
                now
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
                                fieldWithPath("success").description("요청 성공 여부"),
                                fieldWithPath("data.id").description("사용자 ID"),
                                fieldWithPath("data.username").description("사용자 이름"),
                                fieldWithPath("data.authorities").description("권한 목록"),
                                fieldWithPath("data.createdAt").description("생성 시간"),
                                fieldWithPath("data.updatedAt").description("수정 시간"),
                                fieldWithPath("message").description("응답 메시지"),
                                fieldWithPath("timestamp").description("응답 시간")
                        )
                ));
    }
}
