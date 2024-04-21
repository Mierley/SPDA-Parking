package ru.innopolis.spdaparking.service;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.dto.ApplicationDto;
import ru.innopolis.spdaparking.entity.Application;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.mapper.ApplicationMapper;
import ru.innopolis.spdaparking.repository.ApplicationRepository;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

@Service
public class ParkingService {

    private final ApplicationRepository applicationRepository;
    private final ParkingPlaceRepository parkingRepository;
    private final ApplicationMapper applicationMapper;

    public ParkingService(ApplicationRepository applicationRepository, ParkingPlaceRepository parkingRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.parkingRepository = parkingRepository;
        this.applicationMapper = applicationMapper;
    }

    public String takePlace(ApplicationDto applicationDTO) {
        // Получаем парковочное место по его ID
        Optional<ParkingPlace> optionalParkingPlace = parkingRepository.findById(applicationDTO.getParkingPlaceId());
        var applications = applicationRepository.findAll().stream()
                .filter(app -> Objects.equals(app.getPhoneNumber(), applicationDTO.getPhoneNumber()) && app.getDateTo() == null).toList();

        if (applications.size() > 3) {
            //todo нельзя занять больше 3х мест за раз
            return "Нельзя занять больше 3х мест но номер телефона" + applicationDTO.getPhoneNumber() + " за раз.";
        }

        // Если парковочное место найдено и не занято
        if (optionalParkingPlace.isPresent() && !optionalParkingPlace.get().getIsLock()) {
            ParkingPlace parkingPlace = optionalParkingPlace.get();

            // Блокируем парковочное место и сохраняем изменения
            parkingPlace.setIsLock(true);
            parkingRepository.save(parkingPlace);

            // Создаём заявку с заблокированным парковочным местом
            var application = applicationMapper.mapToEntity(applicationDTO);
            application.setDateFrom(new Date());
            applicationRepository.save(application);

            // Возвращаем строковое представление заблокированного парковочного места
            return "Парковочное место " + parkingPlace.getTag() + " успешно забронировано.";
        } else {
            // Возвращаем сообщение об ошибке, если парковочное место не найдено
            return "Парковочное место не найдено или занято.";
        }
    }
}
