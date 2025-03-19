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

    // jwt 토큰 생성 시크릿 키나 토큰유지시간은 properties 에서 설정
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMs
    ) {
        byte[] keyBytes = Arrays.copyOf(secret.getBytes(), 32);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
    }

    // 토큰생성
    public String generateToken(String username, String role) {
        try {
            return Jwts.builder()
                    .subject(username)
                    .claim("role", role)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + expirationMs))
                    .signWith(secretKey, Jwts.SIG.HS256)  // 명시적 알고리즘 지정
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("JWT 생성 실패", e);
        }
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public String extractRole(String token) {
        return (String) parseToken(token).get("role");
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
