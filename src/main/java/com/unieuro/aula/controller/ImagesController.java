package com.unieuro.aula.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
            try {
                String fileName = image.getOriginalFilename();
                Path imagePath = Paths.get("src/data/images", fileName);
                imagesRepository.save(new ImagesEntity(id, imagePath.toString()));
                Files.write(imagePath, image.getBytes());
                Map<String, String> successMap = new HashMap<>();
                successMap.put("success", "Arquivo salvo no servidor");
                return ResponseEntity.status(HttpStatus.OK).body(successMap);
            } catch (IOException e) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("error", "Error writing image file");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

    @GetMapping("/images/{imageName:.+}")
    public ResponseEntity<?> getImage(@PathVariable String imageName, HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        String validated = jwtTokenProvider.validateToken(token);
        if ("valid".equals(validated)) {
            String imagePath = "src/data/images/" + imageName;
            Path path = Paths.get(imagePath);
            byte[] imageBytes;
            try {
                imageBytes = Files.readAllBytes(path);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading image file", e);
            }
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", validated);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMap);
        }
    }

}
