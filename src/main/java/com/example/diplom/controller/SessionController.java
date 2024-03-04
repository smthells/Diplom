package com.example.diplom.controller;

import com.example.diplom.dto.JWTElement;
import com.example.diplom.dto.LogInAuthorities;
import com.example.diplom.service.SessionService;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping("/login")
    public ResponseEntity<JWTElement> logIn(@RequestBody @Valid LogInAuthorities authorities) {
        return sessionService.logIn(authorities);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logOut() {
        return sessionService.logOut();
    }
}
