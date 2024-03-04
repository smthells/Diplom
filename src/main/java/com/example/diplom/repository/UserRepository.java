package com.example.diplom.repository;

import com.example.diplom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Transactional
    boolean existsUserByUsername(String username);

    @Transactional
    Optional<User> findUserByEmail(String username);

    @Transactional
    Optional<User> findUserByUsername(String username);
}

