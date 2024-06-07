package org.acme.vehiclerouting.solver.justifications;

public record VehicleCapacityJustification(String vehicleId, int capacity, int demand,
        String description) implements VehicleRouteJustification {

    public VehicleCapacityJustification(String vehicleId, int capacity, int demand) {
        this(vehicleId, capacity, demand, "Vehicle '%s' exceeded its max capacity by %s."
                .formatted(vehicleId, demand - capacity));
    }
}
