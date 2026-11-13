
package com.thevedika.controller;

import com.thevedika.dto.FarmhouseImageRequest;
import com.thevedika.dto.farmhouse.FarmhouseResponse;
import com.thevedika.entity.Farmhouse;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.service.FarmhouseFileStorageService;
import com.thevedika.service.FarmhouseImageService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin/farmhouses/{farmhouseId}/images")
public class AdminFarmhouseImageController {

        private final FarmhouseRepository farmhouseRepository;
        private final FarmhouseFileStorageService fileStorageService;
        private final FarmhouseImageService imageService;

        public AdminFarmhouseImageController(
                        FarmhouseRepository farmhouseRepository,
                        FarmhouseFileStorageService fileStorageService,
                        FarmhouseImageService imageService) {
                this.farmhouseRepository = farmhouseRepository;
                this.fileStorageService = fileStorageService;
                this.imageService = imageService;
        }

        @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @ResponseStatus(HttpStatus.CREATED)
        public FarmhouseResponse.ImageResponse uploadImage(
                        @PathVariable Long farmhouseId,
                        @RequestParam("file") MultipartFile file,
                        @RequestParam(defaultValue = "0") Integer displayOrder,
                        @RequestParam(defaultValue = "false") Boolean coverImage) {

                // Verify farmhouse exists before storing the file
                Farmhouse farmhouse = farmhouseRepository.findById(farmhouseId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Farmhouse not found"));

                if (displayOrder == null || displayOrder < 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Invalid image display order");
                }

                // Save physical file and get its public URL
                String imageUrl = fileStorageService.storeFarmhouseImage(
                                farmhouse.getId(),
                                file);

                // Save image details in database
                FarmhouseImageRequest request = new FarmhouseImageRequest();
                request.setImageUrl(imageUrl);
                request.setDisplayOrder(displayOrder);
                request.setCoverImage(coverImage);

                return imageService.addAdminImage(farmhouseId, request);
        }
}
