package com.giovannemomesso.device_service.adapter.rest;

import com.giovannemomesso.device_service.adapter.rest.dto.DeviceRequest;
import com.giovannemomesso.device_service.adapter.rest.dto.DeviceResponse;
import com.giovannemomesso.device_service.application.DeviceService;
import com.giovannemomesso.device_service.domain.DeviceId;
import com.giovannemomesso.device_service.domain.DeviceState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Devices", description = "Operations related to device management")
@RequestMapping("/devices")
@RestController
@AllArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Create a new device", description = "Creates a new device in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device successfully created",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResponse> createNewDevice(
            @RequestBody @Validated DeviceRequest deviceRequest) {
        var device = deviceRequest.toDomain();
        var newDevice = deviceService.create(device);
        return ResponseEntity.ok(DeviceResponse.fromDomain(newDevice));
    }

    @Operation(summary = "Update part of an existing device", description = "Updates only the provided fields of an existing device.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device successfully updated",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @PatchMapping(value = "/{deviceId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResponse> patchDevice(
            @RequestBody DeviceRequest deviceRequest,
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Device ID (UUID format)",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid", example = "123e4567-e89b-12d3-a456-426614174000")
            )
            @PathVariable("deviceId") String id) {
        var patchedDevice = deviceService.update(deviceRequest.toDomain(), DeviceId.fromString(id));
        return ResponseEntity.ok(DeviceResponse.fromDomain(patchedDevice));
    }

    @Operation(summary = "Get a device by ID", description = "Retrieves a single device by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @GetMapping(value = "/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeviceResponse> getDeviceById(
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Device ID (UUID format)",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid", example = "123e4567-e89b-12d3-a456-426614174000")
            )
            @PathVariable("deviceId") String id) {
        var device = deviceService.getById(DeviceId.fromString(id));
        return ResponseEntity.ok(DeviceResponse.fromDomain(device));
    }

    @Operation(summary = "List all devices", description = "Retrieves all devices, optionally filtered by brand and state.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devices list retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DeviceResponse.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeviceResponse>> getAllDevices(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String state) {
        var deviceState = DeviceState.fromDescription(state);
        var devices = deviceService.getAll(brand, deviceState);
        return ResponseEntity.ok(devices.stream().map(DeviceResponse::fromDomain).toList());
    }

    @Operation(summary = "Delete a device", description = "Removes a device from the system by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Device successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Device not found")
    })
    @DeleteMapping(value = "/{deviceId}")
    public ResponseEntity<Void> deleteDevice(
            @Parameter(
                    in = ParameterIn.PATH,
                    description = "Device ID (UUID format)",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid", example = "123e4567-e89b-12d3-a456-426614174000")
            )
            @PathVariable("deviceId") String id) {
        deviceService.deleteDevice(DeviceId.fromString(id));
        return ResponseEntity.ok().build();
    }
}
