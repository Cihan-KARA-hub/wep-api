package com.yelman.shopingserver.utils.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${security.jwt.token.secret-key}")
    private  String SECRET_KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        // Authorization header'ı kontrol ediyoruz
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Token'ı alıyoruz
        String token = header.substring(7);
        try {
            // Claims'i çözümlüyoruz
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // Kullanıcı adı ve roller
            String username = claims.getSubject();
            // "roles" claim'ini String olarak alıyoruz
            String roles = claims.get("roles", String.class);
            String id = claims.get("id",String.class);

            if (username != null && roles != null && !roles.isEmpty()) {
                List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority(roles));

                // Kullanıcıyı oluşturuyoruz
                User principal = new User(username, "", authorities);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Güvenlik context'ine Authentication'ı set ediyoruz
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Hata durumunda güvenlik context'ini temizliyoruz
            SecurityContextHolder.clearContext();
        }

        // Diğer filtrelere devam ediyoruz
        filterChain.doFilter(request, response);
    }
}
