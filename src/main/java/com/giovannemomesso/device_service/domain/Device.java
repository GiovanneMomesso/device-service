package com.giovannemomesso.device_service.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Device {

    @EmbeddedId
    private DeviceId id;

    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    private DeviceState state;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdTime;

    public Boolean isInUse() {
        return DeviceState.IN_USE.equals(state);
    }

    public Device createNew() {
        return Device.builder()
                .id(DeviceId.createNew())
                .name(name)
                .brand(brand)
                .state(state)
                .build();
    }

}
