package k_webtoons.k_webtoons.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs
    ) {
        byte[] keyBytes = Arrays.copyOf(secret.getBytes(), 32);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    // 토큰 생성 메서드: indexId를 추가
    public String generateToken(String username, String role, Long indexId) {
        try {
            return Jwts.builder()
                    .subject(username)
                    .claim("role", role)
                    .claim("id", indexId)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expirationMs))
                    .signWith(secretKey, Jwts.SIG.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("JWT 생성 실패", e);
        }
    }

    // 사용자 이메일 추출
    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    // 역할(role) 추출
    public String extractRole(String token) {
        return (String) parseToken(token).get("role");
    }

    // 사용자 ID 추출
    public Long extractId(String token) {
        return (Long) parseToken(token).get("id"); // 페이로드에서 'id' 추출
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 내부적으로 토큰 파싱 처리
    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
