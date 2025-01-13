package com.yelman.identityserver.services;


import com.yelman.identityserver.model.User;
import com.yelman.identityserver.model.role.Role;
import com.yelman.identityserver.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;

@Service
public class JwtService {
    private  final UserRepository userRepository;

    @Value("${security.jwt.token.secret-key}")
    private String SECRET;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String userName, String role,String id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", userName); // Kullanıcı adı
        claims.put("roles", role); // Rol bilgisini ekliyoruz
        claims.put("id", id);
        return createToken(claims, userName);
    }
    public List<String> getRole(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username).orElse(null));

        if (user.isPresent()) {
            Set<Role> roles = user.get().getAuthorities();
            if (!roles.isEmpty()) {
                List<String> roleList = new ArrayList<>();
                roleList.add(roles.iterator().next().getAuthority());
                roleList.add(user.get().getId().toString());
                return roleList;
            }
        }

        // Eğer kullanıcı veya rol bulunmazsa null döndür
        return null;
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUser(token);
        Date expirationDate = extractExpiration(token);
        return userDetails.getUsername().equals(username) && !expirationDate.before(new Date());
    }

    private Date extractExpiration(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    public String extractUser(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Token oluşturma metodu
    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2)) // 2 dakika
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
