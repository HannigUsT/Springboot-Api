package com.unieuro.aula.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unieuro.aula.model.ImagesEntity;
import com.unieuro.aula.repository.ImagesRepository;
import com.unieuro.aula.security.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ImagesController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ImagesRepository imagesRepository;

    @PostMapping("/images/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            Long id = jwtTokenProvider.getUserIdFromToken(token);
            imagesRepository.save(new ImagesEntity(id));
            try {
                byte[] imageBytes = image.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                return ResponseEntity.ok(base64Image);
            } catch (Exception e) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", "Error reading image file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

}
