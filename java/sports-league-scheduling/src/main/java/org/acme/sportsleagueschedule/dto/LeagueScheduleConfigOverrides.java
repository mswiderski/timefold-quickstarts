package org.acme.sportsleagueschedule.dto;

import ai.timefold.solver.service.definition.api.ModelConfigOverrides;
import ai.timefold.solver.service.definition.api.domain.ConstraintReference;

import org.acme.sportsleagueschedule.solver.SportsLeagueSchedulingConstraintProvider;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Definition of soft constraint weights. Use this to express preference of some constraints over others. "
        + "In order to turn off a constraint, set the value of the corresponding attribute to 0.")
public record LeagueScheduleConfigOverrides(
        @ConstraintReference(SportsLeagueSchedulingConstraintProvider.START_TO_AWAY_HOP) @Schema(
                description = "Soft weight of the start to away hop constraint.") long startToAwayHopWeight,
        @ConstraintReference(SportsLeagueSchedulingConstraintProvider.HOME_TO_AWAY_HOP) @Schema(
                description = "Soft weight of the home to away hop constraint.") long homeToAwayHopWeight,
        @ConstraintReference(SportsLeagueSchedulingConstraintProvider.AWAY_TO_AWAY_HOP) @Schema(
                description = "Soft weight of the away to away hop constraint.") long awayToAwayHopWeight,
        @ConstraintReference(SportsLeagueSchedulingConstraintProvider.AWAY_TO_HOME_HOP) @Schema(
                description = "Soft weight of the away to home hop constraint.") long awayToHomeHopWeight,
        @ConstraintReference(SportsLeagueSchedulingConstraintProvider.AWAY_TO_END_HOP) @Schema(
                description = "Soft weight of the away to end hop constraint.") long awayToEndHopWeight,
        @ConstraintReference(SportsLeagueSchedulingConstraintProvider.CLASSIC_MATCHES) @Schema(
                description = "Soft weight of the classic matches constraint.") long classicMatchesWeight)
        implements
            ModelConfigOverrides {

    public LeagueScheduleConfigOverrides {
        startToAwayHopWeight = Math.max(0L, startToAwayHopWeight);
        homeToAwayHopWeight = Math.max(0L, homeToAwayHopWeight);
        awayToAwayHopWeight = Math.max(0L, awayToAwayHopWeight);
        awayToHomeHopWeight = Math.max(0L, awayToHomeHopWeight);
        awayToEndHopWeight = Math.max(0L, awayToEndHopWeight);
        classicMatchesWeight = Math.max(0L, classicMatchesWeight);
    }

    public LeagueScheduleConfigOverrides() {
        this(1L, 1L, 1L, 1L, 1L, 1000L);
    }

    public LeagueScheduleConfigOverrides withStartToAwayHopWeight(long startToAwayHopWeight) {
        return new LeagueScheduleConfigOverrides(startToAwayHopWeight, homeToAwayHopWeight, awayToAwayHopWeight,
                awayToHomeHopWeight, awayToEndHopWeight, classicMatchesWeight);
    }

    public LeagueScheduleConfigOverrides withHomeToAwayHopWeight(long homeToAwayHopWeight) {
        return new LeagueScheduleConfigOverrides(startToAwayHopWeight, homeToAwayHopWeight, awayToAwayHopWeight,
                awayToHomeHopWeight, awayToEndHopWeight, classicMatchesWeight);
    }

    public LeagueScheduleConfigOverrides withAwayToAwayHopWeight(long awayToAwayHopWeight) {
        return new LeagueScheduleConfigOverrides(startToAwayHopWeight, homeToAwayHopWeight, awayToAwayHopWeight,
                awayToHomeHopWeight, awayToEndHopWeight, classicMatchesWeight);
    }

    public LeagueScheduleConfigOverrides withAwayToHomeHopWeight(long awayToHomeHopWeight) {
        return new LeagueScheduleConfigOverrides(startToAwayHopWeight, homeToAwayHopWeight, awayToAwayHopWeight,
                awayToHomeHopWeight, awayToEndHopWeight, classicMatchesWeight);
    }

    public LeagueScheduleConfigOverrides withAwayToEndHopWeight(long awayToEndHopWeight) {
        return new LeagueScheduleConfigOverrides(startToAwayHopWeight, homeToAwayHopWeight, awayToAwayHopWeight,
                awayToHomeHopWeight, awayToEndHopWeight, classicMatchesWeight);
    }

    public LeagueScheduleConfigOverrides withClassicMatchesWeight(long classicMatchesWeight) {
        return new LeagueScheduleConfigOverrides(startToAwayHopWeight, homeToAwayHopWeight, awayToAwayHopWeight,
                awayToHomeHopWeight, awayToEndHopWeight, classicMatchesWeight);
    }
}
