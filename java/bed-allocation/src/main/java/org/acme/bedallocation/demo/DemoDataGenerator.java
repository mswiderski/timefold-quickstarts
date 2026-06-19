package org.acme.bedallocation.demo;

import java.time.Duration;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.solver.service.definition.api.data.AbstractBasicDemoDataGenerator;
import ai.timefold.solver.service.definition.api.domain.Configuration;
import ai.timefold.solver.service.definition.api.domain.ModelConfig;
import ai.timefold.solver.service.definition.api.domain.ModelRequest;
import ai.timefold.solver.service.definition.api.domain.RunConfiguration;
import ai.timefold.solver.service.definition.api.termination.SolverTerminationConfig;

import org.acme.bedallocation.dto.BedScheduleConfigOverrides;
import org.acme.bedallocation.dto.BedScheduleInput;

@ApplicationScoped
public class DemoDataGenerator
        extends AbstractBasicDemoDataGenerator<BedScheduleInput, BedScheduleConfigOverrides> {

    @Override
    protected ModelRequest<BedScheduleInput, BedScheduleConfigOverrides> generateBasicDemoDataRequest() {
        BedScheduleInput problem = DemoDataBuilder.builder().build();
        RunConfiguration runConfiguration = new RunConfiguration("BASIC",
                new SolverTerminationConfig(Duration.ofSeconds(30), null));
        Configuration<BedScheduleConfigOverrides> configuration = new Configuration<>(
                runConfiguration, new ModelConfig<>(new BedScheduleConfigOverrides()));
        return new ModelRequest<>(configuration, problem);
    }
}
