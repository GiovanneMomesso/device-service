package com.giovannemomesso.device_service.adapter.dto;

import com.giovannemomesso.device_service.domain.Device;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Builder
@Getter
public class DeviceResponse {

    private String id;
    private String name;
    private String brand;
    private String state;
    private LocalDateTime createdTime;

    public static DeviceResponse fromDomain(final Device device) {
        return DeviceResponse.builder()
                .id(device.getId().getId().toString())
                .name(device.getName())
                .brand(device.getBrand())
                .state(device.getState().getDescription())
                .createdTime(device.getCreatedTime())
                .build();
    }

}
