package com.mycompany.final_project.route;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import com.mycompany.final_project.spark.SparkContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

class GreetingRouteShould {

    private static final int port = 7999;
    private static final String basePath = "/test";

    private SparkContext context;

    @BeforeAll
    static void beforeAll() {

        RestAssured.port = port;
        RestAssured.basePath = basePath;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeEach
    void beforeEach() {

        context = new SparkContext(port, basePath);
        context.defaultContentType("application/json");
        context.addRouteBuilder(new GreetingRoute());
    }

    @AfterEach
    void afterEach() {

        context.destroy();
    }

    @Test
    void greet_john() {

        final String jsonBody = format("{\"name\":\"%s\",\"surname\":\"%s\"}", "John", "Smith");

        //@formatter:off
        given()
            .accept(JSON)
            .contentType(JSON)
            .body(jsonBody)
        .when()
            .post("/greeting")
        .then()
            .statusCode(200)
            .contentType(JSON)
            .body("greeting", not(nullValue()))
            .body("greeting", equalTo("Hello, John Smith!"));
        //@formatter:on
    }
}