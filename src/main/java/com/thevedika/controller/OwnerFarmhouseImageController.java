
package com.thevedika.controller;

import com.thevedika.dto.FarmhouseImageRequest;
import com.thevedika.dto.farmhouse.FarmhouseResponse;
import com.thevedika.service.FarmhouseImageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.thevedika.service.FarmhouseFileStorageService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/owner/farmhouses/{farmhouseId}/images")
public class OwnerFarmhouseImageController {

    private final FarmhouseImageService imageService;
    private final FarmhouseFileStorageService fileStorageService;

    public OwnerFarmhouseImageController(
            FarmhouseImageService imageService,
            FarmhouseFileStorageService fileStorageService) {
        this.imageService = imageService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public ResponseEntity<List<FarmhouseResponse.ImageResponse>> getImages(
            @PathVariable Long farmhouseId,
            Authentication authentication) {
        return ResponseEntity.ok(
                imageService.getOwnerImages(
                        authentication.getName(), farmhouseId));
    }

    @PostMapping
    public ResponseEntity<FarmhouseResponse.ImageResponse> addImage(
            @PathVariable Long farmhouseId,
            @RequestBody FarmhouseImageRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.addOwnerImage(
                        authentication.getName(),
                        farmhouseId,
                        request));
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<FarmhouseResponse.ImageResponse> updateImage(
            @PathVariable Long farmhouseId,
            @PathVariable Long imageId,
            @RequestBody FarmhouseImageRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(
                imageService.updateOwnerImage(
                        authentication.getName(),
                        farmhouseId,
                        imageId,
                        request));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long farmhouseId,
            @PathVariable Long imageId,
            Authentication authentication) {
        imageService.deleteOwnerImage(
                authentication.getName(),
                farmhouseId,
                imageId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FarmhouseResponse.ImageResponse> uploadImage(
            @PathVariable Long farmhouseId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "0") Integer displayOrder,
            @RequestParam(defaultValue = "false") Boolean coverImage,
            Authentication authentication) {
        // Verify ownership before writing anything to disk.
        imageService.getOwnerImages(
                authentication.getName(),
                farmhouseId);

        String imageUrl = fileStorageService.storeFarmhouseImage(
                farmhouseId,
                file);

        FarmhouseImageRequest request = new FarmhouseImageRequest();
        request.setImageUrl(imageUrl);
        request.setDisplayOrder(displayOrder);
        request.setCoverImage(coverImage);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.addOwnerImage(
                        authentication.getName(),
                        farmhouseId,
                        request));
    }
}
