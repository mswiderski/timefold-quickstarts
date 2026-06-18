package org.acme.employeescheduling.demo;

import java.time.Duration;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.solver.service.definition.api.data.AbstractBasicDemoDataGenerator;
import ai.timefold.solver.service.definition.api.domain.Configuration;
import ai.timefold.solver.service.definition.api.domain.ModelConfig;
import ai.timefold.solver.service.definition.api.domain.ModelRequest;
import ai.timefold.solver.service.definition.api.domain.RunConfiguration;
import ai.timefold.solver.service.definition.api.termination.SolverTerminationConfig;

import org.acme.employeescheduling.dto.EmployeeScheduleConfigOverrides;
import org.acme.employeescheduling.dto.EmployeeScheduleInput;

@ApplicationScoped
public class DemoDataGenerator
        extends AbstractBasicDemoDataGenerator<EmployeeScheduleInput, EmployeeScheduleConfigOverrides> {

    @Override
    protected ModelRequest<EmployeeScheduleInput, EmployeeScheduleConfigOverrides> generateBasicDemoDataRequest() {
        EmployeeScheduleInput problem = DemoDataBuilder.builder()
                .setDaysInSchedule(14)
                .setEmployeeCount(15)
                .build();
        RunConfiguration runConfiguration = new RunConfiguration("BASIC",
                new SolverTerminationConfig(Duration.ofSeconds(30), null));
        Configuration<EmployeeScheduleConfigOverrides> configuration = new Configuration<>(
                runConfiguration, new ModelConfig<>(new EmployeeScheduleConfigOverrides()));
        return new ModelRequest<>(configuration, problem);
    }
}
