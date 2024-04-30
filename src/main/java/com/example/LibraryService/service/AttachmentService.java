package com.example.LibraryService.service;

import com.example.LibraryService.payload.Result;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;

public interface AttachmentService {
    String saveFile(MultipartFile multipartFile);

}
