package com.giovannemomesso.device_service.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Builder(toBuilder = true)
@Getter
public class Device {

    @EmbeddedId
    private DeviceId id;

    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    private DeviceState state;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdTime;

    public Boolean canUpdate() {
        return !DeviceState.IN_USE.equals(state);
    }

}
