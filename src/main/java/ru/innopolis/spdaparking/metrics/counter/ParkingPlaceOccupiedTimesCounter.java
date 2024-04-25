package ru.innopolis.spdaparking.metrics.counter;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ParkingPlaceOccupiedTimesCounter
{
    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> counterCache = new ConcurrentHashMap<>();

    public void inc(String tag)
    {
        if (counterCache.containsKey(tag))
        {
            counterCache.get(tag).increment();
        }
        else
        {
            Counter counter = Counter.builder("parking_place_occupied_times")
                    .tag("tag", tag)
                    .register(meterRegistry);
            counter.increment();
            counterCache.put(tag, counter);
        }
    }
}
