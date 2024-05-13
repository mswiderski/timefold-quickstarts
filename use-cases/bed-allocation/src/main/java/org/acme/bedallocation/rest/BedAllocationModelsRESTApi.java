package org.acme.bedallocation.rest;

import jakarta.ws.rs.Path;

import ai.timefold.quarkus.models.sdk.rest.ModelsRESTApi;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Bed allocation",
        description = "Timefold Bed allocation service - Assign beds based on equipment available")
@Path("/v1/bed-allocations")
public interface BedAllocationModelsRESTApi extends ModelsRESTApi {

}
