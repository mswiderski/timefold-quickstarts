package org.acme.vehiclerouting.rest;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ai.timefold.models.sdk.api.SolvingStatus;
import ai.timefold.models.sdk.api.analysis.ScoreAnalysisConstraintDetail;
import ai.timefold.models.sdk.api.analysis.ScoreAnalysisDetail;
import ai.timefold.models.sdk.api.domain.ModelConfig;
import ai.timefold.models.sdk.api.domain.ScoreAnalysisConfig;
import ai.timefold.models.sdk.api.domain.ScoreAnalysisRequest;
import ai.timefold.models.sdk.maps.service.integration.model.Location;
import ai.timefold.quarkus.models.sdk.defaults.EmptyModelConfigOverrides;
import ai.timefold.solver.core.api.score.analysis.ScoreAnalysis;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import org.acme.vehiclerouting.domain.VehicleRoutePlan;
import org.acme.vehiclerouting.domain.Visit;
import org.acme.vehiclerouting.domain.dto.ApplyRecommendationRequest;
import org.acme.vehiclerouting.domain.dto.RecommendationRequest;
import org.acme.vehiclerouting.domain.dto.VehicleRecommendation;
import org.acme.vehiclerouting.solver.justifications.MinimizeTravelTimeJustification;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class VehicleRoutePlanResourceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @BeforeAll
    static void initializeJacksonParser() {
        // Registers required org.acme.vehiclerouting.domain.jackson.VRPScoreAnalysisJacksonModule,
        // see META-INF/services/com.fasterxml.jackson.databind.Module.
        OBJECT_MAPPER.findAndRegisterModules();
    }

    @Test
    public void solveDemoDataUntilFeasible() {
        VehicleRoutePlan solution = solveDemoData();
        assertTrue(solution.getScore().isFeasible());
    }

    @Test
    public void analyzeFetchAll() throws JsonProcessingException {
        VehicleRoutePlan solution = solveDemoData();
        assertTrue(solution.getScore().isFeasible());

        String analysisAsString = given()
                .contentType(ContentType.JSON)
                .body(new ScoreAnalysisRequest(new ScoreAnalysisConfig(new ModelConfig(new EmptyModelConfigOverrides())),
                        solution))
                .when()
                .post("/v1/route-plans/score-analysis?includeJustifications=true")
                .then()
                .extract()
                .body().asString();

        ScoreAnalysisDetail<?, ?> analysis = parseScoreAnalysis(analysisAsString);

        assertNotNull(analysis.score());
        ScoreAnalysisConstraintDetail<?, ?> minimizeTravelTimeAnalysis =
                analysis.constraints().stream().filter(c -> c.name().equals("minimizeTravelTime")).findFirst().get();
        assertNotNull(minimizeTravelTimeAnalysis);
        assertNotNull(minimizeTravelTimeAnalysis.matches());
        assertFalse(minimizeTravelTimeAnalysis.matches().isEmpty());
    }

    @Test
    public void analyzeFetchShallow() throws JsonProcessingException {
        VehicleRoutePlan solution = solveDemoData();
        assertTrue(solution.getScore().isFeasible());

        String analysisAsString = given()
                .contentType(ContentType.JSON)
                .body(new ScoreAnalysisRequest(new ScoreAnalysisConfig(new ModelConfig(new EmptyModelConfigOverrides())),
                        solution))
                .expect().contentType(ContentType.JSON)
                .when()
                .post("/v1/route-plans/score-analysis")
                .then()
                .extract()
                .body().asString();

        ScoreAnalysisDetail<?, ?> analysis = parseScoreAnalysis(analysisAsString);

        assertNotNull(analysis.score());
        ScoreAnalysisConstraintDetail<?, ?> minimizeTravelTimeAnalysis =
                analysis.constraints().stream().filter(c -> c.name().equals("minimizeTravelTime")).findFirst().get();
        assertNotNull(minimizeTravelTimeAnalysis);
        assertTrue(minimizeTravelTimeAnalysis.matches().isEmpty());
    }

    private VehicleRoutePlan generateInitialSolution() {
        // Fetching the problem data
        String vehicleRoutePlan = given()
                .when().get("/v1/demo-data/FIRENZE")
                .then()
                .statusCode(200)
                .extract()
                .body().asPrettyString();

        // Starting the optimization
        String jobId = given()
                .contentType(ContentType.JSON)
                .body(vehicleRoutePlan)
                .expect().contentType(ContentType.JSON)
                .when().post("/v1/route-plans")
                .then()
                .statusCode(202)
                .extract()
                .body().path("id");


        // Waiting for the solution
        await()
                .atMost(Duration.ofMinutes(1))
                .pollInterval(Duration.ofMillis(500L))
                .until(() -> SolvingStatus.SOLVING_COMPLETED.name().equals(
                        get("/v1/route-plans/" + jobId + "/run")
                                .jsonPath().get("solverStatus")));

        VehicleRoutePlan solution =
                get("/v1/route-plans/" + jobId).then().extract().jsonPath().getObject("modelOutput", VehicleRoutePlan.class);

        return solution;
    }

    private Visit generateNewVisit(VehicleRoutePlan solution) {
        Visit newVisit = new Visit(String.valueOf(solution.getVisits().size() + 1),
                "visit%d".formatted(solution.getVisits().size() + 1), new Location(43.77800837529796, 11.223969038020176),
                2, LocalDateTime.now().plusDays(1).withHour(8).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                Duration.ofMinutes(10));
        solution.getVisits().add(newVisit);
        return newVisit;
    }

    private List<Pair<VehicleRecommendation, ScoreAnalysis>> getRecommendations(VehicleRoutePlan solution, Visit newVisit) {
        RecommendationRequest request = new RecommendationRequest(solution, newVisit.getId());
        return parseRecommendedFitList(given()
                .contentType(ContentType.JSON)
                .body(request)
                .expect().contentType(ContentType.JSON)
                .when()
                .post("/v1/route-plans/recommendation")
                .then()
                .extract()
                .as(List.class));
    }

    private VehicleRoutePlan applyBestRecommendation(VehicleRoutePlan solution, Visit newVisit,
            List<Pair<VehicleRecommendation, ScoreAnalysis>> recommendedFitList) {
        // Selects the best recommendation
        VehicleRecommendation recommendation = recommendedFitList.get(0).getLeft();
        ApplyRecommendationRequest applyRequest = new ApplyRecommendationRequest(solution, newVisit.getId(),
                recommendation.vehicleId(), recommendation.index());

        // Applies the recommendation
        return given()
                .contentType(ContentType.JSON)
                .body(applyRequest)
                .expect().contentType(ContentType.JSON)
                .when()
                .post("/v1/route-plans/recommendation/apply")
                .then()
                .extract()
                .as(VehicleRoutePlan.class);
    }

    @Test
    public void testRecommendedFit() {
        // Generate an initial solution
        VehicleRoutePlan solution = generateInitialSolution();
        assertNotNull(solution);

        // Create a new visit
        Visit newVisit = generateNewVisit(solution);

        // Request recommendation
        List<Pair<VehicleRecommendation, ScoreAnalysis>> recommendedFitList = getRecommendations(solution, newVisit);
        assertNotNull(recommendedFitList);
        assertEquals(5, recommendedFitList.size());

        // Apply the best recommendation
        VehicleRoutePlan updatedSolution = applyBestRecommendation(solution, newVisit, recommendedFitList);
        assertNotNull(updatedSolution);
        assertNotEquals(updatedSolution.getScore().toString(), solution.getScore().toString());
    }

    private VehicleRoutePlan solveDemoData() {
        String vehicleRoutePlan = given()
                .when().get("/v1/demo-data/FIRENZE")
                .then()
                .statusCode(200)
                .extract()
                .body().asPrettyString();

        String jobId = given()
                .contentType(ContentType.JSON)
                .body(vehicleRoutePlan)
                .expect().contentType(ContentType.JSON)
                .when().post("/v1/route-plans")
                .then()
                .statusCode(202)
                .extract()
                .body().path("id");

        await()
                .atMost(Duration.ofMinutes(1))
                .pollInterval(Duration.ofMillis(500L))
                .until(() -> SolvingStatus.SOLVING_COMPLETED.name().equals(
                        get("/v1/route-plans/" + jobId + "/run")
                                .jsonPath().get("solverStatus")));

        VehicleRoutePlan solution =
                get("/v1/route-plans/" + jobId).then().extract().jsonPath().getObject("modelOutput", VehicleRoutePlan.class);
        assertNotNull(solution.getVehicles());
        assertNotNull(solution.getVisits());
        assertNotNull(solution.getVehicles().get(0).getVisits());
        return solution;
    }

    private ScoreAnalysisDetail<HardSoftLongScore, MinimizeTravelTimeJustification> parseScoreAnalysis(String analysis)
            throws JsonProcessingException {
        assertNotNull(analysis);
        return OBJECT_MAPPER.readValue(analysis, new TypeReference<>() {
        });
    }

    private List<Pair<VehicleRecommendation, ScoreAnalysis>>
            parseRecommendedFitList(List<Map<String, Object>> recommendedFitMap) {
        assertNotNull(recommendedFitMap);
        List<Pair<VehicleRecommendation, ScoreAnalysis>> recommendedFitList = new ArrayList<>(recommendedFitMap.size());
        recommendedFitMap.forEach(record -> recommendedFitList.add(Pair.of(
                OBJECT_MAPPER.convertValue(record.get("proposition"), VehicleRecommendation.class),
                OBJECT_MAPPER.convertValue(record.get("scoreDiff"), ScoreAnalysis.class))));
        return recommendedFitList;
    }
}
