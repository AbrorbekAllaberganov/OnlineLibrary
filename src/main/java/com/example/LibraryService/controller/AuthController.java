package com.example.LibraryService.controller;

import com.example.LibraryService.payload.LoginPayload;
import com.example.LibraryService.payload.RegisterPayload;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.service.impl.AuthServiceImpl;
import com.example.LibraryService.service.impl.UserEmailServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl authService;
    private final UserEmailServiceImpl userEmailService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginPayload loginPayload) {
        Result result = authService.loginUser(loginPayload);
        return ResponseEntity.status(result.isStatus() ? 200 : 401).body(result);
    }

    @PostMapping("/register")
    public ResponseEntity<Result> registerUser(@RequestBody RegisterPayload registerPayload) {
        Result result=userEmailService.saveUserByEmailCode(registerPayload);
        return ResponseEntity.status(result.isStatus() ? 200 : 409).body(result);
    }

    @GetMapping("/check-code")
    public ResponseEntity<Result> checkCode(@RequestParam("email")String email,
                                            @RequestParam("code") String code) {
        Result result=userEmailService.checkEmailCode(email,code);
        return ResponseEntity.status(result.isStatus() ? 200 : 409).body(result);
    }

}

