package org.acme.vehiclerouting.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import ai.timefold.sdk.core.api.ModelInput;
import ai.timefold.sdk.core.api.ModelOutput;
import ai.timefold.sdk.maps.service.integration.api.LocationsAwareSolverModel;
import ai.timefold.sdk.maps.service.integration.api.model.Location;
import ai.timefold.sdk.quarkus.deployment.defaults.EmptyModelKpi;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The plan for routing vehicles to visits, including:
 * <ul>
 * <li>capacity - each vehicle has a capacity for visits demand,</li>
 * <li>time windows - each visit accepts the vehicle only in specified time window.</li>
 * </ul>
 *
 * The planning solution is optimized according to the driving time (as opposed to the travel distance, for example)
 * because it is easy to determine if the vehicle arrival time fits into the visit time window.
 * In addition, optimizing travel time optimizes the distance too, as a side effect - in case there is a faster route,
 * the travel time takes precedence (highway vs. local road).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@PlanningSolution
public class VehicleRoutePlan implements ModelInput, ModelOutput, LocationsAwareSolverModel<HardSoftLongScore, EmptyModelKpi> {

    private String name;

    private Location southWestCorner;
    private Location northEastCorner;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @PlanningEntityCollectionProperty
    private List<Vehicle> vehicles;

    @PlanningEntityCollectionProperty
    @ValueRangeProvider
    private List<Visit> visits;

    @JsonIgnore
    @Schema(hidden = true)
    @PlanningScore
    private HardSoftLongScore score;

    private String scoreExplanation;

    public VehicleRoutePlan() {
    }

    @JsonCreator
    public VehicleRoutePlan(@JsonProperty("name") String name,
            @JsonProperty("southWestCorner") Location southWestCorner,
            @JsonProperty("northEastCorner") Location northEastCorner,
            @JsonProperty("startDateTime") LocalDateTime startDateTime,
            @JsonProperty("endDateTime") LocalDateTime endDateTime,
            @JsonProperty("vehicles") List<Vehicle> vehicles,
            @JsonProperty("visits") List<Visit> visits) {
        this.name = name;
        this.southWestCorner = southWestCorner;
        this.northEastCorner = northEastCorner;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.vehicles = vehicles;
        this.visits = visits;


    }

    public String getName() {
        return name;
    }

    public Location getSouthWestCorner() {
        return southWestCorner;
    }

    public Location getNorthEastCorner() {
        return northEastCorner;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long getTotalDrivingTimeSeconds() {
        return vehicles == null ? 0 : vehicles.stream().mapToLong(Vehicle::getTotalDrivingTimeSeconds).sum();
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getScoreExplanation() {
        return scoreExplanation;
    }

    public void setScoreExplanation(String scoreExplanation) {
        this.scoreExplanation = scoreExplanation;
    }

    @JsonIgnore
    @Override
    public List<Location> getLocations() {
        List<Location> locations = Stream.concat(
                vehicles.stream().map(Vehicle::getHomeLocation),
                visits.stream().map(Visit::getLocation)).toList();

        return locations;
    }

    @JsonIgnore
    @Override
    public EmptyModelKpi getKpis() {
        return new EmptyModelKpi();
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public Optional<String> getLocationSetName() {
        return Optional.empty();
    }
}
