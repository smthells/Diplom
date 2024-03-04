package com.example.diplom;

import com.example.diplom.dto.FilenameUpdate;
import com.example.diplom.entity.File;
import com.example.diplom.entity.User;
import com.example.diplom.repository.FileRepository;
import com.example.diplom.repository.UserRepository;
import com.example.diplom.service.FileService;
import com.example.diplom.util.FileBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class FileTest {
    @InjectMocks
    private FileService fileService;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private FileBuilder fileBuilder;

    @Test
    public void testUpdateFile() {
        String username = "username";
        String filename = "filename";
        String filenameUpdate = "filenameUpdate";
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn(username);
        Mockito.doNothing().when(fileRepository)
                .updateFilenameByNameAndUsername(any(), any(), any());
        FilenameUpdate update = new FilenameUpdate(filenameUpdate);
        doReturn(Optional.of(new File(1L,
                Mockito.mock(User.class),
                filename,
                1L,
                ""))).when(fileRepository)
                .getFileByFilenameAndUsername(update.getName(), username);
        fileService.update(filename, update, principal);
        verify(fileRepository, Mockito.times(1))
                .getFileByFilenameAndUsername(update.getName(), username);
    }

    @Test
    public void testUploadFile() {
        String username = "username";
        String filename = "test.txt";
        User user = new User();
        user.setUsername(username);
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test content".getBytes());
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(fileBuilder.buildFileHolder(Mockito.any(MultipartFile.class), Mockito.eq(filename), Mockito.eq(user)))
                .thenAnswer(invocation -> {
                    MultipartFile file = invocation.getArgument(0);
                    return new File(1L, user, filename, file.getSize(), "/path/to/uploaded/file");
                });
        ResponseEntity<String> response = fileService.upload(multipartFile, filename, () -> username);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File was successfully uploaded on user cloud", response.getBody());
        verify(fileRepository).save(Mockito.any(File.class));
    }
}
