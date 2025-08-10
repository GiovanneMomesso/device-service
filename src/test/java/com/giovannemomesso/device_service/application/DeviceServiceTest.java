package com.giovannemomesso.device_service.application;

import com.giovannemomesso.device_service.domain.Device;
import com.giovannemomesso.device_service.domain.DeviceId;
import com.giovannemomesso.device_service.domain.DeviceRepository;
import com.giovannemomesso.device_service.domain.DeviceState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;


    @Test
    void createNewDevice_givenDevice_shouldReturnNewDevice() {
        var creationTime = Instant.now();
        var deviceId = DeviceId.createNew();
        var newDevice = Device.builder().name("device 1").brand("brand 1").state(DeviceState.AVAILABLE).build();
        var savedDevice = Device.builder().id(deviceId).name("device 1").brand("brand 1").state(DeviceState.AVAILABLE).createdTime(creationTime).build();

        when(deviceRepository.save(newDevice)).thenReturn(savedDevice);

        var response = deviceService.create(newDevice);

        assertThat(response).isEqualTo(savedDevice);
    }

}
