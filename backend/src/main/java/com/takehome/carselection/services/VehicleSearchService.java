package com.takehome.carselection.services;

import com.takehome.carselection.models.Meta;
import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.Vehicle;

import java.util.List;

public interface VehicleSearchService {
    Meta meta();
    List<Vehicle> search(SearchCriteria criteria);
}
