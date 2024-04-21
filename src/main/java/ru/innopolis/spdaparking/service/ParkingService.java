package ru.innopolis.spdaparking.service;

import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.dto.ApplicationDto;
import ru.innopolis.spdaparking.dto.ParkingPlaceDto;
import ru.innopolis.spdaparking.entity.Application;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.mapper.ApplicationMapper;
import ru.innopolis.spdaparking.mapper.ParkingPlaceMapper;
import ru.innopolis.spdaparking.repository.ApplicationRepository;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final ApplicationRepository applicationRepository;
    private final ParkingPlaceRepository parkingPlaceRepository;
    private final ApplicationMapper applicationMapper;
    private final ParkingPlaceMapper parkingPlaceMapper;

    public ParkingService(ApplicationRepository applicationRepository, ParkingPlaceRepository parkingRepository, ApplicationMapper applicationMapper, ParkingPlaceMapper parkingPlaceMapper) {
        this.applicationRepository = applicationRepository;
        this.parkingPlaceRepository = parkingRepository;
        this.applicationMapper = applicationMapper;
        this.parkingPlaceMapper = parkingPlaceMapper;
    }

    public String takePlace(ApplicationDto applicationDTO) {
        // Получаем парковочное место по его ID
        Optional<ParkingPlace> optionalParkingPlace = parkingPlaceRepository.findById(applicationDTO.getParkingPlaceId());
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
            parkingPlaceRepository.save(parkingPlace);

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

    public String freePlace(Long place_id, String phoneNumber) {
        // Находим заявку по номеру телефона
        Application application = applicationRepository.findAll().stream()
                .filter(app -> Objects.equals(app.getPhoneNumber(), phoneNumber) && app.getDateTo() == null && app.getParkingPlace().getId().equals(place_id)).findFirst().get();

        if (application != null) {
            // Устанавливаем дату окончания заявки на текущее время
            application.setDateTo(new Date());
            applicationRepository.save(application);

            // Находим парковочное место и разблокируем его
            ParkingPlace parkingPlace = parkingPlaceRepository.findById(place_id).orElse(null);
            if (parkingPlace != null) {
                parkingPlace.setIsLock(false);
                parkingPlaceRepository.save(parkingPlace);

                // Вычисляем время использования места в минутах
                long usageTime = (application.getDateTo().getTime() - application.getDateFrom().getTime()) / (1000 * 60);

                int price = 10;
                // Возвращаем сообщение о разблокировке парковочного места
                return String.format("Вы освободили парковочное место %s. Использовано %d минут. \n К оплате %d рублей.",
                        parkingPlace.getTag(), usageTime, price * usageTime);
            } else {
                return "Ошибка: парковочное место не найдено.";
            }
        } else {
            return "Ошибка: заявка не найдена или не соответствует указанному месту.";
        }
    }


    public List<ParkingPlaceDto> getAll(int page_number, String phoneNumber) {
        // Определяем количество элементов на странице и номер первого элемента для пагинации
        int pageSize = 20; // Здесь можно указать желаемый размер страницы
        int firstElementIndex = (page_number - 1) * pageSize;

        List<Application> openApplications = applicationRepository.findAll().stream()
                .filter(app -> app.getDateTo() == null && app.getPhoneNumber().equals(phoneNumber))
                .collect(Collectors.toList());

        // Получаем id парковочных мест из найденных заявок
        List<Long> placesID = openApplications.stream()
                .map(app -> app.getParkingPlace().getId())
                .collect(Collectors.toList());

        // Получаем все парковочные места
        List<ParkingPlace> allParkingPlaces = parkingPlaceRepository.findAll();

        // Фильтруем парковочные места по найденным id
        List<ParkingPlace> filteredParkingPlaces = allParkingPlaces.stream()
                .filter(place -> placesID.contains(place.getId()))
                .collect(Collectors.toList());

        // Сортируем парковочные места по какому-либо критерию (например, по идентификатору)
        filteredParkingPlaces.sort(Comparator.comparing(ParkingPlace::getId));

        // Выполняем пагинацию
        int endIndex = Math.min(firstElementIndex + pageSize, filteredParkingPlaces.size());
        List<ParkingPlace> pageParkingPlaces = filteredParkingPlaces.subList(firstElementIndex, endIndex);

        // Преобразуем парковочные места в DTO
        List<ParkingPlaceDto> collection = pageParkingPlaces.stream()
                .map(parkingPlaceMapper::mapToDto)
                .collect(Collectors.toList());

        return collection;
    }


}
