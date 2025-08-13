package com.takehome.carselection.loaders;

import com.takehome.carselection.models.Vehicle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvVehicleLoader implements VehicleLoader {

    private final List<Vehicle> vehicles = new ArrayList<>();

    @Override
    public List<Vehicle> loadVehicles() {
        return vehicles;
    }

    public CsvVehicleLoader(@Value("${vehicles.csv.path}") Resource csv) throws Exception {
        if (csv == null || !csv.exists() || !csv.isReadable()) {
            throw new IllegalStateException("CSV not readable: " + csv);
        }

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(csv.getInputStream(), StandardCharsets.UTF_8))) {
            String header = bufferedReader.readLine();

            if (header == null) {
                throw new IllegalStateException("CSV is empty: " + csv);
            }

            String line;
            int row = 1;

            while ((line = bufferedReader.readLine()) != null) {
                row++;
                if (line.isBlank()) continue;

                String[] parts = line.split(",", -1);

                if (parts.length != 8) {
                    System.err.println("Skip row " + row + ": expected 8 columns, got " + parts.length);
                    continue;
                }

                String yearStr  = trim(parts[0]);
                String make     = trim(parts[1]);
                String model    = trim(parts[2]);
                String trim     = trim(parts[3]);
                String body     = trim(parts[4]);
                String seatsStr = trim(parts[5]);
                String msrpStr  = trim(parts[6]);
                String fuel     = trim(parts[7]);

                Integer year = toIntOrNull(yearStr);
                if (year == null || make == null || model == null) {
                    System.err.println("Skip row " + row + ": missing year/make/model");
                    continue;
                }

                Integer seats = toIntOrNull(seatsStr);
                BigDecimal msrp = toDecOrNull(msrpStr);

                vehicles.add(new Vehicle(
                        year, make, model, trim, body, seats, msrp, fuel
                ));
            }
        }
    }

    private static String trim(String value) {
        if (value == null) return null;
        value = value.trim();
        return value.isEmpty() ? null : value;
    }

    private static Integer toIntOrNull(String s) {
        try { return (s == null) ? null : Integer.valueOf(s); }
        catch (Exception e) { return null; }
    }
    private static BigDecimal toDecOrNull(String s) {
        try { return (s == null) ? null : new BigDecimal(s); }
        catch (Exception e) { return null; }
    }
}
