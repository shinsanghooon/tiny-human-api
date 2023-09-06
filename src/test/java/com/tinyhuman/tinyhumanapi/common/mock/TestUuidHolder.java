package com.tinyhuman.tinyhumanapi.common.mock;

import com.tinyhuman.tinyhumanapi.common.service.port.UuidHolder;

public class TestUuidHolder implements UuidHolder {

    private final String uuid;

    public TestUuidHolder(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String random() {
        return this.uuid;
    }
}
