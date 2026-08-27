package com.skyscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication
        extends Application<HoenScannerConfiguration> {

    public static void main(String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Hoen Scanner";
    }

    @Override
    public void initialize(
            final Bootstrap<HoenScannerConfiguration> bootstrap) {
    }

    @Override
    public void run(
            final HoenScannerConfiguration configuration,
            final Environment environment) throws Exception {

        ObjectMapper objectMapper = environment.getObjectMapper();
        List<SearchResult> searchResults = new ArrayList<>();

        try (
                InputStream rentalCars = getClass()
                        .getClassLoader()
                        .getResourceAsStream("rental_cars.json");

                InputStream hotels = getClass()
                        .getClassLoader()
                        .getResourceAsStream("hotels.json")
        ) {
            searchResults.addAll(objectMapper.readValue(
                    rentalCars,
                    new TypeReference<List<SearchResult>>() {
                    }
            ));

            searchResults.addAll(objectMapper.readValue(
                    hotels,
                    new TypeReference<List<SearchResult>>() {
                    }
            ));
        }

        environment.jersey().register(
                new SearchResource(searchResults)
        );
    }
}