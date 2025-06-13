package com.lims.lims_study.presentation.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.test.dto.TestCreateDto;
import com.lims.lims_study.application.test.dto.TestCreateDtoTest;
import com.lims.lims_study.application.test.dto.TestResponseDto;
import com.lims.lims_study.application.test.service.ITestApplicationService;
import com.lims.lims_study.application.test.service.TestDtoMapper;
import com.lims.lims_study.config.TestSecurityConfig;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.test.model.RequestInfo;
import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.global.config.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TestController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import(TestSecurityConfig.class)
public class TestControllerTestRepository {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private ITestApplicationService testApplicationService;

    @SpyBean
    private TestDtoMapper dtoMapper;

    @Test
    void shouldCreateTest() throws Exception {
        User user = new User(1L, "user", "password", "USER");

        Product product = new Product(1L, "product", "product");
        product.setCreatedAt(LocalDateTime.now());

        TestCreateDtoTest createDto = new TestCreateDtoTest();
        createDto.setTitle("test");
        createDto.setDescription("test create");
        createDto.setProductId(1L);
        createDto.setRequiresApproval(false);

        RequestInfo requestInfo = new RequestInfo(createDto.getTitle(), createDto.getDescription(), createDto.isRequiresApproval());

        com.lims.lims_study.domain.test.model.Test test =
                new com.lims.lims_study.domain.test.model.Test(1L, 1L, requestInfo);
        test.setId(1L);
        test.setCreatedAt(LocalDateTime.now());

        TestResponseDto responseDto = dtoMapper.toResponseDto(test, user, product, requestInfo, null, null, null);

        when(testApplicationService.createTest(any(TestCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/tests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(document("test-create",
                        requestFields(
                                fieldWithPath("title").description("시험명"),
                                fieldWithPath("description").description("시험 설명"),
                                fieldWithPath("productId").description("제품 ID"),
                                fieldWithPath("requiresApproval").description("결재 사용 여부")
                        ),
                        responseFields(
                                fieldWithPath("id").description("시험 ID"),
                                fieldWithPath("requestInfo.title").description("의뢰 제목"),
                                fieldWithPath("requestInfo.description").description("의뢰 설명"),
                                fieldWithPath("requestInfo.requiresApproval").description("의뢰 결재 사용 여부"),
                                fieldWithPath("receiptInfo").description("접수정보"),
                                fieldWithPath("resultInfo").description("결과입력 정보"),
                                fieldWithPath("completionInfo").description("완료 정보"),
                                fieldWithPath("user.id").description("유저 ID"),
                                fieldWithPath("user.username").description("유저명"),
                                fieldWithPath("user.authorities").description("유저권한"),
                                fieldWithPath("product.id").description("제품 ID"),
                                fieldWithPath("product.name").description("제품명"),
                                fieldWithPath("product.description").description("제품설명"),
                                fieldWithPath("product.createdAt").description("제품 생성일시"),
                                fieldWithPath("product.updatedAt").description("제품 수정일시"),
                                fieldWithPath("stage").description("시험단계"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시").optional()
                        )
                ));
    }

}
