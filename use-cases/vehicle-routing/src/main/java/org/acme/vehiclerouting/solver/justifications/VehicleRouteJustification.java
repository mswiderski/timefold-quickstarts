package org.acme.vehiclerouting.solver.justifications;

import ai.timefold.models.sdk.api.ModelConstraintJustification;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(oneOf = {
        MinimizeTravelTimeJustification.class,
        VehicleCapacityJustification.class,
        ServiceFinishedAfterMaxEndTimeJustification.class
})
public interface VehicleRouteJustification extends ModelConstraintJustification {

    String DESCRIPTION_FIELD = "description";
}
