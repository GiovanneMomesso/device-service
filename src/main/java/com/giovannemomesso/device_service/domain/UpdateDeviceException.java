package com.giovannemomesso.device_service.domain;

public class UpdateDeviceException extends RuntimeException {
    public UpdateDeviceException(final Device toBeUpdatedDevice) {
        super("Update of in-use devices are not allowed. Device Id: " + toBeUpdatedDevice.getId().getId());
    }
}
