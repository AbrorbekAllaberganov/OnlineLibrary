package com.example.LibraryService.service.impl;

import com.example.LibraryService.entity.Attachment;
import com.example.LibraryService.payload.Result;
import com.example.LibraryService.repository.AttachmentRepository;
import com.example.LibraryService.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Value("${upload}")
    private Path downloadPath;

    @Override
    public String saveFile(MultipartFile multipartFile) {

//        try {
//            Files.copy(multipartFile.getInputStream(), this.downloadPath.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename())));
//            return Result.message("successful",true);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        Attachment attachment = new Attachment();

        attachment.setContentType(multipartFile.getContentType());
        attachment.setFileSize(multipartFile.getSize());
        attachment.setName(multipartFile.getOriginalFilename());
        attachment.setExtension(getExtension(attachment.getName()).toLowerCase());
        attachment.setHashId(UUID.randomUUID().toString());


        LocalDate date = LocalDate.now();

        // change value downloadPath
        String localPath = downloadPath + String.format(
                "/%d/%d/%d/%s",
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                attachment.getExtension().toLowerCase());

        attachment.setUploadPath(localPath);


        // downloadPath / year / month / day / extension
        File file = new File(localPath);

        // " downloadPath / year / month / day / extension "   crate directory
        file.mkdirs();

        // save MyFile into base
        attachment.setLink(file.getAbsolutePath() + "/" + String.format("%s.%s", attachment.getHashId(), attachment.getExtension()));

        attachmentRepository.save(attachment);

        try {
            // copy bytes into new file or saving into storage
            multipartFile.transferTo(new File(file.getAbsolutePath() + "/" + String.format("%s.%s", attachment.getHashId(), attachment.getExtension())));
            return attachment.getHashId();

        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    public Result deleteFile(String hashId) {
        return null;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Attachment findByHashId(String hashId) {
        return attachmentRepository.findByHashId(hashId);
    }
}
