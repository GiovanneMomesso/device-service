package com.giovannemomesso.device_service.application;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceNotFoundException;
import com.giovannemomesso.device_service.domain.DeviceRepository;
import com.giovannemomesso.device_service.domain.UpdateDeviceException;
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

    public Device update(final Device toBeUpdatedDevice) {

        var dbDevice = deviceRepository.findById(toBeUpdatedDevice.getId())
                .orElseThrow(() -> new DeviceNotFoundException(toBeUpdatedDevice.getId()));

        if (dbDevice.canUpdate()) {
            var updatedDevice = dbDevice.toBuilder()
                    .name(toBeUpdatedDevice.getName() == null ? dbDevice.getName() : toBeUpdatedDevice.getName())
                    .brand(toBeUpdatedDevice.getBrand() == null ? dbDevice.getBrand() : toBeUpdatedDevice.getBrand())
                    .state(toBeUpdatedDevice.getState() == null ? dbDevice.getState() : toBeUpdatedDevice.getState())
                    .build();
            return deviceRepository.save(updatedDevice);
        }
        throw new UpdateDeviceException(toBeUpdatedDevice);
    }
}
