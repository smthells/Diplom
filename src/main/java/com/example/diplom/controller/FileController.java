package com.example.diplom.controller;

import com.example.diplom.dto.FileDetails;
import com.example.diplom.dto.FilenameUpdate;
import com.example.diplom.service.FileService;
import com.example.diplom.validation.annotation.FilenameExist;
import com.example.diplom.validation.annotation.FilenameNotExist;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<String> upload(@RequestParam("filename")
                                         @FilenameNotExist String filename,
                                         @RequestBody MultipartFile file,
                                         Principal user) {
        return fileService.upload(file, filename, user);
    }

    @DeleteMapping("/file")
    public ResponseEntity<String> delete(@RequestParam("filename")
                                         @FilenameExist String filename,
                                         Principal user) {
        return fileService.delete(filename, user);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> download(@RequestParam("filename")
                                           @FilenameExist String filename,
                                           Principal user) {
        return fileService.download(filename, user);
    }


    @PutMapping("/file")
    public ResponseEntity<String> update(@RequestParam("filename")
                                         @FilenameExist String filename,
                                         @RequestBody @Valid FilenameUpdate filenameUpdate,
                                         Principal user) {
        return fileService.update(filename, filenameUpdate, user);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileDetails>> getFiles(@RequestParam("limit") int limit,
                                                      Principal user) {
        return fileService.getFiles(limit, user);
    }
}
