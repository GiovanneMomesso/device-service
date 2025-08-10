package com.giovannemomesso.device_service.adapter.postgresql;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceId;
import com.giovannemomesso.device_service.domain.DeviceRepository;
import com.giovannemomesso.device_service.domain.DeviceState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostgresDeviceRepository implements DeviceRepository {

    private final DeviceCrudRepository deviceCrudRepository;

    @Override
    public Device save(final Device device) {
        return deviceCrudRepository.save(device);
    }

    @Override
    public Optional<Device> findById(final DeviceId id) {
        return deviceCrudRepository.findById(id);
    }

    @Override
    public List<Device> findAll(final String brand, final DeviceState deviceState) {

        return deviceCrudRepository.findAllFiltered(brand, deviceState);
    }

    @Override
    public void delete(final DeviceId deviceId) {
        deviceCrudRepository.deleteById(deviceId);
    }
}
