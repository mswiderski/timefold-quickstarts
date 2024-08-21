package org.acme.bedallocation.domain;

import ai.timefold.sdk.core.api.KpiDataFormat;
import ai.timefold.sdk.core.api.ModelKpis;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.extensions.Extension;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class BedAllocationKpis implements ModelKpis {
    @Schema(name = "unassignedShifts", title = "Unassigned Shifts",
            description = "The number of shifts that could not be assigned an employee in this schedule.",
            type = SchemaType.INTEGER, format = KpiDataFormat.Values.NUMBER, example = "10",
            extensions = { @Extension(name = "x-tf-priority", value = "1") })
    private int unassignedShifts;

    public int getUnassignedShifts() {
        return unassignedShifts;
    }

    public void setUnassignedShifts(int unassignedShifts) {
        this.unassignedShifts = unassignedShifts;
    }
}
