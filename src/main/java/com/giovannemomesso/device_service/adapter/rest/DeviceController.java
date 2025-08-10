package com.giovannemomesso.device_service.adapter.rest;

import com.giovannemomesso.device_service.adapter.dto.CreateDeviceRequest;
import com.giovannemomesso.device_service.adapter.dto.DeviceResponse;
import com.giovannemomesso.device_service.application.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/devices")
@RestController
@AllArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResponse> createNewDevice(@RequestBody @Validated CreateDeviceRequest createDeviceRequest) {
        var device = createDeviceRequest.toDomain();

        var newDevice = deviceService.create(device);

        var deviceResponse = DeviceResponse.fromDomain(newDevice);

        return ResponseEntity.ok(deviceResponse);
    }

}
