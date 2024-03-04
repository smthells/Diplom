package com.example.diplom.dto;

import com.example.diplom.validation.annotation.UsernameIsUnique;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupDetails {
    @UsernameIsUnique
    private String username;
    private String name;
    private String lastname;
    private int age;
    private String email;
    private String password;
}
