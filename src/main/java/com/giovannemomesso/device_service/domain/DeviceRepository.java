package com.giovannemomesso.device_service.domain;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository {

    Device save(Device device);

    Optional<Device> findById(DeviceId id);

}
