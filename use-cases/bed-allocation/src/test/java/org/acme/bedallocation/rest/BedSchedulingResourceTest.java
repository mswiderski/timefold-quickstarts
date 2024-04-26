package org.acme.bedallocation.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.Duration;

import ai.timefold.models.sdk.api.SolvingStatus;

import org.acme.bedallocation.domain.BedPlan;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@QuarkusTest
class BedSchedulingResourceTest {

    @BeforeAll
    public static void configure() {
        // required as default RestAssured object mapper serializes LocalDate as an array and by that fails schema validation
        ObjectMapper mapper =
                new ObjectMapper().findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (aClass, s) -> {
                    return mapper;
                }));
    }

    @Test
    void solveDemoDataUntilFeasible() {
        String scheduleJson = given()
                .when().get("/v1/demo-data/SMALL")
                .then()
                .statusCode(200).extract().body().asPrettyString();

        String jobId = given()
                .contentType(ContentType.JSON)
                .body(scheduleJson)
                .when().post("/v1/bed-allocations")
                .then()
                .log().all()
                .statusCode(202)
                .extract()
                .body().path("id");

        await()
                .atMost(Duration.ofMinutes(1))
                .pollInterval(Duration.ofMillis(500L))
                .until(() -> SolvingStatus.SOLVING_COMPLETED.name().equals(
                        get("/v1/bed-allocations/" + jobId + "/run")
                                .jsonPath().get("solverStatus")));

        BedPlan solution =
                get("/v1/bed-allocations/" + jobId).then().extract().jsonPath().getObject("modelOutput", BedPlan.class);
        assertThat(solution.getStays().stream().allMatch(bedDesignation -> bedDesignation.getBed() != null)).isTrue();
        assertThat(solution.getScore().isFeasible()).isTrue();
    }

    @Test
    void analyze() {
        String scheduleJson = given()
                .when().get("/v1/demo-data/SMALL")
                .then()
                .statusCode(200).extract().body().asPrettyString();

        String jobId = given()
                .contentType(ContentType.JSON)
                .body(scheduleJson)
                .when().post("/v1/bed-allocations")
                .then()
                .statusCode(202)
                .extract()
                .body().path("id");

        await()
                .atMost(Duration.ofMinutes(1))
                .pollInterval(Duration.ofMillis(500L))
                .until(() -> SolvingStatus.SOLVING_COMPLETED.name().equals(
                        get("/v1/bed-allocations/" + jobId + "/run")
                                .jsonPath().get("solverStatus")));

        String solutionJson = get("/v1/bed-allocations/" + jobId).then().extract().body().asPrettyString();

        String analysis = given()
                .contentType(ContentType.JSON)
                .body(solutionJson)
                .when()
                .put("/v1/bed-allocations/score-analysis")
                .then()
                .extract()
                .asString();
        // There are too many constraints to validate
        assertThat(analysis).isNotNull();
    }

}