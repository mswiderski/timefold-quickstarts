package org.acme.bedallocation.analysis;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import ai.timefold.models.sdk.api.SolverModelEnricherService;
import ai.timefold.models.sdk.api.SolverModelEnrichmentDirectorService;
import ai.timefold.models.sdk.solver.analysis.AbstractScoreAnalysisService;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import ai.timefold.solver.core.api.solver.SolutionManager;

import org.acme.bedallocation.domain.BedPlan;
import org.acme.bedallocation.model.BedPlanKpis;
import org.acme.bedallocation.model.BedPlanModelConstraintJustification;
import org.acme.bedallocation.solver.BedPlanModelConvertor;

@ApplicationScoped
public class BedPlanScoreAnalysisService
        extends
        AbstractScoreAnalysisService<HardMediumSoftScore, BedPlanJustification, BedPlanKpis, BedPlan, BedPlan, BedPlan, BedPlanModelConstraintJustification>
    {

    BedPlanScoreAnalysisService() {
        super(null, null, null, null);
    }

    @Inject
    public BedPlanScoreAnalysisService(
            SolutionManager<BedPlan, HardMediumSoftScore> solutionManager,
            SolverModelEnrichmentDirectorService modelEnrichmentDirectorService,
            SolverModelEnricherService solverModelEnricherService,
            BedPlanModelConvertor modelConvertor) {
        super(solutionManager, solverModelEnricherService, modelEnrichmentDirectorService, modelConvertor);
    }

    @Override
    public boolean supportsJustifications() {
        return false;
    }
}
