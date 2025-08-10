package com.giovannemomesso.device_service.application;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceRepository;
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
        return deviceRepository.save(newDevice);
    }

}
