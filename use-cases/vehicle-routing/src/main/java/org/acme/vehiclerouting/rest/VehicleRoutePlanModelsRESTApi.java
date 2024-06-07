package org.acme.vehiclerouting.rest;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import ai.timefold.quarkus.models.sdk.rest.ModelsRESTApi;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.RecommendedFit;

import org.acme.vehiclerouting.domain.VehicleRoutePlan;
import org.acme.vehiclerouting.domain.dto.ApplyRecommendationRequest;
import org.acme.vehiclerouting.domain.dto.RecommendationRequest;
import org.acme.vehiclerouting.domain.dto.VehicleRecommendation;
import org.acme.vehiclerouting.solver.VehicleRouteRecommendationService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Vehicle Routing with Capacity and Time Windows",
        description = "Vehicle Routing optimizes routes of vehicles with given capacities to visits available in specified time windows.")
@Path("/v1/route-plans")
public interface VehicleRoutePlanModelsRESTApi extends ModelsRESTApi {



    @Operation(summary = "Request recommendations to the RecommendedFit API for a new visit.",
            operationId = "requestRecommendation")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "The list of fits for the given visit.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = List.class))) })
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    @Path("recommendation")
    default Response recommendedFit(RecommendationRequest request) {

        List<RecommendedFit<VehicleRecommendation, HardSoftLongScore>> recommendedFitList =
                recommendationService().recommendedFit(request);
        return Response.ok(recommendedFitList).build();
    }

    @Operation(summary = "Applies a given recommendation.", operationId = "applyRecommendation")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "The new solution updated with the recommendation.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = VehicleRoutePlan.class))) })
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces(MediaType.APPLICATION_JSON)
    @Path("recommendation/apply")
    default VehicleRoutePlan applyRecommendedFit(ApplyRecommendationRequest request) {
        VehicleRoutePlan updatedSolution = recommendationService().applyRecommendedFit(request);

        return updatedSolution;
    }

    VehicleRouteRecommendationService recommendationService();
}
