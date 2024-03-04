package com.example.diplom.service;

import com.example.diplom.dto.JWTElement;
import com.example.diplom.dto.LogInAuthorities;
import com.example.diplom.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SessionService {
    private AuthenticationManager authenticationManager;
    private JwsProviderService provider;

    public ResponseEntity<JWTElement> logIn(LogInAuthorities authorities) {
        String login = authorities.getLogin();
        String password = authorities.getPassword();
        Authentication authentication = authenticate(login, password);
        String username = getUsernameFromAuthentication(authentication);
        JWTElement jwt = generateJwt(username);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    private Authentication authenticate(String login, String password) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login, password));
    }

    private String getUsernameFromAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getEmail();
    }

    private JWTElement generateJwt(String username) {
        String jwt = provider.build(username);
        return new JWTElement(jwt);
    }

    public ResponseEntity<String> logOut() {
        return new ResponseEntity<>("Logged out", HttpStatus.OK);
    }
}
