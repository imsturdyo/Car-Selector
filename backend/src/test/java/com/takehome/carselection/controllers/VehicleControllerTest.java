package com.takehome.carselection.controllers;

import com.takehome.carselection.models.Meta;
import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.SearchResponse;
import com.takehome.carselection.models.Vehicle;
import com.takehome.carselection.services.VehicleSearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
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
    @Test
    void vehicles_returnsEnvelope_withResults() throws Exception {
        Vehicle vehicle = new Vehicle();
        vehicle.setMake("Toyota"); vehicle.setModel("Corolla"); vehicle.setYear(2021); vehicle.setMsrp(20500.00);
        SearchResponse resp = new SearchResponse(1, 1, 12, List.of(vehicle));
        Mockito.when(service.searchPaged(Mockito.any(SearchCriteria.class), Mockito.eq(1), Mockito.eq(12)))
                .thenReturn(resp);

        mvc.perform(get("/api/vehicles")
                        .param("sort","msrp").param("order","asc")
                        .param("page","1").param("pageSize","12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.total", is(1)))
                .andExpect(jsonPath("$.results[0].make", is("Toyota")))
                .andExpect(jsonPath("$.results[0].model", is("Corolla")));
    }

    @Test
    void vehicles_validatesPage_andReturns400() throws Exception {
        mvc.perform(get("/api/vehicles").param("page","0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", containsString("Page must be equal to or greater than 1")));
    }

    @Test
    void vehicles_validatesPageSize_andReturns400() throws Exception {
        mvc.perform(get("/api/vehicles").param("pageSize","9999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("pageSize")));
    }

    @Test
    void unexpectedException_returns500() throws Exception {
        Mockito.when(service.searchPaged(any(SearchCriteria.class), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("boom"));

        mvc.perform(get("/api/vehicles").param("page","1").param("pageSize","12"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is(500)))
                .andExpect(jsonPath("$.error", is("Internal Server Error")));
    }

}
