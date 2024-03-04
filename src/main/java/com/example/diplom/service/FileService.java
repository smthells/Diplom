package com.example.diplom.service;

import com.example.diplom.dto.FileDetails;
import com.example.diplom.dto.FilenameUpdate;
import com.example.diplom.entity.File;

import java.io.FileOutputStream;
import java.nio.file.Files;

import com.example.diplom.entity.User;
import com.example.diplom.repository.FileRepository;
import com.example.diplom.repository.UserRepository;
import com.example.diplom.util.FileBuilder;

import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public ResponseEntity<byte[]> download(String filename, Principal principal) {
        String username = principal.getName();
        File holder = getFileHolder(filename, username);
        Path path = Path.of(holder.getLink());
        byte[] file = readFile(path);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    private File getFileHolder(String filename, String username) {
        return fileRepository
                .getFileByFilenameAndUsername(filename, username)
                .orElseThrow(() -> new RuntimeException("Unable to get file"));
    }

    private byte[] readFile(Path path) {
        byte[] file;
        try {
            file = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file: " + e.getMessage());
        }
        return file;
    }

    @Transactional
    public ResponseEntity<String> upload(MultipartFile file, String filename, Principal principal) {
        FileBuilder fileBuilder = new FileBuilder();
        String username = principal.getName();
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Unable to find user"));
        File holder = fileBuilder.buildFileHolder(file, filename, user);
        uploadFileToDrive(holder.getLink(), file);
        fileRepository.save(holder);
        return new ResponseEntity<>("File was successfully uploaded on user cloud", HttpStatus.OK);
    }

    private void uploadFileToDrive(String link, MultipartFile file) {
        try (FileOutputStream stream = new FileOutputStream(link)) {
            stream.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to upload file: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<String> update(String filename, FilenameUpdate update, Principal principal) {
        String filenameUpdate = update.getName();
        String username = principal.getName();
        fileRepository.updateFilenameByNameAndUsername(filename, filenameUpdate, username);
        if (fileExists(filenameUpdate, username)) {
            return new ResponseEntity<>("File was updated", HttpStatus.OK);
        } else {
            throw new RuntimeException("Unable to update filename");
        }
    }

    private boolean fileExists(String filename, String username) {
        return fileRepository.getFileByFilenameAndUsername(filename, username).isPresent();
    }

    @Transactional
    public ResponseEntity<String> delete(String fileName, Principal principal) {
        String username = principal.getName();
        String link = fileRepository.getLinkByFilenameNameAndUsername(fileName, username);
        Path path = Path.of(link);
        fileRepository.deleteFileByFilenameAndUsername(fileName, username);
        if (!fileExists(fileName, username)) {
            deleteFileFromDrive(path);
            return new ResponseEntity<>("File was deleted", HttpStatus.OK);
        } else {
            throw new RuntimeException("Unable to delete file");
        }
    }

    private void deleteFileFromDrive(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Unable to delete file from drive: " + e.getMessage());
        }
    }

    public ResponseEntity<List<FileDetails>> getFiles(int limit, Principal principal) {
        String username = principal.getName();
        List<File> page = fileRepository.getAllFiles(username, PageRequest.of(0, limit));
        List<FileDetails> files = page.stream().map(a -> new FileDetails(a.getName(),
                a.getSize())).collect(Collectors.toList());
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
}
