package com.takehome.carselection.controllers;

import com.takehome.carselection.models.Meta;
import com.takehome.carselection.services.VehicleSearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    VehicleSearchService service;

    @Test
    void meta_returnsLists() throws Exception {
        Mockito.when(service.meta()).thenReturn(
                new Meta(List.of("Ford","Toyota"), List.of("Sedan","SUV"), List.of(2,5,7))
        );

        mvc.perform(get("/api/meta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.makes", containsInAnyOrder("Ford","Toyota")))
                .andExpect(jsonPath("$.bodies", containsInAnyOrder("Sedan","SUV")))
                .andExpect(jsonPath("$.seatOptions[?(@==5)]").exists());
    }
}
