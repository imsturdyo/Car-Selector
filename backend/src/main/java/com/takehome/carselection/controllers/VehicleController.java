package com.takehome.carselection.controllers;

import com.takehome.carselection.exceptions.BadRequestException;
import com.takehome.carselection.models.Meta;
import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.SearchResponse;
import com.takehome.carselection.services.VehicleSearchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class VehicleController {
    private final VehicleSearchService service;

    public VehicleController(VehicleSearchService service) {
        this.service = service;
    }

    @GetMapping("/meta")
    public Meta meta() {
        return service.meta();
    }

    @GetMapping("/vehicles")
    public SearchResponse vehicles(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String body,
            @RequestParam(required = false) Integer minSeats,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false, defaultValue = "msrp") String sort,
            @RequestParam(required = false, defaultValue = "asc") String order,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "12") Integer pageSize
    ) {

        if (page < 1) {
            throw new BadRequestException("Page must be equal to or greater than 1");
        }

        if (pageSize < 1 || pageSize > 100) {
            throw new BadRequestException("pageSize must be between 1 and 100");
        }

        return service.searchPaged(new SearchCriteria(make, body, minSeats, maxPrice, sort, order), page, pageSize);
    }
}
