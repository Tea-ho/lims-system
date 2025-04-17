package com.lims.lims_study.presentation.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.product.dto.ProductCreateDto;
import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.product.service.IProductApplicationService;
import com.lims.lims_study.application.product.service.ProductDtoMapper;
import com.lims.lims_study.config.TestSecurityConfig;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.global.config.JwtUtil;
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


@WebMvcTest(ProductController.class)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import(TestSecurityConfig.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private IProductApplicationService productApplicationService;

    @SpyBean
    private ProductDtoMapper dtoMapper;

    @Test
    void shouldCreateProduct() throws Exception {
        ProductCreateDto createDto = new ProductCreateDto();
        createDto.setName("product");
        createDto.setDescription("product Test");

        Product product = new Product(1L, createDto.getName(), createDto.getDescription());
        product.setCreatedAt(LocalDateTime.now());

        ProductResponseDto responseDto = dtoMapper.toResponseDto(product);

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
