package ru.innopolis.spdaparking.service;

import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.dto.ParkingPlaceDto;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingPlaceService {
    private final ParkingPlaceRepository parkingRepository;

    public ParkingPlaceService(ParkingPlaceRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    public List<ParkingPlaceDto> getAll(){
        List<ParkingPlace> applications = parkingRepository.findAll();

        var collection =  applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return collection;
    }



    private ParkingPlaceDto convertToDTO(ParkingPlace parkingPlace) {
        return ParkingPlaceDto.builder()
                .id(parkingPlace.getId())
                .tag(parkingPlace.getTag())
                .isLock(parkingPlace.getIsLock())
                .build();
    }
}
