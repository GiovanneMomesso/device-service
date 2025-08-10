package com.giovannemomesso.device_service.adapter.dto;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateDeviceRequest {
    private String name;
    private String brand;
    private String state;

    public Device toDomain() {
        return Device.builder()
                .name(name)
                .brand(brand)
                .state(DeviceState.fromDescription(state))
                .build();
    }
}
