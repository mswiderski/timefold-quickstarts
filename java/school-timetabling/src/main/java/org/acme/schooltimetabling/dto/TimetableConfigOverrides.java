package org.acme.schooltimetabling.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.schooltimetabling.solver.TimetableConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of soft constraint weights. Every constraint has a default weight of 1, meaning that all constraints are equally important. "
        + "Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record TimetableConfigOverrides(
        @ConstraintReference(TimetableConstraintProvider.TEACHER_ROOM_STABILITY) @Schema(
                description = "Soft weight of the teacher room stability constraint.") long teacherRoomStabilityWeight,
        @ConstraintReference(TimetableConstraintProvider.TEACHER_TIME_EFFICIENCY) @Schema(
                description = "Soft weight of the teacher time efficiency constraint.") long teacherTimeEfficiencyWeight,
        @ConstraintReference(TimetableConstraintProvider.STUDENT_GROUP_SUBJECT_VARIETY) @Schema(
                description = "Soft weight of the student group subject variety constraint.") long studentGroupSubjectVarietyWeight)
        implements
            ModelConfigOverrides {

    public TimetableConfigOverrides {
        teacherRoomStabilityWeight = Math.max(0L, teacherRoomStabilityWeight);
        teacherTimeEfficiencyWeight = Math.max(0L, teacherTimeEfficiencyWeight);
        studentGroupSubjectVarietyWeight = Math.max(0L, studentGroupSubjectVarietyWeight);
    }

    public TimetableConfigOverrides() {
        this(1L, 1L, 1L);
    }

    public TimetableConfigOverrides withTeacherRoomStabilityWeight(long teacherRoomStabilityWeight) {
        return new TimetableConfigOverrides(teacherRoomStabilityWeight, teacherTimeEfficiencyWeight,
                studentGroupSubjectVarietyWeight);
    }

    public TimetableConfigOverrides withTeacherTimeEfficiencyWeight(long teacherTimeEfficiencyWeight) {
        return new TimetableConfigOverrides(teacherRoomStabilityWeight, teacherTimeEfficiencyWeight,
                studentGroupSubjectVarietyWeight);
    }

    public TimetableConfigOverrides withStudentGroupSubjectVarietyWeight(long studentGroupSubjectVarietyWeight) {
        return new TimetableConfigOverrides(teacherRoomStabilityWeight, teacherTimeEfficiencyWeight,
                studentGroupSubjectVarietyWeight);
    }
}
