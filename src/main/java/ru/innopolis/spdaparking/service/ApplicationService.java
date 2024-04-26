package ru.innopolis.spdaparking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.dto.ApplicationDto;
import ru.innopolis.spdaparking.entity.Application;
import ru.innopolis.spdaparking.mapper.ApplicationMapper;
import ru.innopolis.spdaparking.repository.ApplicationRepository;
import ru.innopolis.spdaparking.requests.StatisticsFilter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    public List<ApplicationDto> getAll(StatisticsFilter filter) {
        if(filter.getPhoneNumber() == null)
            throw new IllegalArgumentException("Для получения статистики необходимо указывать номер телефона.");

        List<Application> applications = applicationRepository.findAll();

        return applications.stream()
                .filter(application -> isWithinDateRange(application, filter.getFromDate(), filter.getToDate()))
                .filter(application -> filter.getDriverName() == null || application.getDriverName().toLowerCase().contains(filter.getDriverName().toLowerCase()))
                .filter(application -> filter.getCarDetails() == null || application.getCarDetails().toLowerCase().contains(filter.getCarDetails().toLowerCase()))
                .filter(application -> filter.getPhoneNumber() == null || application.getPhoneNumber().toLowerCase().equals(filter.getPhoneNumber().toLowerCase()))
                .map(applicationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    private boolean isWithinDateRange(Application application, Date fromDate, Date toDate) {
        return (fromDate == null || application.getDateFrom().after(fromDate)) &&
                (toDate == null || application.getDateTo() == null || application.getDateTo().before(toDate));
    }
}