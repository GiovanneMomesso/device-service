package com.giovannemomesso.device_service.adapter.rest.dto;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeviceRequest {
    private String name;
    private String brand;
    @Schema(
            description = "Current status of the device",
            example = "available",
            allowableValues = {"available", "inactive", "in-use"}
    )
    private String state;

    public Device toDomain() {
        return Device.builder()
                .name(name)
                .brand(brand)
                .state(DeviceState.fromDescription(state))
                .build();
    }
}
