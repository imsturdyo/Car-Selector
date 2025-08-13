package com.takehome.carselection.services;

import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.Vehicle;

import java.util.List;

public interface VehicleSearchService {
    List<Vehicle> search(SearchCriteria criteria);
}
