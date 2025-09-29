package com.lims.lims_study.global.config;

import com.lims.lims_study.global.exception.BusinessException;
import com.lims.lims_study.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtProvider(@Value("${jwt.secret}") String secret, 
                      @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    /**
     * JWT 토큰 생성
     */
    public String generateToken(Authentication authentication) {
        String username;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            username = userPrincipal.getUsername();
        } else {
            username = (String) authentication.getPrincipal();
        }

        Date expiryDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자명 추출
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * JWT 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        } catch (MalformedJwtException e) {
            log.error("JWT token is malformed: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("JWT token validation error: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * JWT 토큰에서 만료 시간 추출
     */
    public Date getExpirationFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    /**
     * JWT 토큰이 만료되었는지 확인
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
