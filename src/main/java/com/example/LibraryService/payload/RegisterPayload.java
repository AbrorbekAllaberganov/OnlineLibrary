package com.example.LibraryService.payload;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description = "Register details")
public class RegisterPayload {
    private String userName;
    private String email;
    private String password;

}
