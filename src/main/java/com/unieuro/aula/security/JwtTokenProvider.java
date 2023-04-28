package com.unieuro.aula.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.unieuro.aula.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtTokenProvider {

    private static final String SECRET_KEY = "KendrickLamar";

    private static final long EXPIRATION_TIME = 86_400_000L;

    public String generateToken(Optional<UserEntity> userExists) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userExists.get().getId());
        claims.put("email", userExists.get().getEmail());

        return Jwts.builder()
                .setSubject(String.valueOf(userExists.get().getId()))
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return "valid";
        } catch (SignatureException ex) {
            return "Invalid Token";
        } catch (MalformedJwtException ex) {
            return "Invalid Token";
        } catch (ExpiredJwtException ex) {
            return "Expired Token";
        } catch (UnsupportedJwtException ex) {
            return "Unsupported Token";
        } catch (IllegalArgumentException ex) {
            return "Token is Empty";
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.get("id").toString());
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
