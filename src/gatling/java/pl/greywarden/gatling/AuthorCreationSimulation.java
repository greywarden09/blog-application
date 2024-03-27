package pl.greywarden.gatling;

import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import net.datafaker.Faker;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.OpenInjectionStep.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class AuthorCreationSimulation extends Simulation {
    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();
    private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();
    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildPostScenario();

    public AuthorCreationSimulation() {
        setUp(POST_SCENARIO_BUILDER.injectOpen(atOnceUsers(100))
                .protocols(HTTP_PROTOCOL_BUILDER))
                .assertions(
                        global().successfulRequests().percent().is(100d),
                        global().responseTime().max().lte(500));
    }

    private static Iterator<Map<String, Object>> setupTestFeedData() {
        var faker = new Faker();
        return Stream.generate(() ->
                Map.of("authorName", (Object) faker.name().fullName())).iterator();
    }

    private static HttpProtocolBuilder setupProtocolForSimulation() {
        return http.baseUrl("http://localhost:8080/")
                .acceptHeader("application/json")
                .maxConnectionsPerHost(10)
                .userAgentHeader("Gatling/Performance Test");
    }

    private static ScenarioBuilder buildPostScenario() {
        return CoreDsl.scenario("Load Test Creating Author")
                .feed(FEED_DATA)
                .exec(http("create-author-request").post("/api/authors")
                        .header("Content-Type", "application/json")
                        .body(StringBody("{ \"name\": \"${authorName}\" }"))
                        .check(status().is(201))
                        .check(header("Location").saveAs("location")))
                .exec(http("get-author-request").get(session -> session.getString("location"))
                        .check(status().is(200)));
    }
}
