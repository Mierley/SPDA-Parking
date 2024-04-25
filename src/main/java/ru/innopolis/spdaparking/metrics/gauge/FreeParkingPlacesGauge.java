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
    private static final String gaugeName = "free_parking_places";

    private final Gauge gauge;

    public FreeParkingPlacesGauge(MeterRegistry meterRegistry, ParkingPlaceRepository parkingRepository)
    {
        gauge = Gauge.builder(gaugeName, () -> parkingRepository.count(
                        Example.of(ParkingPlace.builder().isLock(false).build())))
                .register(meterRegistry);
    }
}
