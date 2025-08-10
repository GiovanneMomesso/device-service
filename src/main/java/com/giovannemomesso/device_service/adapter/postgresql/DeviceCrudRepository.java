package com.giovannemomesso.device_service.adapter.postgresql;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceCrudRepository extends ListCrudRepository<Device, DeviceId> {
}
