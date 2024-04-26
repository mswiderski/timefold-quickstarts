package org.acme.bedallocation.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.Path;

import ai.timefold.models.sdk.api.events.ItemStartCommand;
import ai.timefold.models.sdk.api.events.ItemTerminateCommand;
import ai.timefold.models.sdk.api.events.SolverChannels;
import ai.timefold.models.sdk.api.storage.AbstractStorageService;
import ai.timefold.models.sdk.rest.AbstractModelAPIResource;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import org.acme.bedallocation.analysis.BedPlanJustification;
import org.acme.bedallocation.analysis.BedPlanScoreAnalysisService;
import org.acme.bedallocation.domain.BedPlan;
import org.acme.bedallocation.model.BedPlanConstraintConfiguration;
import org.acme.bedallocation.model.BedPlanKpis;
import org.acme.bedallocation.solver.BedPlanModelConvertor;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Tag(name = "Bed allocation",
        description = "Timefold Bed allocation service - Assign beds based on equipment available")
@Path("/v1/bed-allocations")
public class BedPlanningResource extends
        AbstractModelAPIResource<BedPlan, BedPlan, BedPlanConstraintConfiguration, HardMediumSoftScore, BedPlanKpis, BedPlanJustification> {

    @Inject
    public BedPlanningResource(BedPlanScoreAnalysisService scoreAnalysisService,
            AbstractStorageService<BedPlan, BedPlanConstraintConfiguration, BedPlanKpis, BedPlan, HardMediumSoftScore, BedPlanJustification> storageService,
            BedPlanModelConvertor modelConvertor,
            @Channel(SolverChannels.START) Emitter<ItemStartCommand> scheduleStartEmitter,
            @Channel(SolverChannels.TERMINATE) Emitter<ItemTerminateCommand> scheduleTerminateEmitter) {
        super(scoreAnalysisService, storageService, modelConvertor, scheduleStartEmitter, scheduleTerminateEmitter);
    }

}
