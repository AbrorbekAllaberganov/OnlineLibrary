package com.example.LibraryService.service.impl;

import com.example.LibraryService.entity.User;
import com.example.LibraryService.exceptions.BadRequest;
import com.example.LibraryService.exceptions.ResourceNotFound;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.payload.UserPayload;
import com.example.LibraryService.repository.ParentRepository;
import com.example.LibraryService.repository.UserRepository;
import com.example.LibraryService.security.SecurityUtils;
import com.example.LibraryService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SecurityUtils securityUtils;
    private final ParentRepository parentRepository;
    private final UserRepository userRepository;

    @Override
    public Result getMe() {
        try{
            Optional<String> currentUser = securityUtils.getCurrentUser();
            if (currentUser.isEmpty())
                throw new BadRequest("User not found");
            return Result.success(parentRepository.findByUserName(currentUser.get()));
        }catch (Exception e){
            return Result.exception(e);
        }
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                ()->new ResourceNotFound("user","id",id)
            );
    }
}
