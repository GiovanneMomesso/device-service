package com.giovannemomesso.device_service.domain;

import jakarta.persistence.*;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Builder(toBuilder = true)
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

}
