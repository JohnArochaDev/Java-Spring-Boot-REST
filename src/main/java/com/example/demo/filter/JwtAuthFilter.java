package com.example.demo.filter;

import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import com.example.demo.util.EncryptionUtil;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException, java.io.IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String userName = jwtService.extractUsername(token);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    String encodedKey = System.getenv("SECRET_KEY");
                    SecretKey secretKey = EncryptionUtil.getKeyFromString(encodedKey);
                    String decryptedUserName = EncryptionUtil.decrypt(userName, secretKey);
                    UserDetails user = userService.loadUserByUsername(decryptedUserName);
                    if (jwtService.validateToken(token, user)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to encrypt username", e);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}