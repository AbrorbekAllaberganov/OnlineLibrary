package com.example.LibraryService.repository;


import com.example.LibraryService.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParentRepository extends JpaRepository<Parent, UUID> {
    Parent findByUserName (String username);

    @Query(value = "select * from parent where email=?1",nativeQuery = true)
    Parent findByEmail(String email);

}
