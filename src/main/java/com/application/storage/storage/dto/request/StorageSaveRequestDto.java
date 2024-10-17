package com.application.storage.storage.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageSaveRequestDto {
    private String nameItem;
    private int stockItem;
    private String noSerial;
    private String size;
    private String color;
    private String material;
    private String image;
    private String createdBy;
}
