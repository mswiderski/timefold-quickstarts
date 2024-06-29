package org.acme.bedallocation.domain;

import java.util.List;

import ai.timefold.models.sdk.api.ModelInput;
import ai.timefold.models.sdk.api.ModelOutput;
import ai.timefold.models.sdk.api.SolverModel;
import ai.timefold.quarkus.models.sdk.defaults.EmptyModelKpi;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@PlanningSolution
public class BedPlan implements ModelInput, ModelOutput, SolverModel<HardMediumSoftScore, EmptyModelKpi> {

    @ProblemFactCollectionProperty
    private List<Department> departments;
    @PlanningEntityCollectionProperty
    private List<Stay> stays;
    @JsonIgnore
    private List<Room> rooms;
    @JsonIgnore
    private List<Bed> beds;

    @JsonIgnore
    @Schema(hidden = true)
    @PlanningScore
    private HardMediumSoftScore score;

    // No-arg constructor required for Timefold
    public BedPlan() {
    }

    @JsonCreator
    public BedPlan(@JsonProperty("departments") List<Department> departments, @JsonProperty("stays") List<Stay> stays) {
        this.departments = departments;
        this.stays = stays;
        this.rooms = departments.stream()
                .filter(d -> d.getRooms() != null)
                .flatMap(d -> d.getRooms().stream())
                .toList();
        this.beds = departments.stream()
                .filter(d -> d.getRooms() != null)
                .flatMap(d -> d.getRooms().stream())
                .flatMap(r -> r.getBeds().stream())
                .toList();
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************
    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    @ProblemFactCollectionProperty
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }

    public List<Stay> getStays() {
        return stays;
    }

    public void setStays(List<Stay> stays) {
        this.stays = stays;
    }

    public HardMediumSoftScore getScore() {
        return score;
    }

    public void setScore(HardMediumSoftScore score) {
        this.score = score;
    }

    @JsonIgnore
    @Override
    public EmptyModelKpi getKpis() {
        return new EmptyModelKpi();
    }

}
