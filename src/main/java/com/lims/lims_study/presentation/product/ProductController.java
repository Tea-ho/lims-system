package com.lims.lims_study.presentation.product;

import com.lims.lims_study.application.product.dto.ProductCreateDto;
import com.lims.lims_study.application.product.dto.ProductResponseDto;
import com.lims.lims_study.application.product.dto.ProductSearchDto;
import com.lims.lims_study.application.product.dto.ProductUpdateDto;
import com.lims.lims_study.application.product.service.IProductApplicationService;
import com.lims.lims_study.global.common.ApiResponse;
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

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        try {
            return ResponseEntity.ok("API 엔드포인트가 정상 작동합니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        try {
            List<ProductResponseDto> response = productApplicationService.getAllProducts();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> searchProducts(@ModelAttribute ProductSearchDto dto) {
        List<ProductResponseDto> response = productApplicationService.searchProducts(dto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}