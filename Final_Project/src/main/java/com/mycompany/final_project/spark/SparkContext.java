package com.mycompany.final_project.spark;

import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ISO_ORDINAL_DATE;
import static java.util.UUID.randomUUID;
import static spark.utils.StringUtils.isEmpty;
import static spark.utils.StringUtils.isNotEmpty;

import java.util.HashMap;
import java.util.Map;

import com.juliaaano.payload.InvalidMediaTypeException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import spark.Request;
import spark.Service;

public class SparkContext {

    private static final Logger logger = LoggerFactory.getLogger(SparkContext.class);

    private final Service spark;

    private final String basePath;

    public SparkContext(final SparkContextConfig config) {

        this.basePath = config.basePath();

        spark = Service.ignite().port(config.port());

        config.staticFileLocation().ifPresent(this::staticFiles);
        config.threadPool().ifPresent(spark::threadPool);

        spark.get("/status", (req, res) -> "{\"status\":\"OK\"}");

        spark.awaitInitialization();

        logger.info("Spark context started with {}", config);
    }

    public SparkContext(final int port, final String basePath) {

        this(new SparkContextConfig(port, basePath));
    }

    public void addRouteBuilder(final RouteBuilder resource) {

        resource.configure(spark, basePath);
        logger.info("Spark routes registered for {}.", resource.getClass().getSimpleName());
    }

    public void enableErrorHandler() {

        spark.notFound((requeust, response) -> {
            response.type("application/json");
            return "{\"message\":\"Resource not found\"}";
        });

        spark.exception(InvalidMediaTypeException.class, (exception, request, response) -> {
            response.status(415);
            response.type("application/json");
            response.body("{\"message\":\"" + exception.getMessage() + "\"}");
            logger.debug("Exception mapper handled a {}.", exception.getClass().getName(), exception);
        });

        spark.exception(Exception.class, (exception, request, response) -> {

            if ("com.google.gson.JsonSyntaxException".equals(exception.getClass().getName())) {
                response.status(400);
                response.body("{\"message\":\"Bad request.\"}");
            } else {
                response.status(500);
                response.body("{\"message\":\"" + exception.getMessage() + "\"}");
            }
            response.type("application/json");
            logger.error("Exception mapper handled a {}.", exception.getClass().getName(), exception);
        });

        logger.info("Spark error handler enabled.");
    }

    public void enableCors() {

        spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
        });

        logger.info("CORS support enabled.");
    }

    public void enableCorsPreflight() {

        spark.options(basePath + "/*", (request, response) -> {

            // Support of CORS preflight.
            // According to the CORS spec (http://www.w3.org/TR/cors/), any
            // Content-Type other than application/x-www-form-urlencoded,
            // multipart/form-data, or text/plain triggers the preflight.

            response.status(204);

            return "";
        });

        logger.info("CORS preflight support enabled.");
    }

    public void enableCorrelationId() {

        final String CORRELATION_ID = "X-Request-ID";

        spark.before(basePath + "/*", (request, response) -> {

            if (isEmpty(request.headers(CORRELATION_ID))) {

                final String correlationId = Helper.generateCorrelationId();

                response.header(CORRELATION_ID, correlationId);
                MDC.put(CORRELATION_ID, correlationId);

                logger.warn("Correlation ID not present in the request header [{}]; assigned random value.",
                        CORRELATION_ID);

            } else {

                response.header(CORRELATION_ID, request.headers(CORRELATION_ID));
                MDC.put(CORRELATION_ID, request.headers(CORRELATION_ID));
            }
        });

        spark.afterAfter(basePath + "/*", (request, response) -> MDC.remove(CORRELATION_ID));

        logger.info("Correlation ID support enabled via '{}' header.", CORRELATION_ID);
    }

    public void logHttpRequest() {

        spark.before(basePath + "/*", (request, response) -> {

            final StringBuilder requestDetails = new StringBuilder();

            Helper.appendHttpRequestUrl(request, requestDetails);
            requestDetails.append("\n");
            Helper.appendHttpHeaders(request, requestDetails);

            logger.debug("HTTP request received from {}:{} with the following details:\n{}",
                    request.raw().getRemoteHost(), request.raw().getRemotePort(), requestDetails.toString());
        });

        logger.info("Logging the details of all http requests under '{}'.", basePath);
    }

    public void logHttpRequestBody() {

        spark.before(basePath + "/*", (request, response) -> {

            final StringBuilder requestDetails = new StringBuilder();

            Helper.appendHttpRequestUrl(request, requestDetails);
            requestDetails.append("\n");
            Helper.appendHttpHeaders(request, requestDetails);

            logger.debug("HTTP request body:\n{}", request.body());
        });

        logger.info("Logging the body of all http requests under '{}'.", basePath);
    }

    public void defaultContentType(final String defaultContentType) {

        spark.before(basePath + "/*", (request, response) -> response.type(defaultContentType));
    }

    public void enableLogLevelPerRequest() {

        final String LOG_LEVEL_HEADER = "X-Log-Level";

        spark.before(basePath + "/*", (request, response) -> {

            final String logLevelHeader = request.headers(LOG_LEVEL_HEADER);
            if (isNotEmpty(logLevelHeader))
                MDC.put(LOG_LEVEL_HEADER, logLevelHeader);
        });

        spark.afterAfter(basePath + "/*", (request, response) -> MDC.remove(LOG_LEVEL_HEADER));

        logger.info("Log level per-request support enabled via '{}' header.", LOG_LEVEL_HEADER);
    }

    public void destroy() {

        spark.stop();
    }

    private void staticFiles(final String location) {

        spark.staticFiles.location(location);
        spark.staticFiles.expireTime(600);

        spark.staticFiles.header("Access-Control-Allow-Origin", "*");
        spark.staticFiles.header("Access-Control-Allow-Methods", "GET");
        spark.staticFiles.header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
    }

    private static final class Helper {

        private Helper() {
            super();
        }

        private static StringBuilder appendHttpRequestUrl(final Request request, final StringBuilder requestDetails) {

            requestDetails.append(request.requestMethod()).append(": ").append(request.url());

            if (isNotEmpty(request.queryString()))
                requestDetails.append("?").append(request.queryString());

            return requestDetails;
        }

        private static StringBuilder appendHttpHeaders(final Request request, final StringBuilder requestDetails) {

            final Map<String, String> headers = new HashMap<>();
            request.headers().forEach(header -> headers.put(header, request.headers(header)));

            return requestDetails.append("Headers: ").append(headers);
        }

        private static String generateCorrelationId() {

            // APP-YEAR-DAYOFYEAR-randomUUID: APP-2010-150-194a89c21ffa
            return "APP-".concat(now().format(ISO_ORDINAL_DATE)).concat(randomUUID().toString().substring(23, 36));
        }
    }
}
