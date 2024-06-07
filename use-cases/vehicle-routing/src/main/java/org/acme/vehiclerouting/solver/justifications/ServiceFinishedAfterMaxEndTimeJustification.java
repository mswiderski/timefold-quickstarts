package org.acme.vehiclerouting.solver.justifications;

public record ServiceFinishedAfterMaxEndTimeJustification(String visitId, long serviceFinishedDelayInMinutes,
        String description) implements VehicleRouteJustification {

    public ServiceFinishedAfterMaxEndTimeJustification(String visitId, long serviceFinishedDelayInMinutes) {
        this(visitId, serviceFinishedDelayInMinutes, "Visit '%s' serviced with a %s-minute delay."
                .formatted(visitId, serviceFinishedDelayInMinutes));
    }
}
