package org.acme.facilitylocation.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.facilitylocation.solver.FacilityLocationConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of soft constraint weights. Every constraint has a default weight of 1, meaning that all constraints are equally important. "
        + "Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record FacilityLocationConfigOverrides(
        @ConstraintReference(FacilityLocationConstraintProvider.FACILITY_SETUP_COST) @Schema(
                description = "Soft weight of the facility setup cost constraint.") long setupCostWeight,
        @ConstraintReference(FacilityLocationConstraintProvider.DISTANCE_FROM_FACILITY) @Schema(
                description = "Soft weight of the distance from facility constraint.") long distanceFromFacilityWeight)
        implements
            ModelConfigOverrides {

    public FacilityLocationConfigOverrides {
        setupCostWeight = Math.max(0L, setupCostWeight);
        distanceFromFacilityWeight = Math.max(0L, distanceFromFacilityWeight);
    }

    public FacilityLocationConfigOverrides() {
        this(1L, 1L);
    }

    public FacilityLocationConfigOverrides withSetupCostWeight(long setupCostWeight) {
        return new FacilityLocationConfigOverrides(setupCostWeight, distanceFromFacilityWeight);
    }

    public FacilityLocationConfigOverrides withDistanceFromFacilityWeight(long distanceFromFacilityWeight) {
        return new FacilityLocationConfigOverrides(setupCostWeight, distanceFromFacilityWeight);
    }
}
