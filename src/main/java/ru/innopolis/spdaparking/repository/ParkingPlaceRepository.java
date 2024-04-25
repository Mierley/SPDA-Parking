package ru.innopolis.spdaparking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.entity.ParkingPlaceCountByTag;

import java.util.List;

public interface ParkingPlaceRepository extends JpaRepository<ParkingPlace, Long> 
{
    @Query("SELECT new ru.innopolis.spdaparking.entity.ParkingPlaceCountByTag(pp.tag, COUNT(pp.id)) FROM Application a LEFT JOIN a.parkingPlace pp GROUP BY pp.tag")
    List<ParkingPlaceCountByTag> countByParkingPlaceTag();
}

