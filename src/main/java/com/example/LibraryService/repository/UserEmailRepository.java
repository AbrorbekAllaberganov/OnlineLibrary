package com.example.LibraryService.repository;

import com.example.LibraryService.entity.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserEmailRepository extends JpaRepository<UserEmail, UUID> {
    @Query(value = "select * from user_email where email=?1",nativeQuery = true)
    UserEmail findUserEmailByEmail(String email);

    @Query(value = "delete from user_email where email=?1",nativeQuery = true)
    void deleteUserEmailByEmail(String email);

    boolean existsByEmail(String email);
}
