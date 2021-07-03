package com.mycompany.final_project.route;

import com.juliaaano.payload.MediaType;

import spark.Request;

class RequestPayload<T> {

    private final Request request;
    private final Class<T> type;

    RequestPayload(final Request request, final Class<T> type) {

        this.request = request;
        this.type = type;
    }

    T get() {

        return MediaType.of(request.contentType()).payload().newInstance(request.body(), type).get();
    }
}
