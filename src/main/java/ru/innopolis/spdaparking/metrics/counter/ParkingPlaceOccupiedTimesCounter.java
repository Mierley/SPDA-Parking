package ru.innopolis.spdaparking.metrics.counter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import ru.innopolis.spdaparking.repository.ParkingPlaceRepository;
import ru.innopolis.spdaparking.entity.ParkingPlaceCountByTag;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ParkingPlaceOccupiedTimesCounter
{
    private static final String counterName = "parking_place_occupied_times";
    private static final String tagName = "tag";
    
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    public ParkingPlaceOccupiedTimesCounter(MeterRegistry meterRegistry, ParkingPlaceRepository parkingPlaceRepository)
    {
        this.meterRegistry = meterRegistry;
        parkingPlaceRepository.countByParkingPlaceTag().forEach(this::setStartValue);
    }

    public void inc(String tag)
    {
        if (counterCache.containsKey(tag))
        {
            counterCache.get(tag).increment();
        }
        else
        {
            Counter counter = Counter.builder(counterName)
                    .tag(tagName, tag)
                    .register(meterRegistry);
            counter.increment();
            counterCache.put(tag, counter);
        }
    }

    private void setStartValue(ParkingPlaceCountByTag parkingPlaceCountByTag)
    {
        Counter counter = Counter.builder(counterName)
                .tag(tagName, parkingPlaceCountByTag.getTag())
                .register(meterRegistry);
        counter.increment(parkingPlaceCountByTag.getCount());
        counterCache.put(parkingPlaceCountByTag.getTag(), counter);
    }
}
