package com.mycompany.final_project.spark;

import java.util.Optional;

public class SparkContextConfig {

    private final int port;

    private final String basePath;

    private String staticFileLocation;

    private Integer threadPool;

    public SparkContextConfig(int port, String basePath) {

        this.port = port;
        this.basePath = basePath;
    }

    public int port() {
        return port;
    }

    public String basePath() {
        return basePath;
    }

    public void staticFileLocation(final String location) {
        this.staticFileLocation = location;
    }

    public Optional<String> staticFileLocation() {
        return Optional.ofNullable(staticFileLocation);
    }

    public void threadPool(final int threadPool) {
        this.threadPool = threadPool;
    }

    public Optional<Integer> threadPool() {
        return Optional.ofNullable(threadPool);
    }

    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();

        builder.append("[");

        builder.append("port=");
        builder.append(port);
        builder.append(", ");

        builder.append("basePath=");
        builder.append(basePath);

        if (staticFileLocation != null) {
            builder.append(", ");
            builder.append("staticFileLocation=");
            builder.append(staticFileLocation);
        }

        if (threadPool != null) {
            builder.append(", ");
            builder.append("threadPool=");
            builder.append(threadPool);
        }

        builder.append("]");

        return builder.toString();
    }
}
