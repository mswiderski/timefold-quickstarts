package org.acme.vehiclerouting.solver;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.sdk.core.api.ModelConvertor;
import ai.timefold.sdk.core.api.domain.ModelConfig;
import ai.timefold.sdk.quarkus.deployment.defaults.EmptyModelConfigOverrides;
import ai.timefold.sdk.quarkus.deployment.defaults.EmptyModelKpi;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import org.acme.vehiclerouting.domain.VehicleRoutePlan;

@ApplicationScoped
public class VehicleRoutePlanModelConvertor implements
        ModelConvertor<HardSoftLongScore, VehicleRoutePlan, VehicleRoutePlan, EmptyModelConfigOverrides, EmptyModelKpi, VehicleRoutePlan> {



    @Override
    public VehicleRoutePlan toModelOutput(VehicleRoutePlan solverModel) {
        return solverModel;
    }

    @Override
    public VehicleRoutePlan toSolverModel(VehicleRoutePlan modelInput, VehicleRoutePlan previousModelOutput,
            ModelConfig<EmptyModelConfigOverrides> modelConfig) {
        return previousModelOutput != null ? previousModelOutput : modelInput;
    }

    @Override
    public VehicleRoutePlan toSolverModel(VehicleRoutePlan modelInput, ModelConfig<EmptyModelConfigOverrides> modelConfig) {
        return modelInput;
    }

}
