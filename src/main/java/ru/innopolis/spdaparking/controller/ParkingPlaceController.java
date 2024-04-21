package ru.innopolis.spdaparking.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("places")
    public List<ParkingPlaceDto> getApplicationStatistics() {
        return parkingPlaceService.getAll();
    }

    @ApiOperation("Подтвреждение номер")
    @PostMapping("take-place")
    public String takePlace(@RequestBody String phoneNumber) {
        return parkingService. confirmPlaceByPhoneNumber(phoneNumber);
    }
}