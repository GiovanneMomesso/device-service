package com.giovannemomesso.device_service.application;

import com.giovannemomesso.device_service.domain.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public Device create(final Device newDevice) {
        // The specification could provide which device state is appropriated in this use case
        return deviceRepository.save(newDevice.createNew());
    }

    public Device update(final Device toBeUpdatedDevice, final DeviceId deviceId) {

        var dbDevice = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));

        if (dbDevice.canUpdate()) {
            var updatedDevice = dbDevice.toBuilder()
                    .name(toBeUpdatedDevice.getName() == null ? dbDevice.getName() : toBeUpdatedDevice.getName())
                    .brand(toBeUpdatedDevice.getBrand() == null ? dbDevice.getBrand() : toBeUpdatedDevice.getBrand())
                    .state(toBeUpdatedDevice.getState() == null ? dbDevice.getState() : toBeUpdatedDevice.getState())
                    .build();
            return deviceRepository.save(updatedDevice);
        }
        throw new UpdateDeviceException(deviceId);
    }
}
