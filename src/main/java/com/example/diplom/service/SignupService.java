package com.example.diplom.service;

import com.example.diplom.dto.SignupDetails;
import com.example.diplom.entity.User;
import com.example.diplom.repository.UserRepository;
import com.example.diplom.util.UserBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {
    private final UserRepository repository;

    public SignupService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String signUp(SignupDetails details) {
        UserBuilder builder = new UserBuilder();
        User user = builder.buildUser(details);
        repository.save(user);
        String username = user.getUsername();
        if (userExistsInDatabase(username)) {
            return "successfully_signed_up";
        } else {
            throw new RuntimeException("Unable to sign up");
        }
    }

    private boolean userExistsInDatabase(String username) {
        return repository.existsUserByUsername(username);
    }
}
