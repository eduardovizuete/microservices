package org.soccer;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class TeamResourceTest {
    @Test
    void testTeamsEndpoint() {
        given()
                .when()
                .get("/api/team")
                .then()
                .statusCode(200);
    }

}