package com.application.storage.storage.Controller;

import com.application.storage.storage.Service.StorageService;
import com.application.storage.storage.dto.request.StorageDetailRequestDto;
import com.application.storage.storage.dto.request.StorageSaveRequestDto;
import com.application.storage.storage.dto.response.StorageDetailResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StorageController {
    public final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }
    @PostMapping("/{version}/storage/get-detail")
    public ResponseEntity<StorageDetailResponseDto> getAllEmployee(@PathVariable("version") String version,@RequestBody StorageDetailRequestDto request) throws Exception {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalStateException("request name null");
        }
        StorageDetailResponseDto response = storageService.getStorageDetail(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{version}/storage/get-all")
    public ResponseEntity<List<StorageDetailResponseDto>> getAllStorage(@PathVariable("version") String version
                                                                        ) throws Exception {

        List<StorageDetailResponseDto> responses = storageService.getStorageList();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PostMapping("/{version}/storage/add")
    public ResponseEntity<String> addStorage(@PathVariable("version") String version,@RequestBody StorageSaveRequestDto request) throws Exception {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalStateException("request add storage null");
        }
        storageService.addStorage(request);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/{version}/storage/update")
    public ResponseEntity<String> updateStorage(@PathVariable("version") String version,@RequestBody StorageSaveRequestDto request) throws Exception {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalStateException("request update storage null");
        }
        storageService.updateStorage(request);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PostMapping("/{version}/storage/delete")
    public ResponseEntity<String> deleteStorage(@PathVariable("version") String version,@RequestBody StorageDetailRequestDto request) throws Exception {
        if (ObjectUtils.isEmpty(request)) {
            throw new IllegalStateException("request delete storage null");
        }
        storageService.deleteStorage(request);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
