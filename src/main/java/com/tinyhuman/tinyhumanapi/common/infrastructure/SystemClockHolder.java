package com.tinyhuman.tinyhumanapi.common.infrastructure;

import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

@Component
public class SystemClockHolder implements ClockHolder {

    @Override
    public long epochMilli() {
        Clock utcClock = Clock.systemUTC();
        Instant currentUtcInstant = Instant.now(utcClock);
        return currentUtcInstant.toEpochMilli();
    }
}
