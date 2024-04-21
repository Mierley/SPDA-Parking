package ru.innopolis.spdaparking.requests;

import lombok.Data;

import java.util.Date;

@Data
public class StatisticsFilter{
    private Date fromDate;
    private Date toDate;
    private String driverName;
    private String phoneNumber;
    private String carDetails;
    private String parkingPlaceTag;
}