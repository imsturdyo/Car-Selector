package com.takehome.carselection.services;

import com.takehome.carselection.loaders.VehicleLoader;
import com.takehome.carselection.models.Meta;
import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.Vehicle;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SimpleVehicleSearchService implements VehicleSearchService{

    private final VehicleLoader loader;

    public SimpleVehicleSearchService(VehicleLoader loader) {
        this.loader = loader;
    }

    @Override
    public Meta meta() {
        List<Vehicle> all = loader.loadVehicles();
        var makes = all.stream().map(Vehicle::getMake).filter(Objects::nonNull).collect(Collectors.toSet());
        var bodies = all.stream().map(Vehicle::getBody).filter(Objects::nonNull).collect(Collectors.toSet());
        var seats  = all.stream().map(Vehicle::getSeats).filter(Objects::nonNull).collect(Collectors.toSet());
        return new Meta(
                makes.stream().sorted().toList(),
                bodies.stream().sorted().toList(),
                seats.stream().sorted().toList()
        );
    }

    @Override
    public List<Vehicle> search(SearchCriteria criteria) {
        return null;
    }
}
