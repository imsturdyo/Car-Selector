package com.takehome.carselection.loaders;

import com.takehome.carselection.models.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvVehicleLoaderTest {

    @TempDir
    Path tmp;

    @Test
    void loadVehicles_readsCsvAndMapsFields() throws Exception {
        Path csv = tmp.resolve("vehicles.csv");
        Files.writeString(csv, String.join("\n",
                "year,make,model,trim,body,seats,msrp,fuel",
                "2021,Toyota,Corolla,LE,Sedan,5,20500,petrol",
                "2020,Honda,CR-V,EX,SUV,5,30950,diesel"
        ), StandardCharsets.UTF_8);

        Resource resource = new FileSystemResource(csv.toFile());
        var loader = new CsvVehicleLoader(resource);
        List<Vehicle> list = loader.loadVehicles();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getMake()).isEqualTo("Toyota");
        assertThat(list.get(1).getBody()).isEqualTo("SUV");
    }

    @Test
    void loadVehicles_skipsRowsWithWrongColumnCount() throws Exception {
        Path csv = tmp.resolve("vehicles_mixed.csv");
        Files.writeString(csv, String.join("\n",
                "year,make,model,trim,body,seats,msrp,fuel",
                "2021,Toyota,Corolla,LE,Sedan,5,20500,petrol",
                // BAD: extra comma
                "2016,Acura,MDX,Technology, Entertainment and AcuraWatch Plus Packages,SUV,7,50640,premium unleaded (recommended)",
                "2020,Honda,CR-V,EX,SUV,5,30950,diesel"
        ), StandardCharsets.UTF_8);

        Resource res = new FileSystemResource(csv.toFile());
        var loader = new CsvVehicleLoader(res);
        List<Vehicle> list = loader.loadVehicles();

        assertThat(list).hasSize(2);
        assertThat(list.get(0).getModel()).isEqualTo("Corolla");
        assertThat(list.get(1).getMake()).isEqualTo("Honda");
    }

    @Test
    void loadVehicles_parsesNullsForBlankNumerics() throws Exception {
        Path csv = tmp.resolve("vehicles_nulls.csv");
        Files.writeString(csv, String.join("\n",
                "year,make,model,trim,body,seats,msrp,fuel",
                "2022,Ford,Fiesta,SE,Hatchback,, ,regular"  // seats & msrp blank -> null
        ), StandardCharsets.UTF_8);

        Resource res = new FileSystemResource(csv.toFile());
        var loader = new CsvVehicleLoader(res);
        List<Vehicle> list = loader.loadVehicles();

        assertThat(list).hasSize(1);
        Vehicle v = list.get(0);
        assertThat(v.getSeats()).isNull();
        assertThat(v.getMsrp()).isNull();
        assertThat(v.getFuel()).isEqualTo("regular");
    }
}
