package org.acme.bedallocation.solver;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.models.sdk.api.ModelConvertor;
import ai.timefold.models.sdk.api.domain.ModelConfig;
import ai.timefold.quarkus.models.sdk.defaults.EmptyModelConfigOverrides;
import ai.timefold.quarkus.models.sdk.defaults.EmptyModelKpi;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import org.acme.bedallocation.domain.BedPlan;

@ApplicationScoped
public class BedPlanModelConvertor implements
        ModelConvertor<HardMediumSoftScore, BedPlan, BedPlan, EmptyModelConfigOverrides, EmptyModelKpi, BedPlan> {



    @Override
    public BedPlan toModelOutput(BedPlan solverModel) {
        return solverModel;
    }

    @Override
    public BedPlan toSolverModel(BedPlan modelInput, BedPlan previousModelOutput,
            ModelConfig<EmptyModelConfigOverrides> modelConfig) {
        return previousModelOutput != null ? previousModelOutput : modelInput;
    }

    @Override
    public BedPlan toSolverModel(BedPlan modelInput, ModelConfig<EmptyModelConfigOverrides> modelConfig) {
        return modelInput;
    }

}
