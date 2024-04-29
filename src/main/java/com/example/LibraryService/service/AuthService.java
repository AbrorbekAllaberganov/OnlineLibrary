package com.example.LibraryService.service;

import com.example.LibraryService.payload.LoginPayload;
import com.example.LibraryService.payload.Result;

public interface AuthService {
    Result loginUser(LoginPayload loginPayload);
}
