package com.lims.lims_study.presentation.product;

import com.lims.lims_study.application.product.dto.ProductCreateDto;
import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.product.dto.ProductSearchDto;
import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.application.product.service.IProductApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductApplicationService productApplicationService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateDto dto) {
        ProductResponseDto response = productApplicationService.createProduct(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long productId,
                                                            @RequestBody ProductUpdateDto dto) {
        ProductResponseDto response = productApplicationService.updateProduct(productId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productApplicationService.deleteProduct(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long productId) {
        ProductResponseDto response = productApplicationService.getProduct(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> searchProducts(@ModelAttribute ProductSearchDto dto) {
        List<ProductResponseDto> response = productApplicationService.searchProducts(dto);
        return ResponseEntity.ok(response);
    }
}