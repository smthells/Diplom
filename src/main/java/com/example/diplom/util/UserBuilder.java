package com.example.diplom.util;

import com.example.diplom.dto.SignupDetails;
import com.example.diplom.entity.Role;
import com.example.diplom.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor
public class UserBuilder {
    public User buildUser(SignupDetails details) {
        return User.builder().username(details.getUsername())
                .name(details.getName())
                .lastname(details.getLastname())
                .age(details.getAge())
                .email(details.getEmail())
                .role(Role.USER)
                .password(encodePassword(details.getPassword()))
                .build();
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
