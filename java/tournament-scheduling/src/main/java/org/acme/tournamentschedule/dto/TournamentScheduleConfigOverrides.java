package org.acme.tournamentschedule.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.tournamentschedule.solver.TournamentScheduleConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of soft constraint weights. Every constraint has a default weight of 1, meaning that all constraints are equally important. "
        + "Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record TournamentScheduleConfigOverrides(
        @ConstraintReference(TournamentScheduleConstraintProvider.FAIR_ASSIGNMENT_COUNT_PER_TEAM) @Schema(
                description = "Weight of the fair assignment count per team constraint.") long fairAssignmentCountPerTeamWeight,
        @ConstraintReference(TournamentScheduleConstraintProvider.EVENLY_CONFRONTATION_COUNT) @Schema(
                description = "Weight of the evenly confrontation count constraint.") long evenlyConfrontationCountWeight)
        implements
            ModelConfigOverrides {

    public TournamentScheduleConfigOverrides {
        fairAssignmentCountPerTeamWeight = Math.max(0L, fairAssignmentCountPerTeamWeight);
        evenlyConfrontationCountWeight = Math.max(0L, evenlyConfrontationCountWeight);
    }

    public TournamentScheduleConfigOverrides() {
        this(1L, 1L);
    }

    public TournamentScheduleConfigOverrides withFairAssignmentCountPerTeamWeight(long fairAssignmentCountPerTeamWeight) {
        return new TournamentScheduleConfigOverrides(fairAssignmentCountPerTeamWeight, evenlyConfrontationCountWeight);
    }

    public TournamentScheduleConfigOverrides withEvenlyConfrontationCountWeight(long evenlyConfrontationCountWeight) {
        return new TournamentScheduleConfigOverrides(fairAssignmentCountPerTeamWeight, evenlyConfrontationCountWeight);
    }
}
