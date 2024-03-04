package com.example.diplom.util;

import com.example.diplom.entity.File;
import com.example.diplom.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
public class FileBuilder {

    public File buildFileHolder(MultipartFile file, String filename, User user) {
        String username = user.getUsername();
        FolderCreator folderCreator = new FolderCreator(username);
        if (folderCreator.userFolderNotExists()) {
            folderCreator.createUserFolderInDrive(username);
        }
        return buildFileHolderObject(file, filename, user, username);
    }


    private File buildFileHolderObject(MultipartFile file, String filename, User user, String username) {
        return File.builder()
                .name(filename)
                .size(getFileSize(file))
                .user(user)
                .link(createLinkToFile(filename, username))
                .build();
    }

    private long getFileSize(MultipartFile file) {
        return file.getSize();
    }

    private String createLinkToFile(String filename, String username) {
        String path = System.getProperty("user.home");
        String dataFolder = "CloudData";
        return path + "/" + dataFolder + "/" + username + "/" + filename;
    }

}
