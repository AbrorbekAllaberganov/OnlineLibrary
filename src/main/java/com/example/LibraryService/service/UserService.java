package com.example.LibraryService.service;

import com.example.LibraryService.entity.User;
import com.example.LibraryService.payload.Result;


public interface UserService {
    Result getMe();
    User findUserById(Long id);
}
