package com.lims.lims_study.presentation.user;

import com.lims.lims_study.application.user.dto.UserCreateDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.application.user.dto.UserSearchDto;
import com.lims.lims_study.application.user.dto.UserUpdateDto;
import com.lims.lims_study.application.user.service.IUserApplicationService;
import com.lims.lims_study.global.common.ApiResponse;
import com.lims.lims_study.global.common.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserApplicationService userApplicationService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody UserCreateDto dto) {
        log.info("Request to create user: {}", dto.getUsername());
        
        UserResponseDto response = userApplicationService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "" +
                        "사용자가 성공적으로 생성되었습니다."));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long userId, 
            @Valid @RequestBody UserUpdateDto dto) {
        log.info("Request to update user: {}", userId);
        
        UserResponseDto response = userApplicationService.updateUser(userId, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "사용자 정보가 성공적으로 수정되었습니다."));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        log.info("Request to delete user: {}", userId);
        
        userApplicationService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "사용자가 성공적으로 삭제되었습니다."));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable Long userId) {
        log.debug("Request to get user: {}", userId);
        
        UserResponseDto response = userApplicationService.getUser(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser() {
        log.debug("Request to get current user");
        
        UserResponseDto response = userApplicationService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponseDto>>> searchUsers(@Valid @ModelAttribute UserSearchDto dto) {
        log.debug("Request to search users with criteria: {}", dto);
        
        PageResponse<UserResponseDto> response = userApplicationService.searchUsers(dto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameExists(@RequestParam String username) {
        log.debug("Request to check username exists: {}", username);
        
        boolean exists = userApplicationService.existsByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }
}
