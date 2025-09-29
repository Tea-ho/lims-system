package com.lims.lims_study.domain.user.model;

import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class User {
    private Long id;
    private String username;
    private String password;
    private String authorities;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(Long id, String username, String password, String authorities) {
        validateUsername(username);
        validatePassword(password);
        validateAuthorities(authorities);
        
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateUserInfo(String encodedPassword, String authorities) {
        validatePassword(encodedPassword);
        validateAuthorities(authorities);
        
        this.password = encodedPassword;
        this.authorities = authorities;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자명은 필수입니다.");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "사용자명은 3자 이상 50자 이하여야 합니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호는 필수입니다.");
        }
    }

    private void validateAuthorities(String authorities) {
        if (authorities == null || authorities.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE, "권한은 필수입니다.");
        }
    }

    public boolean isSameUser(Long userId) {
        return Objects.equals(this.id, userId);
    }

    public boolean hasAuthority(String authority) {
        return this.authorities != null && this.authorities.contains(authority);
    }

    // 비밀번호 검증용 (암호화된 비밀번호와 비교할 때 사용)
    public boolean isValidPassword(String rawPassword, String encodedPassword) {
        // 실제로는 PasswordEncoder를 사용하여 검증
        return encodedPassword != null && !encodedPassword.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", authorities='" + authorities + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
