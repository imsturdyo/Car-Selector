package com.takehome.carselection.services;

import com.takehome.carselection.loaders.VehicleLoader;
import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.SearchResponse;
import com.takehome.carselection.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleVehicleSearchServiceBranchesTest {

    private static class LoaderWithNulls implements VehicleLoader {
        @Override public List<Vehicle> loadVehicles() {
            return List.of(
                    vehicle(2021,"A","M1", null, "Sedan", 5, null, null), // null msrp
                    vehicle(null,"B","M2", null, "SUV",   5, 10000.00, null), // null year
                    vehicle(2020,"C","M3", null, null,    null, 5000.00, null) // null body/seats
            );
        }
        private Vehicle vehicle(Integer year, String make, String model, String trim, String body, Integer seats, Double msrp, String fuel) {
            var vehicle = new Vehicle();
            vehicle.setYear(year); vehicle.setMake(make); vehicle.setModel(model); vehicle.setTrim(trim);
            vehicle.setBody(body); vehicle.setSeats(seats); vehicle.setMsrp(msrp); vehicle.setFuel(fuel);
            return vehicle;
        }
    }

    private static class EmptyLoader implements VehicleLoader {
        @Override public List<Vehicle> loadVehicles() { return List.of(); }
    }

    private SimpleVehicleSearchService service;

    @BeforeEach
    void setUp() {
        service = new SimpleVehicleSearchService(new LoaderWithNulls());
    }

    @Test
    void invalidSortAndOrder_fallBackToDefaults() {
        var criteria = new SearchCriteria(null, null, null, null, "not-a-field", "downwards");
        var msrps = service.search(criteria).stream().map(Vehicle::getMsrp).toList();
        assertThat(msrps).containsExactly(5000.00, 10000.00, null);
    }

    @Test
    void sortByYear_desc_handlesNullYears() {
        var criteria = new SearchCriteria(null, null, null, null, "year", "desc");
        var years = service.search(criteria).stream().map(Vehicle::getYear).toList();

        assertThat(years).containsExactly(2021, 2020, null);
    }

    @Test
    void paging_outOfRange_returnsEmptyButKeepsTotals() {
        var criteria = new SearchCriteria(null, null, null, null, "msrp", "asc");
        SearchResponse response = service.searchPaged(criteria, 99, 10);
        assertThat(response.getTotal()).isEqualTo(3);
        assertThat(response.getResults()).isEmpty();
        assertThat(response.getPage()).isEqualTo(99);
        assertThat(response.getPageSize()).isEqualTo(10);
    }

    @Test
    void emptyDataset_metaHasDefaults_andSearchPagedReturnsZero() {
        var emptySvc = new SimpleVehicleSearchService(new EmptyLoader());
        var meta = emptySvc.meta();
        assertThat(meta.getSeatOptions()).contains(2,4,5,7); // defaults
        var resp = emptySvc.searchPaged(new SearchCriteria(null,null,null,null,"msrp","asc"), 1, 10);
        assertThat(resp.getTotal()).isZero();
        assertThat(resp.getResults()).isEmpty();
    }
}
