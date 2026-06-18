package org.acme.employeescheduling.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.employeescheduling.solver.EmployeeSchedulingConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of soft constraint weights. Every constraint has a default weight of 1, meaning that all constraints are equally important. "
        + "Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record EmployeeScheduleConfigOverrides(
        @ConstraintReference(EmployeeSchedulingConstraintProvider.UNDESIRED_DAY_FOR_EMPLOYEE) @Schema(
                description = "Soft weight of the undesired day for employee constraint.") long undesiredDayForEmployeeWeight,
        @ConstraintReference(EmployeeSchedulingConstraintProvider.DESIRED_DAY_FOR_EMPLOYEE) @Schema(
                description = "Soft weight of the desired day for employee constraint.") long desiredDayForEmployeeWeight,
        @ConstraintReference(EmployeeSchedulingConstraintProvider.BALANCE_EMPLOYEE_SHIFT_ASSIGNMENTS) @Schema(
                description = "Soft weight of the balance employee shift assignments constraint.") long balanceEmployeeShiftAssignmentsWeight)
        implements
            ModelConfigOverrides {

    public EmployeeScheduleConfigOverrides {
        undesiredDayForEmployeeWeight = Math.max(0L, undesiredDayForEmployeeWeight);
        desiredDayForEmployeeWeight = Math.max(0L, desiredDayForEmployeeWeight);
        balanceEmployeeShiftAssignmentsWeight = Math.max(0L, balanceEmployeeShiftAssignmentsWeight);
    }

    public EmployeeScheduleConfigOverrides() {
        this(1L, 1L, 1L);
    }

    public EmployeeScheduleConfigOverrides withUndesiredDayForEmployeeWeight(long undesiredDayForEmployeeWeight) {
        return new EmployeeScheduleConfigOverrides(undesiredDayForEmployeeWeight, desiredDayForEmployeeWeight,
                balanceEmployeeShiftAssignmentsWeight);
    }

    public EmployeeScheduleConfigOverrides withDesiredDayForEmployeeWeight(long desiredDayForEmployeeWeight) {
        return new EmployeeScheduleConfigOverrides(undesiredDayForEmployeeWeight, desiredDayForEmployeeWeight,
                balanceEmployeeShiftAssignmentsWeight);
    }

    public EmployeeScheduleConfigOverrides withBalanceEmployeeShiftAssignmentsWeight(
            long balanceEmployeeShiftAssignmentsWeight) {
        return new EmployeeScheduleConfigOverrides(undesiredDayForEmployeeWeight, desiredDayForEmployeeWeight,
                balanceEmployeeShiftAssignmentsWeight);
    }
}
