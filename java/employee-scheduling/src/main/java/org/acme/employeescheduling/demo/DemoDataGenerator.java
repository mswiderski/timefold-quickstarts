package org.acme.employeescheduling.demo;

import jakarta.enterprise.context.ApplicationScoped;

import ai.timefold.solver.service.definition.api.data.AbstractBasicDemoDataGenerator;
import ai.timefold.solver.service.definition.api.domain.Configuration;
import ai.timefold.solver.service.definition.api.domain.ModelConfig;
import ai.timefold.solver.service.definition.api.domain.ModelRequest;
import ai.timefold.solver.service.definition.api.domain.RunConfiguration;

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
        Configuration<EmployeeScheduleConfigOverrides> configuration = new Configuration<>(
                new RunConfiguration("BASIC"), new ModelConfig<>(new EmployeeScheduleConfigOverrides()));
        return new ModelRequest<>(configuration, problem);
    }
}
