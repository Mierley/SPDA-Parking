package ru.innopolis.spdaparking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingPlaceDto {
    private Long id;
    private String tag;
    private Boolean isLock;
    private boolean ByUser;
}
