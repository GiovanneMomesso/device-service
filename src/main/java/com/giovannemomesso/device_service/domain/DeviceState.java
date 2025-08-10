package com.giovannemomesso.device_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@AllArgsConstructor
public enum DeviceState {
    AVAILABLE("available"),
    IN_USE("in-use"),
    INACTIVE("inactive");

    private final String description;

    public static DeviceState fromDescription(String description) {
        if (!StringUtils.hasText(description)) {
            throw new IllegalArgumentException("Empty device state");
        }
        for (var deviceState : values()) {
            if (deviceState.getDescription().equalsIgnoreCase(description)) {
                return deviceState;
            }
        }
        throw new IllegalArgumentException("Unknown device state: " + description);
    }
}
