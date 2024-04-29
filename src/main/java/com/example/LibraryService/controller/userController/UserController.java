package com.example.LibraryService.controller.userController;

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
@CrossOrigin(value = "*", maxAge = 3600L)
@Api(value = "/api/user", description = "These APIs belong to registered user, and all of them require JWT TOKEN")
public class UserController {
    private final UserServiceImpl adminService;

    @GetMapping
    @ApiOperation(value = "GET USER INFO", notes = "Get all information of user by token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The token is correct, and user is found"),
            @ApiResponse(code = 409, message = "The token is incorrect")
    })
    public ResponseEntity<Result> getMe(){
        Result result= adminService.getMe();
        return ResponseEntity.status(result.isStatus()?200:409).body(result);
    }

}
