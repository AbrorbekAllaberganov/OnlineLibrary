package com.example.LibraryService.payload;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPayload {
    String firstName;
    String lastName;
    String bio;
    String email;
    String password;
}
