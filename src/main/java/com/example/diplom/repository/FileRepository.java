package com.example.diplom.repository;

import com.example.diplom.entity.File;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {
    @Modifying
    @Transactional
    @Query("delete from File f where f.user.username = :username and lower(f.name) = lower(:filename)")
    void deleteFileByFilenameAndUsername(String filename, String username);

    @Transactional
    @Query("select f from File f where f.user.username = :username and lower(f.name) = lower(:filename)")
    Optional<File> getFileByFilenameAndUsername(@Param("filename") String filename, String username);

    @Modifying
    @Transactional
    @Query("update File f set f.name = :updateFilename where f.user.username = :username and" +
           " lower(f.name) = lower(:filename)")
    void updateFilenameByNameAndUsername(String filename, String updateFilename, String username);

    @Transactional
    @Query("select f from File f left join fetch f.user where f.user.username = :username")
    List<File> getAllFiles(String username, PageRequest page);

    @Transactional
    @Query("select f.link from File f where f.user.username = :username and lower(f.name) = lower(:filename)")
    String getLinkByFilenameNameAndUsername(String filename, String username);
}
