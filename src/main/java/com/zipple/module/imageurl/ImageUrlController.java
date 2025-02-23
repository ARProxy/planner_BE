package com.zipple.module.imageurl;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class ImageUrlController {

    @Hidden
    @GetMapping(value = "/zipple/**")
    public ResponseEntity<Resource> viewImage(HttpServletRequest request) {
        try {
            String requestUri = request.getRequestURI();

            String relativePath = requestUri.substring("/zipple/".length());
            Path filePath = Paths.get(File.separator, relativePath);

            Resource resource = new FileSystemResource(filePath.toFile());

            if (resource.exists() && resource.isReadable()) {
                String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException ex) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Hidden
    @GetMapping(value = "/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.ok().build();
    }
}
