package com.takehome.carselection.controllers;

import com.takehome.carselection.models.Meta;
import com.takehome.carselection.services.VehicleSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VehicleController {
    private final VehicleSearchService service;

    public VehicleController(VehicleSearchService service) {
        this.service = service;
    }

    @GetMapping("/meta")
    public Meta meta() {
        return service.meta();
    }
}
