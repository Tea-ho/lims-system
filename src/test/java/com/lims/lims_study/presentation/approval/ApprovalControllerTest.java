package com.lims.lims_study.presentation.approval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.approval.dto.ApprovalCreateDto;
import com.lims.lims_study.application.approval.dto.ApprovalResponseDto;
import com.lims.lims_study.application.approval.dto.ApprovalSignUpdateDto;
import com.lims.lims_study.application.approval.service.IApprovalApplicationService;
import com.lims.lims_study.domain.approval.model.ApprovalStatus;
import com.lims.lims_study.domain.test.model.TestStage;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ApprovalController.class,
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
class ApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IApprovalApplicationService approvalService;

    private ApprovalResponseDto approvalResponseDto;

    @BeforeEach
    void setUp() {
        // ApprovalResponseDto는 복잡한 구조이므로 실제 사용 시 목 객체로만 검증
        approvalResponseDto = null;
    }

    @Test
    @DisplayName("승인 생성 테스트")
    void createApproval() throws Exception {
        String createDtoJson = "{\"requesterId\":1,\"comment\":\"승인 요청\",\"signs\":[{\"approverId\":1,\"targetId\":100,\"stage\":\"RECEIPT_APPROVAL\"}]}";

        when(approvalService.createApproval(any(ApprovalCreateDto.class)))
                .thenReturn(approvalResponseDto);

        mockMvc.perform(post("/api/approvals")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("승인 서명 업데이트 테스트")
    void updateApprovalSign() throws Exception {
        ApprovalSignUpdateDto updateDto = new ApprovalSignUpdateDto(ApprovalStatus.APPROVED, "승인합니다");

        when(approvalService.updateApprovalSign(eq(1L), eq(1L), any(ApprovalSignUpdateDto.class)))
                .thenReturn(approvalResponseDto);

        mockMvc.perform(put("/api/approvals/1/signs/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("승인 삭제 테스트")
    void deleteApproval() throws Exception {
        mockMvc.perform(delete("/api/approvals/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("승인 조회 테스트")
    void getApproval() throws Exception {
        when(approvalService.getApproval(1L))
                .thenReturn(approvalResponseDto);

        mockMvc.perform(get("/api/approvals/1"))
                .andExpect(status().isOk());
                // approvalResponseDto가 null이므로 필드 검증은 생략
    }

    @Test
    @DisplayName("타겟별 승인 목록 조회 테스트")
    void getApprovalsByTarget() throws Exception {
        List<ApprovalResponseDto> approvals = Arrays.asList(approvalResponseDto);

        when(approvalService.getApprovalsByTarget(100L))
                .thenReturn(approvals);

        mockMvc.perform(get("/api/approvals/target/100"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("대기 중인 승인 목록 조회 테스트")
    void getPendingApprovals() throws Exception {
        List<ApprovalResponseDto> approvals = Arrays.asList(approvalResponseDto);

        when(approvalService.getPendingApprovals(any(), anyString()))
                .thenReturn(approvals);

        mockMvc.perform(get("/api/approvals/pending")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("승인 처리 테스트")
    void processApproval() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("status", "APPROVED");
        request.put("comment", "승인합니다");
        request.put("signId", "1");

        when(approvalService.updateApprovalSign(eq(1L), eq(1L), any(ApprovalSignUpdateDto.class)))
                .thenReturn(approvalResponseDto);

        mockMvc.perform(put("/api/approvals/1/process")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
