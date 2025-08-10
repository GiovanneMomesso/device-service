package com.giovannemomesso.device_service.application;

import com.giovannemomesso.device_service.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;


    @Test
    void createNewDevice_givenDevice_shouldReturnNewDevice() {
        var creationTime = LocalDateTime.now();
        var deviceId = DeviceId.createNew();
        var newDevice = Device.builder().name("device 1").brand("brand 1").state(DeviceState.AVAILABLE).build();
        var savedDevice = Device.builder().id(deviceId).name("device 1").brand("brand 1").state(DeviceState.AVAILABLE).createdTime(creationTime).build();

        when(deviceRepository.save(newDevice)).thenReturn(savedDevice);

        var response = deviceService.create(newDevice);

        assertThat(response).isEqualTo(savedDevice);
    }

    @Test
    void updateDevice_givenValidDevice_shouldReturnUpdatedDevice() {
        var deviceId = DeviceId.createNew();
        var createdTime = LocalDateTime.now();
        var toBeUpdatedDevice = Device.builder()
                .state(DeviceState.INACTIVE)
                .name("device 1")
                .brand("brand 1")
                .build();


        var dbDevice = Device.builder()
                .state(DeviceState.AVAILABLE)
                .name("device")
                .brand("brand")
                .id(deviceId)
                .createdTime(createdTime)
                .build();

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(dbDevice));
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var updatedDevice = deviceService.update(toBeUpdatedDevice, deviceId);

        assertThat(updatedDevice)
                .satisfies(ud -> {
                    assertThat(ud.getName()).hasToString("device 1");
                    assertThat(ud.getBrand()).hasToString("brand 1");
                    assertThat(ud.getState()).isEqualTo(DeviceState.INACTIVE);
                    assertThat(ud.getId()).isEqualTo(deviceId);
                    assertThat(ud.getCreatedTime()).isEqualTo(createdTime);
                });
    }

    @Test
    void updateDevice_givenValidDeviceWithOnlyState_shouldReturnUpdatedOnlyDeviceState() {
        var deviceId = DeviceId.createNew();
        var createdTime = LocalDateTime.now();
        var toBeUpdatedDevice = Device.builder()
                .state(DeviceState.INACTIVE)
                .build();


        var dbDevice = Device.builder()
                .state(DeviceState.AVAILABLE)
                .name("device")
                .brand("brand")
                .id(deviceId)
                .createdTime(createdTime)
                .build();

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(dbDevice));
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var updatedDevice = deviceService.update(toBeUpdatedDevice, deviceId);

        assertThat(updatedDevice)
                .satisfies(ud -> {
                    assertThat(ud.getName()).hasToString("device");
                    assertThat(ud.getBrand()).hasToString("brand");
                    assertThat(ud.getState()).isEqualTo(DeviceState.INACTIVE);
                    assertThat(ud.getId()).isEqualTo(deviceId);
                    assertThat(ud.getCreatedTime()).isEqualTo(createdTime);
                });
    }

    @Test
    void updateDevice_givenValidDeviceWithOnlyName_shouldReturnUpdatedOnlyDeviceName() {
        var deviceId = DeviceId.createNew();
        var createdTime = LocalDateTime.now();
        var toBeUpdatedDevice = Device.builder()
                .name("new device name")
                .build();


        var dbDevice = Device.builder()
                .state(DeviceState.AVAILABLE)
                .name("device")
                .brand("brand")
                .id(deviceId)
                .createdTime(createdTime)
                .build();

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(dbDevice));
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var updatedDevice = deviceService.update(toBeUpdatedDevice, deviceId);

        assertThat(updatedDevice)
                .satisfies(ud -> {
                    assertThat(ud.getName()).hasToString("new device name");
                    assertThat(ud.getBrand()).hasToString("brand");
                    assertThat(ud.getState()).isEqualTo(DeviceState.AVAILABLE);
                    assertThat(ud.getId()).isEqualTo(deviceId);
                    assertThat(ud.getCreatedTime()).isEqualTo(createdTime);
                });
    }

    @Test
    void updateDevice_givenNotFoundDevice_shouldThrowDeviceNotFoundException() {
        var deviceId = DeviceId.createNew();
        var toBeUpdatedDevice = Device.builder()
                .name("new device name")
                .build();

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.update(toBeUpdatedDevice, deviceId))
                .isInstanceOf(DeviceNotFoundException.class)
                .hasMessage("Device not found for id: " + toBeUpdatedDevice.getId().getId());
    }

    @Test
    void updateDevice_givenNotInvalidStateDevice_shouldThrowUpdateDeviceException() {
        var deviceId = DeviceId.createNew();
        var createdTime = LocalDateTime.now();
        var toBeUpdatedDevice = Device.builder()
                .name("new device name")
                .state(DeviceState.AVAILABLE)
                .build();

        var dbDevice = Device.builder()
                .state(DeviceState.IN_USE)
                .name("device")
                .brand("brand")
                .id(deviceId)
                .createdTime(createdTime)
                .build();

        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(dbDevice));

        assertThatThrownBy(() -> deviceService.update(toBeUpdatedDevice, deviceId))
                .isInstanceOf(UpdateDeviceException.class)
                .hasMessage("Update of in-use devices are not allowed. Device Id: " + toBeUpdatedDevice.getId().getId());
    }

}
