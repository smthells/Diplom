package com.example.diplom.validation;

import com.example.diplom.entity.File;
import com.example.diplom.repository.FileRepository;
import com.example.diplom.validation.annotation.FilenameNotExist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class FilenameNotExistValidator implements ConstraintValidator<FilenameNotExist, String> {
    private final FileRepository fileRepository;

    public FilenameNotExistValidator(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public boolean isValid(String filename, ConstraintValidatorContext constraintValidatorContext) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<File> holder = fileRepository.getFileByFilenameAndUsername(filename, username);
        return holder.isEmpty();
    }
}
