package org.acme.bedallocation.solver;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.sdk.core.api.ModelConvertor;
import ai.timefold.sdk.core.api.domain.ModelConfig;
import ai.timefold.sdk.quarkus.deployment.defaults.EmptyModelConfigOverrides;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import org.acme.bedallocation.domain.BedAllocationKpis;
import org.acme.bedallocation.domain.BedPlan;

@ApplicationScoped
public class BedPlanModelConvertor implements
        ModelConvertor<HardMediumSoftScore, BedPlan, BedPlan, EmptyModelConfigOverrides, BedAllocationKpis, BedPlan> {



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
