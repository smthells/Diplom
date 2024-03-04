package com.example.diplom.util;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

@AllArgsConstructor
public class FolderCreator {
    private String username;
    private final String data = "CloudData";

    public boolean userFolderNotExists() {
        return !Files.exists(getPathToUser());
    }

    public void createUserFolderInDrive(String username) {
        try {
            Files.createDirectories(getPathToUser());
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to create folder for user: " + username, e);
        }
    }

    public Path getPathToUser() {
        String pathToSystemUser = System.getProperty("user.home");
        return Path.of(pathToSystemUser, data, username);
    }

}
