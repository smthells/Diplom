package com.example.diplom.validation;

import com.example.diplom.repository.UserRepository;
import com.example.diplom.validation.annotation.UsernameIsUnique;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class UsernameIsUniqueValidator implements ConstraintValidator<UsernameIsUnique, String> {
    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsUserByUsername(username);
    }
}
