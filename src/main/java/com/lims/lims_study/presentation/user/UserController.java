package com.lims.lims_study.presentation.user;

import com.lims.lims_study.application.user.dto.UserCreateDto;
import com.lims.lims_study.application.user.dto.UserResponseDto;
import com.lims.lims_study.application.user.dto.UserSearchDto;
import com.lims.lims_study.application.user.dto.UserUpdateDto;
import com.lims.lims_study.application.user.service.IUserApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserApplicationService userApplicationService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto dto) {
        UserResponseDto response = userApplicationService.createUser(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto dto) {
        UserResponseDto response = userApplicationService.updateUser(userId, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userApplicationService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        UserResponseDto response = userApplicationService.getUser(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        UserResponseDto response = userApplicationService.getCurrentUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> searchUsers(@ModelAttribute UserSearchDto dto) {
        List<UserResponseDto> response = userApplicationService.searchUsers(dto);
        return ResponseEntity.ok(response);
    }
}