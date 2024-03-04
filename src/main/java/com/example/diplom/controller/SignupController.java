package com.example.diplom.controller;

import com.example.diplom.dto.SignupDetails;
import com.example.diplom.service.SignupService;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class SignupController {
    private SignupService signupService;

    @GetMapping("/signup")
    public String signUp() {
        return "sign_up";
    }

    @PostMapping("/signup")
    public String signUp(@Valid SignupDetails details) {
        return signupService.signUp(details);
    }
}
