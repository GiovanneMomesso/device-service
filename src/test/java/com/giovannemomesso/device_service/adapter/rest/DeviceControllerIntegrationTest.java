package com.giovannemomesso.device_service.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannemomesso.device_service.adapter.dto.CreateDeviceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeviceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createDevice_givenValidInput_shouldReturnNewlyCreatedDevice() throws Exception {
        var createDeviceRequest = CreateDeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state("available")
                .build();

        mockMvc.perform(post("/devices")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("device 1"))
                .andExpect(jsonPath("$.brand").value("brand 1"))
                .andExpect(jsonPath("$.state").value("available"))
                .andExpect(jsonPath("$.createdTime").exists());

    }

}
