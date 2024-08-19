package org.soccer;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.soccer.soccerplayer.dto.SoccerPlayerDTO;
import org.soccer.soccerplayer.resource.SoccerPlayerResource;
import org.soccer.soccerplayer.service.SoccerPlayerService;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
@TestHTTPEndpoint(SoccerPlayerResource.class)
public class SoccerPlayerResourceTest {

    @InjectSpy
    SoccerPlayerService soccerPlayerService;

    @Test
    void testGetSoccerPlayerEndpoint() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200);

        Mockito.verify(soccerPlayerService,
                Mockito.times(1))
                .getAll();
    }

    @Test
    void testGetSoccerPlayerByIdEndpoint() {
        given()
                .pathParam("id", 1L)
                .when()
                .get("{id}")
                .then()
                .body("name", equalTo("name"))
                .statusCode(200);

        Mockito.verify(soccerPlayerService,
                Mockito.times(1))
                .getById(1L);
    }

    @Test
    void testCreateSoccerPlayerEndpoint() {
        SoccerPlayerDTO soccerPlayerDTO = new SoccerPlayerDTO("name", "surname", 18, "team");

        given()
                .contentType("application/json")
                .body(soccerPlayerDTO)
                .when()
                .post()
                .then()
                .statusCode(200);

        Mockito.verify(soccerPlayerService,
                Mockito.times(1))
                .create(soccerPlayerDTO);
    }

    @Test
    void testUpdateSoccerPlayerEndpoint() {
        SoccerPlayerDTO soccerPlayerDTO = new SoccerPlayerDTO("name", "surname", 18, "team");
        var id = 1L;

        given()
                .contentType("application/json")
                .body(soccerPlayerDTO)
                .when()
                .put("{id}", id)
                .then()
                .statusCode(200);

        Mockito.verify(soccerPlayerService,
                Mockito.times(1))
                .update(id, soccerPlayerDTO);
    }

    @Test
    void testDeleteSoccerPlayerEndpoint() {
        var id = 1L;

        given()
                .pathParam("id", id)
                .when()
                .delete("{id}")
                .then()
                .statusCode(200);

        Mockito.verify(soccerPlayerService,
                        Mockito.times(1))
                .delete(id);
    }

}
