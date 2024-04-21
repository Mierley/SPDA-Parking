package ru.innopolis.spdaparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.innopolis.spdaparking.entity.ParkingPlace;

public interface ParkingPlaceRepository extends JpaRepository<ParkingPlace, Long> {};