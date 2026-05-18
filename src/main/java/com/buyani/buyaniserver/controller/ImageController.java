package com.buyani.buyaniserver.controller;

import com.buyani.buyaniserver.util.StorageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/image")
public class ImageController {

    @GetMapping
    public ResponseEntity<byte[]> getImage(@RequestParam String key) {
        try {
            StorageUtil storageUtil = new StorageUtil();
            byte[] imageBytes = storageUtil.getImageBytes(key);
            if (imageBytes == null) {
                return ResponseEntity.notFound().build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching image: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}