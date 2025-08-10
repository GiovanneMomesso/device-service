package com.giovannemomesso.device_service.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UpdateDeviceException extends RuntimeException {
    public UpdateDeviceException(final DeviceId deviceId) {
        super("Update of in-use devices are not allowed. Device Id: " + deviceId.getId());
    }
}
