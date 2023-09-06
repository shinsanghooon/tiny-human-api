package com.tinyhuman.tinyhumanapi.common.infrastructure;

import com.tinyhuman.tinyhumanapi.common.service.port.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHolder implements UuidHolder {
    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
