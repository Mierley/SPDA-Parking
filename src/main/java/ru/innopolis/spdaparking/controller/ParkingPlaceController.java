package ru.innopolis.spdaparking.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.spdaparking.dto.ApplicationDto;
import ru.innopolis.spdaparking.dto.ParkingPlaceDto;
import ru.innopolis.spdaparking.service.ParkingPlaceService;
import ru.innopolis.spdaparking.service.ParkingService;

import java.util.Date;
import java.util.List;

@Component
@RestController
@RequestMapping("/parking")
@Api(tags = "Контроллер примера")
public class ParkingPlaceController {

    private final ParkingPlaceService parkingPlaceService;
    private final ParkingService parkingService;

    @Autowired
    public ParkingPlaceController(ParkingPlaceService parkingPlaceService, ParkingService parkingService) {
        this.parkingPlaceService = parkingPlaceService;
        this.parkingService = parkingService;
    }

    @ApiOperation("Получение списка парковочных мест")
    @GetMapping("places/{page_number}")
    public List<ParkingPlaceDto> getParkingPlaces(@PathVariable("page_number") int page_number) {
        return parkingPlaceService.getAll(page_number);
    }

    @ApiOperation("Получение списка парковочных мест по номеру телефона")
    @PostMapping("places/{page_number}")
    public List<ParkingPlaceDto> getParkingPlacesByPhoneNumber(@PathVariable("page_number") int page_number, @RequestBody String phoneNumber) {
        return parkingService.getAll(page_number, phoneNumber);
    }

    @ApiOperation("Занять парковочное место по телефону")
    @PostMapping("take-place")
    public String takePlace(@RequestBody ApplicationDto applicationDto) {
        return parkingService.takePlace(applicationDto);
    }

    @ApiOperation("Освободить парковочное место по телефону")
    @PostMapping("free-place/{place_id}")
    public String freePlace(@PathVariable("place_id") Long place_id, @RequestBody String phoneNumber) {
        return parkingService.freePlace(place_id, phoneNumber);
    }
}