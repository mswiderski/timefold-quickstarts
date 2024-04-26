package org.acme.bedallocation.rest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.Provider;

import ai.timefold.models.sdk.api.domain.ModelRequest;

import org.acme.bedallocation.domain.BedPlan;
import org.acme.bedallocation.model.BedPlanConstraintConfiguration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class ModelRequestMessageBodyReader
        implements MessageBodyReader<ModelRequest<BedPlan, BedPlanConstraintConfiguration>> {

    @Inject
    ObjectMapper mapper;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == ModelRequest.class;
    }

    @Override
    public ModelRequest<BedPlan, BedPlanConstraintConfiguration> readFrom(
            Class<ModelRequest<BedPlan, BedPlanConstraintConfiguration>> type, Type genericType,
            Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {

        return mapper.readValue(entityStream,
                new TypeReference<>() {
                });
    }

}
