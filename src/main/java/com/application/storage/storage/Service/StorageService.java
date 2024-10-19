package com.application.storage.storage.Service;

import com.application.storage.storage.Entity.StorageEntity;
import com.application.storage.storage.Repository.StorageRepository;
import com.application.storage.storage.dto.request.StorageDetailRequestDto;
import com.application.storage.storage.dto.request.StorageSaveRequestDto;
import com.application.storage.storage.dto.response.AdditionalInfoDto;
import com.application.storage.storage.dto.response.StorageDetailResponseDto;
import com.application.storage.storage.exception.ErrorResponse;
import com.application.storage.storage.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

// Initialize the logger


@Service
public class StorageService {
    private final StorageRepository storageRepository;

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png");

    Logger log = LoggerFactory.getLogger(StorageService.class);


    @Autowired
    public StorageService(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public StorageDetailResponseDto getStorageDetail(StorageDetailRequestDto request) throws StorageException {
        try {
            log.info("Start request to get storage detail: {}", request);
            StorageEntity storageEntity = storageRepository.findByNomorSeriBarang(request.getNoSerial());
            log.info("Storage found: {}", storageEntity);
            if (storageEntity == null) {
                throw new StorageException(ErrorResponse.builder()
                        .ErrorMessage("Storage not found for serial number: " + request.getNoSerial())
                        .ErrorStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .ErrorCode(HttpStatus.NOT_FOUND.toString())
                        .build(), HttpStatus.NOT_FOUND);
            }

            return StorageDetailResponseDto.builder()
                    .namaBarang(storageEntity.getNamaBarang())
                    .idBarang(storageEntity.getIdBarang())
                    .size(storageEntity.getAdditionalInfo().getSize())
                    .color(storageEntity.getAdditionalInfo().getColor())
                    .material(storageEntity.getAdditionalInfo().getMaterial())
                    .nomorSeriBarang(storageEntity.getNomorSeriBarang())
                    .build();
        } catch (StorageException e) {
            log.error(e.getMessage(), e);

            throw new StorageException(ErrorResponse.builder()
                    .ErrorCode(e.getStatus().toString())
                    .ErrorMessage(e.getMessage())
                    .ErrorStatus(e.getStatus().name())
                    .build(), e.getStatus());

        }
    }

    public List<StorageDetailResponseDto> getStorageList() throws StorageException {
    try {
        List<StorageEntity> storageEntities = storageRepository.findAll();
        log.info("get Storage list for entities {}  ", storageEntities);

        return storageEntities.stream()
               .map(storageEntity -> StorageDetailResponseDto.builder()
                       .namaBarang(storageEntity.getNamaBarang())
                       .idBarang(storageEntity.getIdBarang())
                       .size(storageEntity.getAdditionalInfo().getSize())
                       .color(storageEntity.getAdditionalInfo().getColor())
                       .material(storageEntity.getAdditionalInfo().getMaterial())
                       .nomorSeriBarang(storageEntity.getNomorSeriBarang())
                       .build())
               .collect(Collectors.toList());

    } catch (Exception e) {
        throw new StorageException(ErrorResponse.builder()
               .ErrorCode("500")
               .ErrorMessage(e.getMessage())
               .ErrorStatus("internal server error")
               .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void addStorage(StorageSaveRequestDto request) {
        try {
            log.info("Start request to add storage: {}", request);
            byte[] bytes = Base64.getDecoder().decode(request.getImage());

            if (!isValidImage(bytes)) {
                throw new StorageException(ErrorResponse.builder()
                       .ErrorMessage("Invalid image format")
                       .ErrorStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())
                       .ErrorCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString())
                       .build(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            StorageEntity storageEntity = StorageEntity.builder()
                   .namaBarang(request.getNameItem())
                    .jumlahStokBarang(request.getStockItem())
                   .additionalInfo(AdditionalInfoDto.builder()
                           .size(request.getSize())
                           .color(request.getColor())
                           .material(request.getMaterial())
                           .build())
                   .nomorSeriBarang(request.getNoSerial())
                    .gambarBarang(bytes)
                    .createdBy(request.getCreatedBy())
                    .createdAt(LocalDateTime.now())
                   .build();

            storageRepository.save(storageEntity);
            log.info("Storage added: {}", storageEntity);
        } catch (StorageException e) {
            log.error(e.getMessage(), e);

            throw new StorageException(ErrorResponse.builder()
                    .ErrorCode(e.getStatus().toString())
                    .ErrorMessage(e.getMessage())
                    .ErrorStatus(e.getStatus().name())
                    .build(), e.getStatus());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStorage(StorageSaveRequestDto request) {
        try {
            log.info("Start request to update storage: {}", request);
            StorageEntity storageEntity = storageRepository.findByNomorSeriBarang(request.getNoSerial());

            if (ObjectUtils.isEmpty(storageEntity)) {
                throw new StorageException(ErrorResponse.builder()
                       .ErrorMessage("Storage not found for serial number when update: " + request.getNoSerial())
                       .ErrorStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                       .ErrorCode(HttpStatus.NOT_FOUND.toString())
                       .build(),
                        HttpStatus.NOT_FOUND);
            }

            storageEntity.setNamaBarang(request.getNameItem());
            storageEntity.setJumlahStokBarang(request.getStockItem());
            storageEntity.getAdditionalInfo().setSize(request.getSize());
            storageEntity.getAdditionalInfo().setColor(request.getColor());
            storageEntity.getAdditionalInfo().setMaterial(request.getMaterial());
            storageEntity.setNomorSeriBarang(request.getNoSerial());
            storageEntity.setGambarBarang(Base64.getDecoder().decode(request.getImage()));

            storageRepository.save(storageEntity);

        } catch (StorageException e) {
            log.error(e.getMessage(), e);

            throw new StorageException(ErrorResponse.builder()
                    .ErrorCode(e.getStatus().toString())
                    .ErrorMessage(e.getMessage())
                    .ErrorStatus(e.getStatus().name())
                    .build(),
                    e.getStatus());
        }
    }

    public void deleteStorage(StorageDetailRequestDto request) {
        try {
            log.info("Start request to delete storage: {}", request.getNoSerial());
            StorageEntity storageEntity = storageRepository.findByNomorSeriBarang(request.getNoSerial());

            if (ObjectUtils.isEmpty(storageEntity)) {
                throw new StorageException(ErrorResponse.builder()
                       .ErrorMessage("Storage not found for serial number when delete: " + request.getNoSerial())
                       .ErrorStatus(HttpStatus.NOT_FOUND.getReasonPhrase())
                       .ErrorCode(HttpStatus.NOT_FOUND.toString())
                       .build(),
                        HttpStatus.NOT_FOUND);
            }

            storageRepository.delete(storageEntity);

        } catch (StorageException e) {
            log.error(e.getMessage(), e);

            throw new StorageException(ErrorResponse.builder()
                    .ErrorCode(e.getStatus().toString())
                    .ErrorMessage(e.getMessage())
                    .ErrorStatus(e.getStatus().name())
                    .build(),
                    e.getStatus());
        }
    }

    private boolean isValidImage(byte[] image) throws IOException {
        log.info("Start isValidImage : {}", image);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
             ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream)) {

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(imageInputStream);

            if (!imageReaders.hasNext()) {
                return false;
            }

            ImageReader imageReader = imageReaders.next();
            String typeMime = imageReader.getFormatName().toLowerCase();
            imageReader.dispose();

            return ALLOWED_MIME_TYPES.contains("image/" + typeMime);
        }
    }
}