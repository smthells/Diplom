package com.example.diplom.dto;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LogInAuthorities {
    private String login;
    @Size(min = 6)
    private String password;
}
