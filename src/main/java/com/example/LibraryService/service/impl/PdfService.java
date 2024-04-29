package com.example.LibraryService.service.impl;

import com.example.LibraryService.payload.Result;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class PdfService {

    @Value("${upload}")
    String upload;


    public String createPdf(String name, String text) {
        try {
            String filePath = upload + File.separator + name+".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk = new Chunk(text, font);

            document.add(chunk);
            document.close();

//            PDDocument document = new PDDocument();
//            PDPage page = new PDPage();
//            document.addPage(page);
//            PDPageContentStream contentStream = new PDPageContentStream(document, page);
//            contentStream.setFont(PDType1Font.HELVETICA, 12);
//
//            contentStream.beginText();
//            contentStream.newLineAtOffset(50, 700);
//
//            contentStream.showText(text);
//            contentStream.endText();
//            document.save(filePath);
            return filePath;
        } catch (IOException | DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadFile(String fileName, String text) throws IOException {
        String filePath = createPdf(fileName, text);

        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        RestTemplate restTemplate = new RestTemplate();

        Resource fileResource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return Paths.get(filePath).getFileName().toString();
            }
        };

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", fileResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:8081/api/auth/attachment/save",
                requestEntity,
                String.class
        );

        return response.getBody();
    }
}
