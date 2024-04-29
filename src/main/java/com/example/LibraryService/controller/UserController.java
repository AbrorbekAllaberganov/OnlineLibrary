package com.example.LibraryService.controller;

import com.example.LibraryService.payload.Result;
import com.example.LibraryService.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl adminService;

    @GetMapping
    public ResponseEntity<Result> getMe(){
        Result result= adminService.getMe();
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

}
