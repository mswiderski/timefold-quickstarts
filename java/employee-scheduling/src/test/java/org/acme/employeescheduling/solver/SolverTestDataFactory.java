package org.acme.employeescheduling.solver;

import org.acme.employeescheduling.demo.DemoDataBuilder;
import org.acme.employeescheduling.dto.EmployeeScheduleInput;

public final class SolverTestDataFactory {

    private SolverTestDataFactory() {
    }

    public static EmployeeScheduleInput createProblem() {
        return DemoDataBuilder.builder()
                .setDaysInSchedule(7)
                .setEmployeeCount(5)
                .setRandomSeed(42L)
                .build();
    }
}
