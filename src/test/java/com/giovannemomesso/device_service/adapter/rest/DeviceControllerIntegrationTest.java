package com.giovannemomesso.device_service.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannemomesso.device_service.adapter.rest.dto.DeviceRequest;
import com.giovannemomesso.device_service.adapter.rest.dto.DeviceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
        var createDeviceRequest = DeviceRequest.builder()
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

    @Test
    void patchDevice_givenValidInput_shouldPatchDeviceAndReturn() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state("available")
                .build();

        var createStringResponse = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse = objectMapper.readValue(createStringResponse, DeviceResponse.class);

        var patchRequest = DeviceRequest.builder()
                .name("device 2")
                .brand("brand new")
                .state("in-use")
                .build();

        mockMvc.perform(patch("/devices/" + createResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createResponse.getId()))
                .andExpect(jsonPath("$.name").value("device 2"))
                .andExpect(jsonPath("$.brand").value("brand new"))
                .andExpect(jsonPath("$.state").value("in-use"))
                .andExpect(jsonPath("$.createdTime").exists());

    }

    @Test
    void patchDevice_givenInvalidState_shouldReturnBadRequest() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state("in-use")
                .build();

        var createStringResponse = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse = objectMapper.readValue(createStringResponse, DeviceResponse.class);

        var patchRequest = DeviceRequest.builder()
                .name("device 2")
                .brand("brand new")
                .state("available")
                .build();
        mockMvc.perform(patch("/devices/" + createResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Update of in-use devices are not allowed. Device Id: " + createResponse.getId()));

    }

}
