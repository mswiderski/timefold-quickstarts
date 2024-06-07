package org.acme.vehiclerouting.solver;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.solver.RecommendedFit;
import ai.timefold.solver.core.api.solver.SolutionManager;

import org.acme.vehiclerouting.domain.Vehicle;
import org.acme.vehiclerouting.domain.VehicleRoutePlan;
import org.acme.vehiclerouting.domain.Visit;
import org.acme.vehiclerouting.domain.dto.ApplyRecommendationRequest;
import org.acme.vehiclerouting.domain.dto.RecommendationRequest;
import org.acme.vehiclerouting.domain.dto.VehicleRecommendation;

import io.quarkus.arc.Unremovable;

@Unremovable
@ApplicationScoped
public class VehicleRouteRecommendationService {

    static final int MAX_RECOMMENDED_FIT_LIST_SIZE = 5;

    private SolutionManager<VehicleRoutePlan, HardSoftLongScore> solutionManager;

    @Inject
    public VehicleRouteRecommendationService(SolutionManager<VehicleRoutePlan, HardSoftLongScore> solutionManager) {
        this.solutionManager = solutionManager;
    }

    public List<RecommendedFit<VehicleRecommendation, HardSoftLongScore>> recommendedFit(RecommendationRequest request) {
        Visit visit = request.solution().getVisits().stream()
                .filter(v -> v.getId().equals(request.visitId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Visit %s not found".formatted(request.visitId())));
        List<RecommendedFit<VehicleRecommendation, HardSoftLongScore>> recommendedFitList = solutionManager
                .recommendFit(request.solution(), visit, v -> new VehicleRecommendation(v.getVehicle().getId(),
                        v.getVehicle().getVisits().indexOf(v)));
        if (!recommendedFitList.isEmpty()) {
            return recommendedFitList.subList(0, Math.min(MAX_RECOMMENDED_FIT_LIST_SIZE, recommendedFitList.size()));
        }

        return recommendedFitList;
    }

    public VehicleRoutePlan applyRecommendedFit(ApplyRecommendationRequest request) {
        VehicleRoutePlan updatedSolution = request.solution();
        String vehicleId = request.vehicleId();
        Vehicle vehicleTarget = updatedSolution.getVehicles().stream()
                .filter(v -> v.getId().equals(vehicleId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Vehicle %s not found".formatted(vehicleId)));
        Visit visit = request.solution().getVisits().stream()
                .filter(v -> v.getId().equals(request.visitId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Visit %s not found".formatted(request.visitId())));
        vehicleTarget.getVisits().add(request.index(), visit);
        solutionManager.update(updatedSolution);
        return updatedSolution;
    }
}
