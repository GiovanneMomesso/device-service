package com.giovannemomesso.device_service.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceId {
    private UUID id;

    public static DeviceId createNew() {return new DeviceId(UUID.randomUUID());}

    public static DeviceId fromString(String id) {
        return new DeviceId(UUID.fromString(id));
    }
}
