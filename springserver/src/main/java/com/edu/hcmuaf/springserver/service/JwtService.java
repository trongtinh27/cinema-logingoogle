package com.edu.hcmuaf.springserver.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edu.hcmuaf.springserver.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String Secret_key = "123";
    private static final int tokenExpirationTime = 50*60*1000;

    public String generateToken(User user, Collection<SimpleGrantedAuthority> authorities) {
        // Thêm thông tin cần thiết vào token

        Algorithm algorithm = Algorithm.HMAC256(Secret_key.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationTime))
                .withClaim("role", authorities.stream().toList().get(0).getAuthority())
                .sign(algorithm);
    }

    public String extractTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public int getTokenExpirationTime() {
        return tokenExpirationTime;
    }
}

