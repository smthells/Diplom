package com.example.diplom;

import com.example.diplom.entity.Role;
import com.example.diplom.entity.User;
import com.example.diplom.repository.UserRepository;
import com.example.diplom.service.CustomUserDetailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;

    @Test
    public void testLoadUserByEmailAndUserFound() {
        String username = "user@test.com";
        User user = new User("user", "user@test.com", "password",
                "User", "Userov", 11, Role.USER);
        Mockito.when(repository.findUserByEmail(username)).thenReturn(Optional.of(user));
        User loadedUser = customUserDetailService.loadUserByUsername(username);
        Assertions.assertEquals(user, loadedUser);
    }

    @Test
    public void testLoadUserByEmailAndUserNotFound() {
        String username = "user@test.com";
        Mockito.when(repository.findUserByEmail(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> {
            customUserDetailService.loadUserByUsername(username);
        });
    }
}

