package com.giovannemomesso.device_service.adapter.postgresql;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceId;
import com.giovannemomesso.device_service.domain.DeviceState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceCrudRepository extends ListCrudRepository<Device, DeviceId> {

    @Query(value = """
            SELECT d FROM Device d
            WHERE (:brand IS NULL OR brand = :brand)
            AND (:state IS NULL OR state = :state)
            """)
    List<Device> findAllFiltered(@Param("brand") String brand, @Param("state") DeviceState state);

}
