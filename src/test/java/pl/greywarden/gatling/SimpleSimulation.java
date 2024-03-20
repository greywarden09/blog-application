package pl.greywarden.gatling;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.OpenInjectionStep.RampRate.RampRateOpenInjectionStep;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import pl.greywarden.tutorial.domain.dto.CreateAuthorRequest;

import java.time.Duration;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.StringBody;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsersPerSec;
import static io.gatling.javaapi.http.HttpDsl.header;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

@Slf4j
public class SimpleSimulation extends Simulation {

    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();
    private static final Iterator<Map<String, Object>> FEED_AUTHORS = setupTestFeedData();
    private static final ScenarioBuilder POST_SCENARIO_BUILDER = buildPostScenario();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public SimpleSimulation() {
        setUp(POST_SCENARIO_BUILDER.injectOpen(postEndpointInjectionProfile())
                .protocols(HTTP_PROTOCOL_BUILDER)).assertions(global().responseTime()
                .max()
                .lte(10000), global().successfulRequests()
                .percent()
                .gt(90d));
    }

    private RampRateOpenInjectionStep postEndpointInjectionProfile() {
        var totalDesiredUserCount = 1;
        var userRampPerInterval = 50d;
        var rampUpIntervalSeconds = 30d;

        var totalRampUptimeSeconds = 120;
        var steadyStateDurationSeconds = 300;

        return rampUsersPerSec(userRampPerInterval / (rampUpIntervalSeconds / 60)).to(totalDesiredUserCount)
                .during(Duration.ofMinutes(1));
    }

    private static Iterator<Map<String, Object>> setupTestFeedData() {
        var faker = new Faker();
        return Stream.generate(() -> Map.of("name", (Object) faker.name().fullName())).iterator();
    }

    private static HttpProtocolBuilder setupProtocolForSimulation() {
        return http.baseUrl("http://localhost:8080")
                .acceptHeader("application/json")
                .maxConnectionsPerHost(10)
                .userAgentHeader("Gatling/Performance Test");
    }

    private static ScenarioBuilder buildPostScenario() {
        return CoreDsl.scenario("Load test creating author")
                .feed(FEED_AUTHORS)
                .exec(http("create-author").post("/api/authors")
                        .header("Content-Type", "application/json")
                        .body(StringBody(SimpleSimulation::createAuthor))
                        .check(status().is(201))
                        .check(header("Location").saveAs("location")))
                .exec(http("get-author").get(session -> session.getString("location"))
                        .check(status().is(200)))
                .exec(http("create-blog-entry").post("/api/blog-entries"));
    }

    @SneakyThrows
    private static String createAuthor(Session session) {
        var name = session.getString("name");
        return objectMapper.writeValueAsString(new CreateAuthorRequest(name));
    }

    private static String createBlogEntry(Session session) {
        var location = Objects.requireNonNull(session.getString("location"));
        var authorId = Integer.parseInt(location.substring(location.lastIndexOf("/")));


    }

}
