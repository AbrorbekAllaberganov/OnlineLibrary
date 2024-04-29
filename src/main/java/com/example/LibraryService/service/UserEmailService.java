package com.example.LibraryService.service;

import com.example.LibraryService.payload.RegisterPayload;
import com.example.LibraryService.payload.Result;

public interface UserEmailService {
    Result saveUserByEmailCode(RegisterPayload registerPayload);

    String generateEmailCode();

    void sendCodeToEmail(String email, String code);

    Result checkEmailCode(String email,String code);
}
