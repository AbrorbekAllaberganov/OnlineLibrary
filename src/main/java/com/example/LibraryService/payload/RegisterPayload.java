package com.example.LibraryService.payload;

import io.swagger.annotations.ApiModel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description = "Register details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterPayload {
    String userName;
    String email;
    String password;
    String firstName;
    String lastName;
    String bio;

}
