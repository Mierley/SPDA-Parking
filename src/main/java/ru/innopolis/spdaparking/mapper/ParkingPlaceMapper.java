package ru.innopolis.spdaparking.mapper;

import org.mapstruct.Mapper;
import ru.innopolis.spdaparking.dto.ParkingPlaceDto;
import ru.innopolis.spdaparking.entity.ParkingPlace;

@Mapper
public class ParkingPlaceMapper {
    public ParkingPlace mapToEntity(ParkingPlaceDto dto) {
        ParkingPlace parkingPlace = new ParkingPlace();
        parkingPlace.setId(dto.getId());
        parkingPlace.setTag(dto.getTag());
        parkingPlace.setIsLock(dto.getIsLock());
        return parkingPlace;
    }

    public ParkingPlaceDto mapToDto(ParkingPlace parkingPlace) {
        return ParkingPlaceDto.builder()
                .id(parkingPlace.getId())
                .tag(parkingPlace.getTag())
                .isLock(parkingPlace.getIsLock())
                .build();
    }
}
