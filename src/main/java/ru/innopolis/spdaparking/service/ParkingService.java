package ru.innopolis.spdaparking.service;

import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.entity.Application;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.repository.ApplicationRepository;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;

import java.util.List;

@Service
public class ParkingService {

    private final ApplicationRepository applicationRepository;
    private final ParkingPlaceRepository parkingRepository;

    public ParkingService(ApplicationRepository applicationRepository, ParkingPlaceRepository parkingRepository) {
        this.applicationRepository = applicationRepository;
        this.parkingRepository = parkingRepository;
    }

    public String confirmPlaceByPhoneNumber(String phoneNumber) {
        // Получаем список всех заявок
        List<Application> applicationsList = applicationRepository.findAll();

        // Ищем заявку по номеру телефона
        Application application = applicationsList.stream()
                .filter(app -> app.getPhoneNumber().toLowerCase().contains(phoneNumber.toLowerCase()))
                .findFirst()
                .orElse(null);

        // Если заявка найдена
        if (application != null) {
            // Получаем парковочное место из заявки
            ParkingPlace parkingPlace = application.getParkingPlace();

            // Если парковочное место найдено
            if (parkingPlace != null) {
                // Блокируем его и сохраняем изменения
                parkingPlace.setIsLock(true);
                parkingRepository.save(parkingPlace);

                // Возвращаем строковое представление парковочного места
                return "Parking place " + parkingPlace.getTag() + " has been successfully locked.";
            }
        }

        // Возвращаем сообщение об ошибке, если заявка не найдена или у неё нет парковочного места
        return "Application not found or parking place not assigned.";
    }
}
