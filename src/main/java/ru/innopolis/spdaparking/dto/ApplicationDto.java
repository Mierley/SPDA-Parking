package ru.innopolis.spdaparking.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ApplicationDto {
    private Long id;
    private String driverName;
    private String carDetails;
    private String phoneNumber;
    private Date dateFrom;
    private Date dateTo;
    private Long parkingPlaceId;
    private String parkingPlaceTag;
}