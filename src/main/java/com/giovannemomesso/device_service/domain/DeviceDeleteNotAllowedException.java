package com.giovannemomesso.device_service.domain;

public class DeviceDeleteNotAllowedException extends RuntimeException {
    public DeviceDeleteNotAllowedException(final DeviceId deviceId) {
        super("In use devices can not be deleted. Device Id: " + deviceId.getId());
    }
}
