package com.mycompany.final_project.route;

import com.mycompany.final_project.spark.RouteBuilder;
import spark.Service;


public class RandomResultsRoute implements RouteBuilder {

    private final java.util.Random random = new java.util.Random();

    @Override
    public void configure(final Service spark, final String basePath) {

        spark.get(basePath + "/random/city", (request, response) -> randomCity());
        spark.get(basePath + "/random/number", (request, response) -> randomNumber());
        spark.get(basePath + "/random/uuid", (request, response) -> randomUUID());
    }

    private String randomCity() {

        return cities[random.nextInt(cities.length - 1)];
    }

    private String randomNumber() {

        return String.valueOf(random.nextInt(100));
    }

    private String randomUUID() {

        return java.util.UUID.randomUUID().toString();
    }

    private static final String[] cities = new String[] {
        "Sydney",
        "Melbourne",
        "Berlin",
        "New York",
        "London",
        "Amsterdam",
        "Paris",
        "Brisbane",
        "Budapest",
        "Roma",
        "Genoa",
        "Barcelona",
        "Florianopolis",
        "Dublin",
        "Rio de Janeiro",
        "Madrid",
        "Copenhagen"
    };
}
