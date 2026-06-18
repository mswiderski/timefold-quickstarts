package org.acme.maintenancescheduling.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.maintenancescheduling.solver.MaintenanceScheduleConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of soft constraint weights. Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record MaintenanceScheduleConfigOverrides(
        @ConstraintReference(MaintenanceScheduleConstraintProvider.BEFORE_IDEAL_END_DATE) @Schema(
                description = "Soft weight of the before ideal end date constraint.") long beforeIdealEndDateWeight,
        @ConstraintReference(MaintenanceScheduleConstraintProvider.AFTER_IDEAL_END_DATE) @Schema(
                description = "Soft weight of the after ideal end date constraint.") long afterIdealEndDateWeight,
        @ConstraintReference(MaintenanceScheduleConstraintProvider.TAG_CONFLICT) @Schema(
                description = "Soft weight of the tag conflict constraint.") long tagConflictWeight)
        implements
            ModelConfigOverrides {

    public MaintenanceScheduleConfigOverrides {
        beforeIdealEndDateWeight = Math.max(0L, beforeIdealEndDateWeight);
        afterIdealEndDateWeight = Math.max(0L, afterIdealEndDateWeight);
        tagConflictWeight = Math.max(0L, tagConflictWeight);
    }

    public MaintenanceScheduleConfigOverrides() {
        this(1L, 1_000_000L, 1_000L);
    }

    public MaintenanceScheduleConfigOverrides withBeforeIdealEndDateWeight(long beforeIdealEndDateWeight) {
        return new MaintenanceScheduleConfigOverrides(beforeIdealEndDateWeight, afterIdealEndDateWeight, tagConflictWeight);
    }

    public MaintenanceScheduleConfigOverrides withAfterIdealEndDateWeight(long afterIdealEndDateWeight) {
        return new MaintenanceScheduleConfigOverrides(beforeIdealEndDateWeight, afterIdealEndDateWeight, tagConflictWeight);
    }

    public MaintenanceScheduleConfigOverrides withTagConflictWeight(long tagConflictWeight) {
        return new MaintenanceScheduleConfigOverrides(beforeIdealEndDateWeight, afterIdealEndDateWeight, tagConflictWeight);
    }
}
