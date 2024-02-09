package com.pillar.bridge.util.jwt;

import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import org.slf4j.Logger;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.access.expiration}")
    private Long accessExpiration;
    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    public String generateAccessToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateRefreshToken(String uuid) {
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUuidFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    // access token 유효성 검증
    public boolean validateToken(String authToken) throws JwtException{
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.info("유효하지 않은 토큰입니다");
            throw new JwtException("잘못된 형식의 토큰입니다.");
        } catch (ExpiredJwtException ex) {
            logger.info("만료된 토큰입니다");
            throw new JwtException("토큰이 만료되었습니다.");
        } catch (UnsupportedJwtException ex) {
            logger.info("잘못된 토큰입니다");
            throw new JwtException("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            logger.info("토큰이 비어있습니다");
            throw new JwtException("토큰이 비어있습니다.");
        }
    }

    //refresh token 유효성 검증
    public boolean validateRefreshToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.info("유효하지 않은 토큰입니다");
        } catch (ExpiredJwtException ex) {
            logger.info("만료된 토큰입니다");
        } catch (UnsupportedJwtException ex) {
            logger.info("잘못된 토큰입니다");
        } catch (IllegalArgumentException ex) {
            logger.info("토큰이 비어있습니다");
        }
        return false;
    }

}
