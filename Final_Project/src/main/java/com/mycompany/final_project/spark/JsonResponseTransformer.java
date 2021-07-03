package com.mycompany.final_project.spark;

import static com.juliaaano.payload.MediaType.JSON;

import spark.ResponseTransformer;

public class JsonResponseTransformer implements ResponseTransformer {

    @Override
    public String render(final Object model) {

        return JSON.payload().newInstance(model).raw();
    }
}
