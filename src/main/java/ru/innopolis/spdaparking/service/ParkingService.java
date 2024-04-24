package ru.innopolis.spdaparking.service;

import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.dto.ApplicationDto;
import ru.innopolis.spdaparking.dto.PaginationResponse;
import ru.innopolis.spdaparking.dto.ParkingPlaceDto;
import ru.innopolis.spdaparking.entity.Application;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.mapper.ApplicationMapper;
import ru.innopolis.spdaparking.mapper.ParkingPlaceMapper;
import ru.innopolis.spdaparking.repository.ApplicationRepository;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;
import ru.innopolis.spdaparking.responces.Response;

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

    public Response takePlace(ApplicationDto applicationDTO) {
        // Получаем парковочное место по его ID
        Optional<ParkingPlace> optionalParkingPlace = parkingPlaceRepository.findById(applicationDTO.getParkingPlaceId());
        var applications = applicationRepository.findAll().stream()
                .filter(app -> Objects.equals(app.getPhoneNumber(), applicationDTO.getPhoneNumber()) && app.getDateTo() == null).toList();

        if (applications.size() >= 3) {
            //todo нельзя занять больше 3х мест за раз
            return new Response("Нельзя занять больше 3х мест но номер телефона" + applicationDTO.getPhoneNumber() + " за раз.", 403);
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
            return new Response( "Парковочное место " + parkingPlace.getTag() + " успешно забронировано.", 200);
        } else {
            // Возвращаем сообщение об ошибке, если парковочное место не найдено
            return new Response( "Парковочное место не найдено или занято.", 404);
        }
    }

    public Response freePlace(Long place_id, String phoneNumber) {
        // Находим заявку по номеру телефона
        Application application = applicationRepository.findAll().stream()
                .filter(app -> Objects.equals(app.getPhoneNumber(), phoneNumber) && app.getDateTo() == null && app.getParkingPlace().getId().equals(place_id)).findFirst().orElse(null);

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

                int price = 1;
                // Возвращаем сообщение о разблокировке парковочного места
                return new Response(String.format("Вы освободили парковочное место %s. Использовано %d минут. \n К оплате %d рублей.",
                        parkingPlace.getTag(), usageTime, price * usageTime), 200);
            } else {
                return new Response("Ошибка: заявка не найдена или не соответствует указанному месту.", 404);
            }
        } else {
            return new Response("Ошибка: заявка не найдена или не соответствует указанному месту.", 404);
        }
    }


    public PaginationResponse<ParkingPlaceDto> getAll(int pageNumber, String phoneNumber) {
        // Определяем количество элементов на странице и номер первого элемента для пагинации
        int pageSize = 20; // Здесь можно указать желаемый размер страницы
        int firstElementIndex = (pageNumber - 1) * pageSize;

        List<Application> openApplications = applicationRepository.findAll().stream()
                .filter(app -> app.getDateTo() == null && app.getPhoneNumber().equals(phoneNumber))
                .collect(Collectors.toList());

        List<ParkingPlace> allParkingPlaces = new ArrayList<>(parkingPlaceRepository.findAll());;


        // Сортируем парковочные места по какому-либо критерию (например, по идентификатору)
        allParkingPlaces.sort(Comparator.comparing(ParkingPlace::getId));

        // Выполняем пагинацию
        int endIndex = Math.min(firstElementIndex + pageSize, allParkingPlaces.size());
        List<ParkingPlace> pageParkingPlaces = allParkingPlaces.subList(firstElementIndex, endIndex);

        // Преобразуем парковочные места в DTO
        List<ParkingPlaceDto> collection = pageParkingPlaces.stream()
                .map(parkingPlace -> {
                    ParkingPlaceDto dto = parkingPlaceMapper.mapToDto(parkingPlace);
                    // Находим соответствующую заявку в списке applications
                    Optional<Application> matchingApplication = openApplications.stream()
                            .filter(app -> Objects.equals(app.getParkingPlace().getId(), parkingPlace.getId()))
                            .findFirst();
                    // Если заявка найдена, устанавливаем номер телефона
                    matchingApplication.ifPresent(app -> dto.setByUser(true));
                    return dto;
                })
                .collect(Collectors.toList());

        return PaginationResponse.of(collection,allParkingPlaces.size(), pageNumber, pageSize, allParkingPlaces.stream().filter(p -> !p.getIsLock()).toList().size());
    }


}
