package ru.innopolis.spdaparking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParkingPlaceCountByTag
{
    private String tag;
    private Long count;
}
