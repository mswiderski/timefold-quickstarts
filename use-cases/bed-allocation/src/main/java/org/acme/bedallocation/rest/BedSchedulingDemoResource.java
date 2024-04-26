package org.acme.bedallocation.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import ai.timefold.models.sdk.api.domain.ModelRequest;

import org.acme.bedallocation.domain.BedPlan;
import org.acme.bedallocation.model.BedPlanConstraintConfiguration;
import org.acme.bedallocation.rest.DemoDataGenerator.DemoData;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Demo data", description = "Timefold provided demo Employee Schedule data")
@Path("/v1/demo-data")
public class BedSchedulingDemoResource {

    private final DemoDataGenerator dataGenerator;

    @Inject
    public BedSchedulingDemoResource(DemoDataGenerator dataGenerator) {
        this.dataGenerator = dataGenerator;
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of known test data sets") })
    @Operation(summary = "List of available test data sets represented as identifiers")
    @GET
    public List<String> list() {
        // returns a list of demo endpoints that can provide test data sets
        return Arrays.stream(DemoDataGenerator.DemoData.values())
                .map(DemoDataGenerator.DemoData::name)
                .collect(Collectors.toList());
    }

    @APIResponses(value = {
            @APIResponse(responseCode = "404", description = "In case given test data does not exist",
                    content = @Content(mediaType = "application/json")),
            @APIResponse(responseCode = "200", description = "Employee schedule as a data set") })
    @Operation(summary = "Get bed schedule test data set with given identifier")
    @GET
    @Path("/{demoDataId}")
    public ModelRequest<BedPlan, BedPlanConstraintConfiguration>
            getTestData(@PathParam("demoDataId") DemoData demoData) {

        return dataGenerator.generateDemoData(demoData);
    }

}
