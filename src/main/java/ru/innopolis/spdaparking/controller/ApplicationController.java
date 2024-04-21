package ru.innopolis.spdaparking.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.innopolis.spdaparking.dto.ApplicationDto;
import ru.innopolis.spdaparking.requests.StatisticsFilter;
import ru.innopolis.spdaparking.requests.TakePlace;
import ru.innopolis.spdaparking.service.ApplicationService;

import java.util.Date;
import java.util.List;

@Component
@RestController
@RequestMapping("/application")
@Api(tags = "Контроллер примера")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @ApiOperation("Пример метода")
    @PostMapping("statistics")
    public List<ApplicationDto> getApplicationStatistics(@RequestBody StatisticsFilter filter) {
        return applicationService.getAll(filter);
    }

    @ApiOperation("Занять место водителем")
    @PostMapping("take-place")
    public void takePlace(@RequestBody ApplicationDto applicationDto) {

        applicationService.createApplication(applicationDto);
    }
}