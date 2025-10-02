package com.lims.lims_study.domain.user.service;

import com.lims.lims_study.domain.user.model.User;
import com.lims.lims_study.domain.user.repository.UserRepository;
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
class UserDomainServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserDomainService userDomainService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testuser", "password123", "ROLE_USER");
    }

    @Test
    @DisplayName("사용자 생성 테스트")
    void createUser() {
        // given
        doNothing().when(userValidator).validateForCreation(user);
        doNothing().when(userRepository).insert(user);

        // when
        User result = userDomainService.createUser(user);

        // then
        verify(userValidator).validateForCreation(user);
        verify(userRepository).insert(user);
        assertNotNull(result);
    }

    @Test
    @DisplayName("사용자 수정 테스트")
    void updateUser() {
        // given
        doNothing().when(userValidator).validateForUpdate(user);
        doNothing().when(userRepository).update(user);

        // when
        User result = userDomainService.updateUser(user);

        // then
        verify(userValidator).validateForUpdate(user);
        verify(userRepository).update(user);
        assertNotNull(result);
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void deleteUser() {
        // given
        when(userValidator.validateUserExists(1L))
                .thenReturn(user);

        // when
        userDomainService.deleteUser(1L);

        // then
        verify(userValidator).validateUserExists(1L);
        verify(userRepository).delete(1L);
    }

    @Test
    @DisplayName("ID로 사용자 조회 테스트")
    void findById() {
        // given
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        // when
        Optional<User> result = userDomainService.findById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    @DisplayName("사용자명으로 조회 테스트")
    void findByUsername() {
        // given
        when(userRepository.findByUsername("testuser"))
                .thenReturn(Optional.of(user));

        // when
        Optional<User> result = userDomainService.findByUsername("testuser");

        // then
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    @DisplayName("사용자 검색 테스트")
    void searchUsers() {
        // given
        List<User> users = Arrays.asList(user);
        when(userRepository.searchUsers("test", null, 0, 10))
                .thenReturn(users);

        // when
        List<User> result = userDomainService.searchUsers("test", null, 0, 10);

        // then
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }

    @Test
    @DisplayName("검색 조건으로 사용자 수 카운트 테스트")
    void countBySearchCriteria() {
        // given
        when(userRepository.countBySearchCriteria("test", null))
                .thenReturn(1L);

        // when
        long count = userDomainService.countBySearchCriteria("test", null);

        // then
        assertEquals(1L, count);
    }

    @Test
    @DisplayName("사용자 ID 존재 여부 확인 테스트")
    void existsById() {
        // given
        when(userRepository.existsById(1L))
                .thenReturn(true);

        // when
        boolean exists = userDomainService.existsById(1L);

        // then
        assertTrue(exists);
    }

    @Test
    @DisplayName("사용자명 존재 여부 확인 테스트")
    void existsByUsername() {
        // given
        when(userRepository.existsByUsername("testuser"))
                .thenReturn(true);

        // when
        boolean exists = userDomainService.existsByUsername("testuser");

        // then
        assertTrue(exists);
    }
}
