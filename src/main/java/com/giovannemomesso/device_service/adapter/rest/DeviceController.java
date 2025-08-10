package com.giovannemomesso.device_service.adapter.rest;

import com.giovannemomesso.device_service.adapter.rest.dto.DeviceRequest;
import com.giovannemomesso.device_service.adapter.rest.dto.DeviceResponse;
import com.giovannemomesso.device_service.application.DeviceService;
import com.giovannemomesso.device_service.domain.DeviceId;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/devices")
@RestController
@AllArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResponse> createNewDevice(@RequestBody @Validated DeviceRequest deviceRequest) {
        var device = deviceRequest.toDomain();

        var newDevice = deviceService.create(device);

        var deviceResponse = DeviceResponse.fromDomain(newDevice);

        return ResponseEntity.ok(deviceResponse);
    }

    @PatchMapping(value = "/{deviceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResponse> patchDevice(@RequestBody DeviceRequest deviceRequest, @PathVariable("deviceId") String id) {
        var device = deviceRequest.toDomain();

        var deviceId = DeviceId.fromString(id);

        var patchedDevice = deviceService.update(device, deviceId);

        var deviceResponse = DeviceResponse.fromDomain(patchedDevice);

        return ResponseEntity.ok(deviceResponse);
    }

}
