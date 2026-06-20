package org.acme.foodpackaging.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.foodpackaging.solver.FoodPackagingConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of constraint weights. Every constraint has a default weight of 1, meaning that all constraints are equally important. "
        + "Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record PackagingScheduleConfigOverrides(
        @ConstraintReference(FoodPackagingConstraintProvider.IDEAL_END_DATE_TIME) @Schema(
                description = "Medium weight of the ideal end date time constraint.") long idealEndDateTimeWeight,
        @ConstraintReference(FoodPackagingConstraintProvider.MAXIMIZE_JOBS_ASSIGNED) @Schema(
                description = "Medium weight of the maximize jobs assigned constraint.") long maximizeJobsAssignedWeight,
        @ConstraintReference(FoodPackagingConstraintProvider.MINIMIZE_MAKESPAN) @Schema(
                description = "Soft weight of the minimize makespan constraint.") long minimizeMakespanWeight)
        implements
            ModelConfigOverrides {

    public PackagingScheduleConfigOverrides {
        idealEndDateTimeWeight = Math.max(0L, idealEndDateTimeWeight);
        maximizeJobsAssignedWeight = Math.max(0L, maximizeJobsAssignedWeight);
        minimizeMakespanWeight = Math.max(0L, minimizeMakespanWeight);
    }

    public PackagingScheduleConfigOverrides() {
        this(1L, 1L, 1L);
    }

    public PackagingScheduleConfigOverrides withIdealEndDateTimeWeight(long idealEndDateTimeWeight) {
        return new PackagingScheduleConfigOverrides(idealEndDateTimeWeight, maximizeJobsAssignedWeight,
                minimizeMakespanWeight);
    }

    public PackagingScheduleConfigOverrides withMaximizeJobsAssignedWeight(long maximizeJobsAssignedWeight) {
        return new PackagingScheduleConfigOverrides(idealEndDateTimeWeight, maximizeJobsAssignedWeight,
                minimizeMakespanWeight);
    }

    public PackagingScheduleConfigOverrides withMinimizeMakespanWeight(long minimizeMakespanWeight) {
        return new PackagingScheduleConfigOverrides(idealEndDateTimeWeight, maximizeJobsAssignedWeight,
                minimizeMakespanWeight);
    }
}
