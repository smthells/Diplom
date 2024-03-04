package com.example.diplom.dto;

import com.example.diplom.validation.annotation.FilenameNotExist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FilenameUpdate {
    @FilenameNotExist
    private String name;
}
