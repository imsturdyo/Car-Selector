package com.takehome.carselection.services;

import com.takehome.carselection.loaders.VehicleLoader;
import com.takehome.carselection.models.Meta;
import com.takehome.carselection.models.SearchCriteria;
import com.takehome.carselection.models.SearchResponse;
import com.takehome.carselection.models.Vehicle;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class SimpleVehicleSearchService implements VehicleSearchService{

    private static final Set<String> ALLOWED_SORT =  Set.of("msrp","year");
    private static final Set<String> ALLOWED_ORDER = Set.of("asc","desc");

    private final VehicleLoader loader;
    private final AtomicReference<Meta> cachedMeta = new AtomicReference<>();
    private List<Vehicle> allVehicles;
    private Map<String, List<Vehicle>> byMake;
    private Map<String, List<Vehicle>> byBody;

    public SimpleVehicleSearchService(VehicleLoader loader) {
        this.loader = loader;
    }

    @Override
    public Meta meta() {
        Meta meta = cachedMeta.get();
        if (meta != null) {
            return meta;
        }

        List<Vehicle> all = loader.loadVehicles();
        var makes = all.stream().map(Vehicle::getMake).filter(Objects::nonNull).collect(Collectors.toSet());
        var bodies = all.stream().map(Vehicle::getBody).filter(Objects::nonNull).collect(Collectors.toSet());
        var seats  = all.stream().map(Vehicle::getSeats).filter(Objects::nonNull).collect(Collectors.toSet());

        Meta computed = new Meta(
                makes.stream().sorted().toList(),
                (bodies.isEmpty() ? List.of("Sedan","SUV","Hatchback") : bodies.stream().sorted().toList()),
                (seats.isEmpty()  ? List.of(2,4,5,7) : seats.stream().sorted().toList())
        );

        cachedMeta.compareAndSet(null, computed);

        return cachedMeta.get();
    }

    @Override
    public List<Vehicle> search(SearchCriteria criteria) {
        ensureLoaded();
        List<Vehicle> candidates = allVehicles;

        if (criteria.getMake() != null && !criteria.getMake().isBlank()) {
            candidates = byMake.getOrDefault(criteria.getMake().toLowerCase(), List.of());
        }
        if (criteria.getBody() != null && !criteria.getBody().isBlank()) {
            var bodyList = byBody.getOrDefault(criteria.getBody().toLowerCase(), List.of());
            if (candidates == allVehicles) {
                candidates = bodyList;
            } else {
                // intersect
                Set<Vehicle> set = new HashSet<>(candidates);
                candidates = bodyList.stream().filter(set::contains).toList();
            }
        }

        // Filter
        var filtered = candidates.stream()
                .filter(vehicle -> criteria.getMinSeats() == null || (vehicle.getSeats() != null && vehicle.getSeats() >= criteria.getMinSeats()))
                .filter(vehicle -> criteria.getMaxPrice() == null || (vehicle.getMsrp() != null && vehicle.getMsrp() <= criteria.getMaxPrice()))
                .toList();

        // Sort
        String sort = (criteria.getSort() == null || !ALLOWED_SORT.contains(criteria.getSort())) ? "msrp" : criteria.getSort();
        String order = (criteria.getOrder() == null || !ALLOWED_ORDER.contains(criteria.getOrder())) ? "asc" : criteria.getOrder();

        Comparator<Vehicle> compare = switch (sort) {
            case "year" -> Comparator.comparing(vehicle -> vehicle.getYear() == null ? Integer.MIN_VALUE : vehicle.getYear());
            default     -> Comparator.comparing(vehicle -> vehicle.getMsrp() == null ? Integer.MAX_VALUE : vehicle.getMsrp());
        };
        if ("desc".equalsIgnoreCase(order)) {
            compare = compare.reversed();
        }

        return filtered.stream().sorted(compare).toList();
    }

    public SearchResponse searchPaged(SearchCriteria criteria, int requestedPage, int requestedPageSize) {
        var filteredAndSortedVehicles = search(criteria);
        int totalResults = filteredAndSortedVehicles.size();

        int currentPage = Math.max(1, requestedPage);
        int itemsPerPage = Math.min(Math.max(1, requestedPageSize), 100);

        int startIndex = (currentPage - 1) * itemsPerPage;

        if (startIndex >= totalResults) {
            return new SearchResponse(totalResults, currentPage, itemsPerPage, List.of());
        }

        int endIndex = Math.min(startIndex + itemsPerPage, totalResults);

        var pageResults = filteredAndSortedVehicles.subList(startIndex, endIndex);

        return new SearchResponse(totalResults, currentPage, itemsPerPage, pageResults);
    }

    private synchronized void ensureLoaded() {
        if (allVehicles != null) return;
        allVehicles = loader.loadVehicles();
        byMake = allVehicles.stream()
                .filter(vehicle -> vehicle.getMake() != null)
                .collect(Collectors.groupingBy(vehicle -> vehicle.getMake().toLowerCase()));
        byBody = allVehicles.stream()
                .filter(vehicle -> vehicle.getBody() != null)
                .collect(Collectors.groupingBy(vehicle -> vehicle.getBody().toLowerCase()));
    }
}
