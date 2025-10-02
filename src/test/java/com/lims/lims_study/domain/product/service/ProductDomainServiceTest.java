package com.lims.lims_study.domain.product.service;

import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDomainServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator productValidator;

    @InjectMocks
    private ProductDomainService productDomainService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "테스트 제품", "테스트 설명");
    }

    @Test
    @DisplayName("제품 생성 테스트")
    void createProduct() {
        // given
        doNothing().when(productValidator).checkProductNameBlank(product);
        doNothing().when(productValidator).checkDuplicateProductName(product);
        doNothing().when(productRepository).insert(product);

        // when
        productDomainService.createProduct(product);

        // then
        verify(productValidator).checkProductNameBlank(product);
        verify(productValidator).checkDuplicateProductName(product);
        verify(productRepository).insert(product);
    }

    @Test
    @DisplayName("제품 수정 테스트")
    void updateProduct() {
        // given
        Product existingProduct = new Product(1L, "기존 제품", "기존 설명");

        when(productValidator.validateProductExists(1L))
                .thenReturn(existingProduct);
        doNothing().when(productRepository).update(any(Product.class));

        // Create ProductUpdateDto with proper values
        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setName("수정된 제품");
        updateDto.setDescription("수정된 설명");

        // when
        productDomainService.updateProduct(1L, updateDto);

        // then
        verify(productValidator).validateProductExists(1L);
        verify(productRepository).update(any(Product.class));
    }

    @Test
    @DisplayName("제품 삭제 테스트")
    void deleteProduct() {
        // given
        when(productValidator.validateProductExists(1L))
                .thenReturn(product);

        // when
        productDomainService.deleteProduct(1L);

        // then
        verify(productValidator).validateProductExists(1L);
        verify(productRepository).delete(1L);
    }

    @Test
    @DisplayName("제품 ID로 조회 테스트")
    void findById() {
        // given
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        // when
        Optional<Product> result = productDomainService.findById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals("테스트 제품", result.get().getName());
    }

    @Test
    @DisplayName("제품명으로 검색 테스트")
    void searchProducts() {
        // given
        List<Product> products = Arrays.asList(product);
        when(productRepository.searchByName("테스트"))
                .thenReturn(products);

        // when
        List<Product> result = productDomainService.searchProducts("테스트");

        // then
        assertEquals(1, result.size());
        assertEquals("테스트 제품", result.get(0).getName());
    }

    @Test
    @DisplayName("전체 제품 조회 테스트")
    void findAllProducts() {
        // given
        List<Product> products = Arrays.asList(product);
        when(productRepository.findAllProducts())
                .thenReturn(products);

        // when
        List<Product> result = productDomainService.findAllProducts();

        // then
        assertEquals(1, result.size());
        assertEquals("테스트 제품", result.get(0).getName());
    }
}
