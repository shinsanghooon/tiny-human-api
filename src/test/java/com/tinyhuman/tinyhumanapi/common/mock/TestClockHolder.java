package com.tinyhuman.tinyhumanapi.common.mock;

import com.tinyhuman.tinyhumanapi.common.service.port.ClockHolder;

public class TestClockHolder implements ClockHolder {

    private final long millis;

    public TestClockHolder(long millis) {
        this.millis = millis;
    }

    @Override
    public long epochMilli() {
        return this.millis;
    }
}

