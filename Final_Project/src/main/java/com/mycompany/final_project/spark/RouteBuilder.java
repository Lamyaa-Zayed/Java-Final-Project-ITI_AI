package com.mycompany.final_project.spark;

import spark.Service;

@FunctionalInterface
public interface RouteBuilder {

    void configure(final Service spark, final String basePath);
}
