package com.example.diplom.service;

import com.example.diplom.entity.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilterService extends OncePerRequestFilter {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;
    private final JwsProviderService provider;
    private final CustomUserDetailService customUserDetailService;
    @Value("${auth-header}")
    private String authHeader;
    private final String beginningOfAToken = "Bearer ";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest servletRequest,
                                    @NonNull HttpServletResponse servletResponse,
                                    @NonNull FilterChain filterChain) {
        request = servletRequest;
        response = servletResponse;
        chain = filterChain;
        String header = getHeader();
        if (headerNotValid(header)) {
            filter();
            return;
        }
        String jwt = getJwt(header);
        String subject = getTokenSubject(jwt);
        if (jwtIsNotValid(jwt, subject)) {
            filter();
            return;
        }
        UserDetails userDetails = getUserDetails(subject);
        if (userDetails == null) {
            filter();
            return;
        }
        setAuthentication(userDetails);
        filter();
    }

    private String getHeader() {
        return request.getHeader(authHeader);
    }

    private boolean headerNotValid(String header) {
        return header == null || !header.startsWith(beginningOfAToken);
    }

    private void filter() {
        try {
            chain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJwt(String header) {
        return header.substring(beginningOfAToken.length()).trim();
    }

    private String getTokenSubject(String jwt) {
        String subject = provider.getSubject(jwt);
        if (subject == null) {
            filter();
        }
        return subject;
    }

    private boolean jwtIsNotValid(String jwt, String subject) {
        return !provider.verify(jwt, subject);
    }

    private User getUserDetails(String subject) {
        return customUserDetailService.loadUserByUsername(subject);
    }

    private void setAuthentication(UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
