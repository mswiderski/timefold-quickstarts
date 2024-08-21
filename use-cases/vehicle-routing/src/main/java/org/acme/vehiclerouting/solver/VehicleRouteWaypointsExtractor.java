package org.acme.vehiclerouting.solver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.sdk.maps.service.integration.api.WaypointsExtractor;
import ai.timefold.sdk.maps.service.integration.api.model.Location;
import ai.timefold.sdk.maps.service.integration.api.model.Waypoints;

import org.acme.vehiclerouting.domain.Vehicle;
import org.acme.vehiclerouting.domain.VehicleRoutePlan;
import org.acme.vehiclerouting.domain.Visit;

@ApplicationScoped
public class VehicleRouteWaypointsExtractor implements WaypointsExtractor<VehicleRoutePlan> {

    @Override
    public List<Waypoints> extractBaseWaypoints(VehicleRoutePlan solverVehicleRoutePlan) {
        return solverVehicleRoutePlan.getVehicles().stream()
                .filter(this::hasWaypoints)
                .map(this::getBaseWaypoints)
                .collect(Collectors.toList());
    }

    private boolean hasWaypoints(Vehicle vehicle) {
        return vehicle.getVisits() != null && !vehicle.getVisits().isEmpty();
    }

    private Waypoints getBaseWaypoints(Vehicle vehicle) {
        List<Location> wayLocations = new ArrayList<>(vehicle.getVisits().size() + 2);
        wayLocations.add(vehicle.getHomeLocation());
        for (Visit visit : vehicle.getVisits()) {
            wayLocations.add(visit.getLocation());
        }

        return new Waypoints(vehicle.getId(), wayLocations);
    }
}
