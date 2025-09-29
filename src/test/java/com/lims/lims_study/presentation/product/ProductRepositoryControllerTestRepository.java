package com.lims.lims_study.presentation.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.product.dto.ProductCreateDto;
import com.lims.lims_study.application.product.dto.ProductCreateDtoTest;
import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.product.service.IProductApplicationService;
import com.lims.lims_study.config.TestSecurityConfig;
import com.lims.lims_study.config.TestConfig;
import com.lims.lims_study.global.common.ApiResponse;
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
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import({TestSecurityConfig.class, TestConfig.class})
public class ProductRepositoryControllerTestRepository {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private IProductApplicationService productApplicationService;

    @Test
    void shouldCreateProduct() throws Exception {
        ProductCreateDtoTest createDto = new ProductCreateDtoTest();
        createDto.setName("product");
        createDto.setDescription("product TestRepository");

        ProductResponseDto responseDto = new ProductResponseDto(
                1L,
                createDto.getName(),
                createDto.getDescription(),
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString()
        );

        when(productApplicationService.createProduct(any(ProductCreateDto.class))).thenReturn(responseDto);
        
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andDo(document("product-create",
                        requestFields(
                                fieldWithPath("name").description("제품명"),
                                fieldWithPath("description").description("제품설명")
                        ),
                        responseFields(
                                fieldWithPath("id").description("제품 ID"),
                                fieldWithPath("name").description("제품명"),
                                fieldWithPath("description").description("제품설명"),
                                fieldWithPath("createdAt").description("생성일시"),
                                fieldWithPath("updatedAt").description("수정일시")
                        )
                ));
    }
}
