package ru.innopolis.spdaparking.mapper;

import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.innopolis.spdaparking.dto.ApplicationDto;
import ru.innopolis.spdaparking.entity.Application;
import ru.innopolis.spdaparking.entity.ParkingPlace;

@Component
public class ApplicationMapper {

    public Application mapToEntity(ApplicationDto dto) {
        Application application = new Application();
        application.setId(dto.getId());
        application.setDriverName(dto.getDriverName());
        application.setCarDetails(dto.getCarDetails());
        application.setPhoneNumber(dto.getPhoneNumber());
        application.setDateFrom(dto.getDateFrom());
        application.setDateTo(dto.getDateTo());
        // Дополнительно установить место парковки, если оно доступно в DTO
        if (dto.getParkingPlaceId() != null) {
            ParkingPlace parkingPlace = new ParkingPlace();
            parkingPlace.setId(dto.getParkingPlaceId());
            application.setParkingPlace(parkingPlace);
        }
        return application;
    }

    public ApplicationDto mapToDto(Application application) {
        return ApplicationDto.builder()
                .id(application.getId())
                .driverName(application.getDriverName())
                .carDetails(application.getCarDetails())
                .phoneNumber(application.getPhoneNumber())
                .dateFrom(application.getDateFrom())
                .dateTo(application.getDateTo())
                .parkingPlaceId(application.getParkingPlace() != null ? application.getParkingPlace().getId() : null)
                .build();
    }
}
