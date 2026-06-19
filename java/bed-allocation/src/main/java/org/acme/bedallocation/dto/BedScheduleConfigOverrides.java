package org.acme.bedallocation.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.bedallocation.solver.BedScheduleConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of soft constraint weights. Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record BedScheduleConfigOverrides(
        @ConstraintReference(BedScheduleConstraintProvider.PREFERRED_MAXIMUM_ROOM_CAPACITY) @Schema(
                description = "Soft weight of the preferred maximum room capacity constraint.") long preferredMaximumRoomCapacityWeight,
        @ConstraintReference(BedScheduleConstraintProvider.DEPARTMENT_SPECIALTY) @Schema(
                description = "Soft weight of the department specialty constraint.") long departmentSpecialtyWeight,
        @ConstraintReference(BedScheduleConstraintProvider.DEPARTMENT_SPECIALTY_NOT_FIRST_PRIORITY) @Schema(
                description = "Soft weight of the department specialty not first priority constraint.") long departmentSpecialtyNotFirstPriorityWeight,
        @ConstraintReference(BedScheduleConstraintProvider.PREFERRED_PATIENT_EQUIPMENT) @Schema(
                description = "Soft weight of the preferred patient equipment constraint.") long preferredPatientEquipmentWeight)
        implements
            ModelConfigOverrides {

    public BedScheduleConfigOverrides {
        preferredMaximumRoomCapacityWeight = Math.max(0L, preferredMaximumRoomCapacityWeight);
        departmentSpecialtyWeight = Math.max(0L, departmentSpecialtyWeight);
        departmentSpecialtyNotFirstPriorityWeight = Math.max(0L, departmentSpecialtyNotFirstPriorityWeight);
        preferredPatientEquipmentWeight = Math.max(0L, preferredPatientEquipmentWeight);
    }

    public BedScheduleConfigOverrides() {
        this(8L, 10L, 10L, 50L);
    }

    public BedScheduleConfigOverrides withPreferredMaximumRoomCapacityWeight(long preferredMaximumRoomCapacityWeight) {
        return new BedScheduleConfigOverrides(preferredMaximumRoomCapacityWeight, departmentSpecialtyWeight,
                departmentSpecialtyNotFirstPriorityWeight, preferredPatientEquipmentWeight);
    }

    public BedScheduleConfigOverrides withDepartmentSpecialtyWeight(long departmentSpecialtyWeight) {
        return new BedScheduleConfigOverrides(preferredMaximumRoomCapacityWeight, departmentSpecialtyWeight,
                departmentSpecialtyNotFirstPriorityWeight, preferredPatientEquipmentWeight);
    }

    public BedScheduleConfigOverrides withDepartmentSpecialtyNotFirstPriorityWeight(
            long departmentSpecialtyNotFirstPriorityWeight) {
        return new BedScheduleConfigOverrides(preferredMaximumRoomCapacityWeight, departmentSpecialtyWeight,
                departmentSpecialtyNotFirstPriorityWeight, preferredPatientEquipmentWeight);
    }

    public BedScheduleConfigOverrides withPreferredPatientEquipmentWeight(long preferredPatientEquipmentWeight) {
        return new BedScheduleConfigOverrides(preferredMaximumRoomCapacityWeight, departmentSpecialtyWeight,
                departmentSpecialtyNotFirstPriorityWeight, preferredPatientEquipmentWeight);
    }
}
