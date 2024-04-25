package ru.innopolis.spdaparking.metrics.gauge;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.entity.ParkingPlace;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;

@Service
public class FreeParkingPlacesGauge
{
    private final MeterRegistry meterRegistry;
    private final ParkingPlaceRepository parkingRepository;
    private final Gauge gauge;

    public FreeParkingPlacesGauge(MeterRegistry meterRegistry, ParkingPlaceRepository parkingRepository)
    {
        this.meterRegistry = meterRegistry;
        this.parkingRepository = parkingRepository;
        gauge = Gauge.builder("free_parking_places", () -> parkingRepository.count(
                        Example.of(ParkingPlace.builder().isLock(false).build())))
                .register(meterRegistry);
    }
}
