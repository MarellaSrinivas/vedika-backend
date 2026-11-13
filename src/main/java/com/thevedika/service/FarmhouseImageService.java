
package com.thevedika.service;

import com.thevedika.dto.FarmhouseImageRequest;
import com.thevedika.dto.farmhouse.FarmhouseResponse;
import com.thevedika.entity.Farmhouse;
import com.thevedika.entity.FarmhouseImage;
import com.thevedika.entity.User;
import com.thevedika.repository.FarmhouseImageRepository;
import com.thevedika.repository.FarmhouseRepository;
import com.thevedika.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FarmhouseImageService {

    private final FarmhouseImageRepository imageRepository;
    private final FarmhouseRepository farmhouseRepository;
    private final UserRepository userRepository;

    public FarmhouseImageService(
            FarmhouseImageRepository imageRepository,
            FarmhouseRepository farmhouseRepository,
            UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.farmhouseRepository = farmhouseRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<FarmhouseResponse.ImageResponse> getAdminImages(
            Long farmhouseId) {
        verifyFarmhouseExists(farmhouseId);
        return getImages(farmhouseId);
    }

    @Transactional
    public FarmhouseResponse.ImageResponse addAdminImage(
            Long farmhouseId,
            FarmhouseImageRequest request) {
        Farmhouse farmhouse = verifyFarmhouseExists(farmhouseId);
        return addImage(farmhouse, request);
    }

    @Transactional
    public FarmhouseResponse.ImageResponse updateAdminImage(
            Long farmhouseId,
            Long imageId,
            FarmhouseImageRequest request) {
        verifyFarmhouseExists(farmhouseId);
        return updateImage(farmhouseId, imageId, request);
    }

    @Transactional
    public void deleteAdminImage(Long farmhouseId, Long imageId) {
        verifyFarmhouseExists(farmhouseId);
        deleteImage(farmhouseId, imageId);
    }

    @Transactional(readOnly = true)
    public List<FarmhouseResponse.ImageResponse> getOwnerImages(
            String phone,
            Long farmhouseId) {
        verifyOwnerFarmhouse(phone, farmhouseId);
        return getImages(farmhouseId);
    }

    @Transactional
    public FarmhouseResponse.ImageResponse addOwnerImage(
            String phone,
            Long farmhouseId,
            FarmhouseImageRequest request) {
        Farmhouse farmhouse = verifyOwnerFarmhouse(phone, farmhouseId);
        return addImage(farmhouse, request);
    }

    @Transactional
    public FarmhouseResponse.ImageResponse updateOwnerImage(
            String phone,
            Long farmhouseId,
            Long imageId,
            FarmhouseImageRequest request) {
        verifyOwnerFarmhouse(phone, farmhouseId);
        return updateImage(farmhouseId, imageId, request);
    }

    @Transactional
    public void deleteOwnerImage(
            String phone,
            Long farmhouseId,
            Long imageId) {
        verifyOwnerFarmhouse(phone, farmhouseId);
        deleteImage(farmhouseId, imageId);
    }

    private Farmhouse verifyFarmhouseExists(Long farmhouseId) {
        return farmhouseRepository.findById(farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found"));
    }

    private Farmhouse verifyOwnerFarmhouse(
            String phone,
            Long farmhouseId) {
        User owner = userRepository.findByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Owner account not found"));

        return farmhouseRepository
                .findByIdAndOwner_Id(farmhouseId, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Farmhouse not found for this owner"));
    }

    private List<FarmhouseResponse.ImageResponse> getImages(
            Long farmhouseId) {
        return imageRepository
                .findByFarmhouseIdOrderByDisplayOrderAsc(farmhouseId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private FarmhouseResponse.ImageResponse addImage(
            Farmhouse farmhouse,
            FarmhouseImageRequest request) {
        validateRequest(request);

        if (Boolean.TRUE.equals(request.getCoverImage())) {
            imageRepository.clearCoverImage(farmhouse.getId());
        }

        FarmhouseImage image = new FarmhouseImage();
        image.setFarmhouse(farmhouse);
        image.setImageUrl(request.getImageUrl().trim());
        image.setDisplayOrder(
                request.getDisplayOrder() == null
                        ? 0
                        : request.getDisplayOrder());
        image.setCoverImage(
                Boolean.TRUE.equals(request.getCoverImage()));

        return toResponse(imageRepository.save(image));
    }

    private FarmhouseResponse.ImageResponse updateImage(
            Long farmhouseId,
            Long imageId,
            FarmhouseImageRequest request) {
        validateRequest(request);

        FarmhouseImage image = imageRepository
                .findByIdAndFarmhouseId(imageId, farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Image not found"));

        if (Boolean.TRUE.equals(request.getCoverImage())) {
            imageRepository.clearCoverImage(farmhouseId);
        }

        image.setImageUrl(request.getImageUrl().trim());

        if (request.getDisplayOrder() != null) {
            image.setDisplayOrder(request.getDisplayOrder());
        }

        image.setCoverImage(
                Boolean.TRUE.equals(request.getCoverImage()));

        return toResponse(imageRepository.save(image));
    }

    private void deleteImage(Long farmhouseId, Long imageId) {
        FarmhouseImage image = imageRepository
                .findByIdAndFarmhouseId(imageId, farmhouseId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Image not found"));

        imageRepository.delete(image);
    }

    private void validateRequest(FarmhouseImageRequest request) {
        if (request == null
                || request.getImageUrl() == null
                || request.getImageUrl().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Image URL is required");
        }

        if (request.getImageUrl().length() > 500) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Image URL cannot exceed 500 characters");
        }

        if (request.getDisplayOrder() != null
                && request.getDisplayOrder() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Display order cannot be negative");
        }
    }

    private FarmhouseResponse.ImageResponse toResponse(
            FarmhouseImage image) {
        return new FarmhouseResponse.ImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.getDisplayOrder(),
                image.getCoverImage());
    }
}
