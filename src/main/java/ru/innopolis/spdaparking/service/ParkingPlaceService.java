package ru.innopolis.spdaparking.service;

import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.dto.PaginationResponse;
import ru.innopolis.spdaparking.dto.ParkingPlaceDto;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.mapper.ParkingPlaceMapper;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingPlaceService {
    private final ParkingPlaceRepository parkingRepository;
    private final ParkingPlaceMapper parkingPlaceMapper;

    public ParkingPlaceService(ParkingPlaceRepository parkingRepository, ParkingPlaceMapper parkingPlaceMapper) {
        this.parkingRepository = parkingRepository;
        this.parkingPlaceMapper = parkingPlaceMapper;
    }

    public PaginationResponse<ParkingPlaceDto> getAll(int pageNumber) {
        int pageSize = 20; // Размер страницы

        List<ParkingPlace> parkingPlaces = parkingRepository.findAll();

        // Вычисляем индекс первого элемента на странице
        int startIndex = (pageNumber - 1) * pageSize;

        // Вычисляем индекс последнего элемента на странице
        int endIndex = Math.min(startIndex + pageSize, parkingPlaces.size());

        // Получаем подсписок элементов, соответствующих запрошенной странице
        List<ParkingPlace> pageParkingPlaces = parkingPlaces.stream()
                .sorted(Comparator.comparing(ParkingPlace::getId))
                .toList()
                .subList(startIndex, endIndex);

        // Преобразуем элементы подсписка в DTO
        List<ParkingPlaceDto> collection = pageParkingPlaces.stream()
                .map(parkingPlaceMapper::mapToDto)
                .collect(Collectors.toList());

        return PaginationResponse.of(collection, parkingPlaces.size(), pageNumber, pageSize, parkingPlaces.stream().filter(p -> !p.getIsLock()).toList().size());
    }
}
