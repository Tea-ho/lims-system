package com.lims.lims_study.application.product.service;

import com.lims.lims_study.application.product.dto.ProductCreateDto;
import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.product.dto.ProductSearchDto;
import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.domain.product.model.Product;
import com.lims.lims_study.domain.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductApplicationService implements IProductApplicationService {
    private final IProductService productService;
    private final ProductDtoMapper productDtoMapper;

    @Override
    public ProductResponseDto createProduct(ProductCreateDto dto) {
        Product product = new Product(null, dto.getName(), dto.getDescription());
        productService.createProduct(product);
        return productDtoMapper.toResponseDto(product);
    }

    @Override
    public ProductResponseDto updateProduct(Long productId, ProductUpdateDto dto) {
        productService.updateProduct(productId, dto);
        Product updated = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + productId));
        return productDtoMapper.toResponseDto(updated);
    }

    @Override
    public void deleteProduct(Long productId) {
        productService.deleteProduct(productId);
    }

    @Override
    public ProductResponseDto getProduct(Long productId) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("ProductRepository not found: " + productId));
        return productDtoMapper.toResponseDto(product);
    }

    @Override
    public List<ProductResponseDto> searchProducts(ProductSearchDto dto) {
        String searchName = (dto != null && dto.getName() != null && !dto.getName().trim().isEmpty())
                          ? dto.getName() : null;
        List<Product> products = productService.searchProducts(searchName);
        return products.stream()
                .map(productDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return products.stream()
                .map(productDtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}