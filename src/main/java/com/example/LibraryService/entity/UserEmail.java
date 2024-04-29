package com.example.LibraryService.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    String userName;
    String email;
    String firstName;
    String lastName;
    String bio;
    String password;
    String emailCode;

    @CreationTimestamp
    Date createdAt;
}
