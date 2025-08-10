package com.giovannemomesso.device_service.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giovannemomesso.device_service.adapter.rest.dto.DeviceRequest;
import com.giovannemomesso.device_service.adapter.rest.dto.DeviceResponse;
import com.giovannemomesso.device_service.domain.DeviceState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .state(DeviceState.AVAILABLE.getDescription())
                .build();

        mockMvc.perform(post("/devices")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("device 1"))
                .andExpect(jsonPath("$.brand").value("brand 1"))
                .andExpect(jsonPath("$.state").value(DeviceState.AVAILABLE.getDescription()))
                .andExpect(jsonPath("$.createdTime").exists());

    }

    @Test
    void patchDevice_givenValidInput_shouldPatchDeviceAndReturn() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state(DeviceState.AVAILABLE.getDescription())
                .build();

        var createStringResponse = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse = objectMapper.readValue(createStringResponse, DeviceResponse.class);

        var patchRequest = DeviceRequest.builder()
                .name("device 2")
                .brand("brand new")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        mockMvc.perform(patch("/devices/" + createResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createResponse.getId()))
                .andExpect(jsonPath("$.name").value("device 2"))
                .andExpect(jsonPath("$.brand").value("brand new"))
                .andExpect(jsonPath("$.state").value(DeviceState.IN_USE.getDescription()))
                .andExpect(jsonPath("$.createdTime").exists());

    }

    @Test
    void patchDevice_givenInvalidState_shouldReturnBadRequest() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        var createStringResponse = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse = objectMapper.readValue(createStringResponse, DeviceResponse.class);

        var patchRequest = DeviceRequest.builder()
                .name("device 2")
                .brand("brand new")
                .state(DeviceState.AVAILABLE.getDescription())
                .build();
        mockMvc.perform(patch("/devices/" + createResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patchRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Update of in-use devices are not allowed. Device Id: " + createResponse.getId()));

    }

    @Test
    void getDeviceById_givenValidId_shouldReturnDevice() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        var createStringResponse = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse = objectMapper.readValue(createStringResponse, DeviceResponse.class);

        mockMvc.perform(get("/devices/" + createResponse.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createResponse.getId()))
                .andExpect(jsonPath("$.name").value("device 1"))
                .andExpect(jsonPath("$.brand").value("brand 1"))
                .andExpect(jsonPath("$.state").value(DeviceState.IN_USE.getDescription()))
                .andExpect(jsonPath("$.createdTime").exists());
    }

    @Test
    void getDeviceById_givenInvalidId_shouldReturnBadRequest() throws Exception {

        var randomId = UUID.randomUUID();

        mockMvc.perform(get("/devices/" + randomId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Device not found for id: " + randomId));

    }

    @Test
    void getAllDevices_shouldReturnAllDevices() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        var createStringResponse = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse = objectMapper.readValue(createStringResponse, DeviceResponse.class);

        var createDeviceRequest1 = DeviceRequest.builder()
                .name("device 2")
                .brand("brand 2")
                .state(DeviceState.AVAILABLE.getDescription())
                .build();

        var createStringResponse1 = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest1)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse1 = objectMapper.readValue(createStringResponse1, DeviceResponse.class);


        mockMvc.perform(get("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(createResponse.getId()))
                .andExpect(jsonPath("$[0].name").value("device 1"))
                .andExpect(jsonPath("$[1].id").value(createResponse1.getId()))
                .andExpect(jsonPath("$[1].name").value("device 2"));
    }

    @Test
    void getAllDevices_givenBrandParam_shouldReturnAllDevicesForBrand() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createDeviceRequest1 = DeviceRequest.builder()
                .name("device 2")
                .brand("brand 2")
                .state(DeviceState.AVAILABLE.getDescription())
                .build();

        var createStringResponse1 = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest1)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse1 = objectMapper.readValue(createStringResponse1, DeviceResponse.class);

        var createDeviceRequest2 = DeviceRequest.builder()
                .name("device 3")
                .brand("brand 2")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        var createStringResponse2 = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest2)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse2 = objectMapper.readValue(createStringResponse2, DeviceResponse.class);


        mockMvc.perform(get("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("brand", "brand 2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(createResponse1.getId()))
                .andExpect(jsonPath("$[0].name").value("device 2"))
                .andExpect(jsonPath("$[1].id").value(createResponse2.getId()))
                .andExpect(jsonPath("$[1].name").value("device 3"));
    }

    @Test
    void getAllDevices_givenStateParam_shouldReturnAllDevicesForBrand() throws Exception {
        var createDeviceRequest = DeviceRequest.builder()
                .name("device 1")
                .brand("brand 1")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        var createStringResponse = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse = objectMapper.readValue(createStringResponse, DeviceResponse.class);

        var createDeviceRequest1 = DeviceRequest.builder()
                .name("device 2")
                .brand("brand 2")
                .state(DeviceState.AVAILABLE.getDescription())
                .build();

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest1)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();


        var createDeviceRequest2 = DeviceRequest.builder()
                .name("device 3")
                .brand("brand 2")
                .state(DeviceState.IN_USE.getDescription())
                .build();

        var createStringResponse2 = mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createDeviceRequest2)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        var createResponse2 = objectMapper.readValue(createStringResponse2, DeviceResponse.class);


        mockMvc.perform(get("/devices")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("state", "in-use"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(createResponse.getId()))
                .andExpect(jsonPath("$[0].name").value("device 1"))
                .andExpect(jsonPath("$[1].id").value(createResponse2.getId()))
                .andExpect(jsonPath("$[1].name").value("device 3"));
    }

}
