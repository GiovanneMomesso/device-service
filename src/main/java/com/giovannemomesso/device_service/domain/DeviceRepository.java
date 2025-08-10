package com.giovannemomesso.device_service.domain;

import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository {

    Device save(Device device);

}
