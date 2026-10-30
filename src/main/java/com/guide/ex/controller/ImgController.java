package com.guide.ex.controller;

import com.guide.ex.dto.member.MemberProfileDTO;
import com.guide.ex.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Log4j2
public class ImgController {

    @Value("${com.guide.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    @GetMapping("/photo/{fileName:.+}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String fileName) throws IOException {
        Path baseDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        Path filePath = baseDir.resolve(fileName).normalize();
        // ../ 등으로 업로드 폴더 밖 파일을 읽지 못하게 막는다
        if (!filePath.startsWith(baseDir)) {
            return ResponseEntity.badRequest().build();
        }
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        String contentType = Files.probeContentType(filePath);
        MediaType mediaType = contentType != null && contentType.startsWith("image/")
                ? MediaType.parseMediaType(contentType)
                : MediaType.APPLICATION_OCTET_STREAM;

        // <img>로 바로 보여야 하므로 attachment 가 아닌 inline
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }


}
