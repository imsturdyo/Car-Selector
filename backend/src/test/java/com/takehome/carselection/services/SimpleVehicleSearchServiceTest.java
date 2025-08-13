package com.takehome.carselection.services;

import com.takehome.carselection.loaders.VehicleLoader;
import com.takehome.carselection.models.Meta;
import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.SearchResponse;
import com.takehome.carselection.models.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleVehicleSearchServiceTest {

    private static class FakeLoader implements VehicleLoader {
        @Override
        public List<Vehicle> loadVehicles() {
            return List.of(
                    vehicle(2021, "Toyota", "Corolla", "LE", "Sedan", 5, 20500.00, "petrol"),
                    vehicle(2020, "Honda",  "CR-V",    "EX", "SUV",   5, 30950.00, "diesel"),
                    vehicle(2021, "Tesla",  "Model 3", "Std+", "Sedan", 5, 39990.00, "electric"),
                    vehicle(2019, "Ford",   "Focus",   "SE", "Hatchback", 5, 17990.00, "petrol"),
                    vehicle(2018, "Ford",   "Fiesta",  "S",  "Hatchback", 5, 14990.00, "petrol")
            );
        }
        private Vehicle vehicle(Integer year, String make, String model, String trim, String body, Integer seats, Double msrp, String fuel) {
            Vehicle vehicle = new Vehicle();
            vehicle.setYear(year); vehicle.setMake(make); vehicle.setModel(model); vehicle.setTrim(trim);
            vehicle.setBody(body); vehicle.setSeats(seats); vehicle.setMsrp(msrp); vehicle.setFuel(fuel);
            return vehicle;
        }

    }

    private SimpleVehicleSearchService service;

    @BeforeEach
    void setUp() {
        service = new SimpleVehicleSearchService(new FakeLoader());
    }

    @Test
    void meta_isComputedOnce_andHasDefaultsIfMissing() {
        Meta meta1 = service.meta();
        Meta meta2 = service.meta();
        assertThat(meta1).isNotNull();
        assertThat(meta1.getMakes()).contains("Toyota", "Honda", "Tesla", "Ford");
        assertThat(meta1.getBodies()).contains("Sedan", "SUV", "Hatchback");
        assertThat(meta1.getSeatOptions()).contains(5);
        assertThat(meta2).isSameAs(meta1);
    }

    @Test
    void search_filtersByMake_andBody_andNumericBounds() {
        SearchCriteria searchCriteria = new SearchCriteria("Ford", "Hatchback", null, 17000.00, "msrp", "asc");
        var results = service.search(searchCriteria);
        assertThat(results).extracting(Vehicle::getModel).containsExactly("Fiesta");
    }

    @Test
    void search_sortsByMsrpAscending_byDefault() {
        SearchCriteria criteria = new SearchCriteria(null, null, null, null, "msrp", "asc");
        var results = service.search(criteria);
        assertThat(results).extracting(Vehicle::getMsrp).containsExactly(14990.00, 17990.00, 20500.00, 30950.00, 39990.00);
    }

    @Test
    void search_sortsByYearDescending_whenRequested() {
        SearchCriteria criteria = new SearchCriteria(null, null, null, null, "year", "desc");
        var years = service.search(criteria).stream().map(Vehicle::getYear).toList();
        assertThat(years).containsExactly(2021, 2021, 2020, 2019, 2018);
    }

    @Test
    void searchPaged_returnsEnvelope_andSlicesByPageAndSize() {
        SearchCriteria criteria = new SearchCriteria(null, null, null, null, "msrp", "asc");
        SearchResponse page1 = service.searchPaged(criteria, 1, 2);
        SearchResponse page2 = service.searchPaged(criteria, 2, 2);
        SearchResponse page3 = service.searchPaged(criteria, 3, 2);

        assertThat(page1.getTotal()).isEqualTo(5);
        assertThat(page1.getResults()).extracting(Vehicle::getMsrp).containsExactly(14990.00, 17990.00);

        assertThat(page2.getResults()).extracting(Vehicle::getMsrp).containsExactly(20500.00, 30950.00);
        assertThat(page3.getResults()).extracting(Vehicle::getMsrp).containsExactly(39990.00);
    }

    @Test
    void search_usesIndexesForMakeAndBody_intersectionWorks() {
        // Make only
        var ford = service.search(new SearchCriteria("Ford", null, null, null, "msrp", "asc"));
        assertThat(ford).hasSize(2);

        // Body only
        var sedans = service.search(new SearchCriteria(null, "Sedan", null, null, "msrp", "asc"));
        assertThat(sedans).hasSize(2);

        // Both
        var fordSedans = service.search(new SearchCriteria("Ford", "Sedan", null, null, "msrp", "asc"));
        assertThat(fordSedans).isEmpty();
    }
}
