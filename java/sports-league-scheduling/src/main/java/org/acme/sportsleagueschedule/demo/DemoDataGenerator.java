package org.acme.sportsleagueschedule.demo;

import java.time.Duration;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.solver.service.definition.api.data.AbstractBasicDemoDataGenerator;
import ai.timefold.solver.service.definition.api.domain.Configuration;
import ai.timefold.solver.service.definition.api.domain.ModelConfig;
import ai.timefold.solver.service.definition.api.domain.ModelRequest;
import ai.timefold.solver.service.definition.api.domain.RunConfiguration;
import ai.timefold.solver.service.definition.api.termination.SolverTerminationConfig;

import org.acme.sportsleagueschedule.dto.LeagueScheduleConfigOverrides;
import org.acme.sportsleagueschedule.dto.LeagueScheduleInput;

@ApplicationScoped
public class DemoDataGenerator
        extends AbstractBasicDemoDataGenerator<LeagueScheduleInput, LeagueScheduleConfigOverrides> {

    @Override
    protected ModelRequest<LeagueScheduleInput, LeagueScheduleConfigOverrides> generateBasicDemoDataRequest() {
        LeagueScheduleInput problem = DemoDataBuilder.builder().build();
        RunConfiguration runConfiguration = new RunConfiguration("BASIC",
                new SolverTerminationConfig(Duration.ofSeconds(30), null));
        Configuration<LeagueScheduleConfigOverrides> configuration = new Configuration<>(
                runConfiguration, new ModelConfig<>(new LeagueScheduleConfigOverrides()));
        return new ModelRequest<>(configuration, problem);
    }
}
