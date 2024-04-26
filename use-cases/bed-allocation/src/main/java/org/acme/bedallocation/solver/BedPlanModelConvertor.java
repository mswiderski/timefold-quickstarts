package org.acme.bedallocation.solver;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.models.sdk.api.ModelConvertor;
import ai.timefold.models.sdk.api.domain.ModelConfig;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import org.acme.bedallocation.domain.BedPlan;
import org.acme.bedallocation.model.BedPlanKpis;
import org.acme.bedallocation.model.BedPlanModelConstraintJustification;

@ApplicationScoped
public class BedPlanModelConvertor implements
        ModelConvertor<HardMediumSoftScore, BedPlan, BedPlan, BedPlanModelConstraintJustification, BedPlanKpis, BedPlan> {



    @Override
    public BedPlan toModelOutput(BedPlan solverModel) {
        return solverModel;
    }

    @Override
    public BedPlan toSolverModel(BedPlan modelInput, BedPlan previousModelOutput,
            ModelConfig<BedPlanModelConstraintJustification> modelConfig) {
        return previousModelOutput != null ? previousModelOutput : modelInput;
    }

    @Override
    public BedPlan toSolverModel(BedPlan modelInput, ModelConfig<BedPlanModelConstraintJustification> modelConfig) {
        return modelInput;
    }

}
