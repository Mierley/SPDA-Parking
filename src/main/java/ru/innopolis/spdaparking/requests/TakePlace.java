package ru.innopolis.spdaparking.requests;

import lombok.Data;

import java.util.Date;

@Data
public class TakePlace {
    private Long parkingPlaceId;
    private String driverName;
    private String carDetails;
    private String phoneNumber;
    private Date dateFrom;
    private Date dateTo;
}
