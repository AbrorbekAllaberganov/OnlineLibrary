package com.example.LibraryService.controller.authController;

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
@CrossOrigin(value = "*", maxAge = 3600L)
//@CrossOrigin(value = "http://10.13.1.152:5173", maxAge = 3600L)
@Api(value = "/api/auth", description = "Login and register APIs, this url doesn't require JWT TOKEN")
public class AuthController {
    private final AuthServiceImpl authService;
    private final UserEmailServiceImpl userEmailService;

    @PostMapping("/login")
    @ApiOperation(value = "User Login API", notes = "user can login by user name and password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully login by user"),
            @ApiResponse(code = 401, message = "User has not registered yet")
    })
    public ResponseEntity<?> loginUser(@RequestBody LoginPayload loginPayload) {
        Result result = authService.loginUser(loginPayload);
        return ResponseEntity.status(result.isStatus() ? 200 : 401).body(result);
    }

    @PostMapping("/register")
    @ApiOperation(value = "User Register API", notes = "This API saves user in addition table, if user enters correct gmail code, user register fully")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registration of user"),
            @ApiResponse(code = 409, message = "Email or user name are already registered")
    })
    public ResponseEntity<Result> registerUser(@RequestBody RegisterPayload registerPayload) {
        Result result=userEmailService.saveUserByEmailCode(registerPayload);
        return ResponseEntity.status(result.isStatus() ? 200 : 409).body(result);
    }

    @GetMapping("/check-code")
    @ApiOperation(value = "Check confirm code API", notes = "If user enters correct confirmation code, user finishes registarition part")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The code which is entered is correct or incorrect"),
            @ApiResponse(code = 409, message = "The email is not found")
    })
    public ResponseEntity<Result> checkCode(@RequestParam("email")String email,
                                            @RequestParam("code") String code) {
        Result result=userEmailService.checkEmailCode(email,code);
        return ResponseEntity.status(result.isStatus() ? 200 : 409).body(result);
    }

}

