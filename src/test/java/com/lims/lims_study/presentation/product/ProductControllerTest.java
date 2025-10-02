package com.lims.lims_study.presentation.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lims.lims_study.application.product.dto.ProductCreateDto;
import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.product.dto.ProductSearchDto;
import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.application.product.service.IProductApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class,
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
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IProductApplicationService productApplicationService;

    private ProductResponseDto productResponseDto;

    @BeforeEach
    void setUp() {
        productResponseDto = new ProductResponseDto(
                1L,
                "테스트 제품",
                "테스트 설명",
                "2025-01-01 00:00:00",
                "2025-01-01 00:00:00"
        );
    }

    @Test
    @DisplayName("제품 생성 테스트")
    void createProduct() throws Exception {
        String createDtoJson = "{\"name\":\"신규 제품\",\"description\":\"테스트 설명\"}";

        when(productApplicationService.createProduct(any(ProductCreateDto.class)))
                .thenReturn(productResponseDto);

        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 제품"));
    }

    @Test
    @DisplayName("제품 수정 테스트")
    void updateProduct() throws Exception {
        String updateDtoJson = "{\"name\":\"수정된 제품\",\"description\":\"수정된 설명\"}";

        when(productApplicationService.updateProduct(eq(1L), any(ProductUpdateDto.class)))
                .thenReturn(productResponseDto);

        mockMvc.perform(put("/api/products/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("제품 삭제 테스트")
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/api/products/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("제품 조회 테스트")
    void getProduct() throws Exception {
        when(productApplicationService.getProduct(1L))
                .thenReturn(productResponseDto);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 제품"));
    }

    @Test
    @DisplayName("전체 제품 조회 테스트")
    void getAllProducts() throws Exception {
        List<ProductResponseDto> products = Arrays.asList(productResponseDto);

        when(productApplicationService.getAllProducts())
                .thenReturn(products);

        mockMvc.perform(get("/api/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("테스트 제품"));
    }

    @Test
    @DisplayName("제품 검색 테스트")
    void searchProducts() throws Exception {
        List<ProductResponseDto> products = Arrays.asList(productResponseDto);

        when(productApplicationService.searchProducts(any(ProductSearchDto.class)))
                .thenReturn(products);

        mockMvc.perform(get("/api/products")
                        .param("productName", "테스트"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].name").value("테스트 제품"));
    }

    @Test
    @DisplayName("테스트 엔드포인트 확인")
    void testEndpoint() throws Exception {
        mockMvc.perform(get("/api/products/test"))
                .andExpect(status().isOk());
    }
}
