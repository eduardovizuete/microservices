package org.soccer.footballmanager;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.soccer.footballmanager.domain.FootballManager;
import org.soccer.footballmanager.resource.FootballManagerResource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@TestHTTPEndpoint(FootballManagerResource.class)
public class FootballManagerResourceTest {

    @Test
    public void getAll() {
        given()
                .when()
                .get()
                .then()
                .statusCode(200);
    }

    @Test
    public void getById() {
        FootballManager fm = createFootballManager();
        FootballManager saved = given()
                .contentType(ContentType.JSON)
                .body(fm)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(FootballManager.class);
        FootballManager got = given()
                .when()
                .get("/{fmId}", saved.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(FootballManager.class);
        assertThat(saved).isEqualTo(got);
    }

    @Test
    public void getByIdNotFound() {
        given()
                .when()
                .get("/{fmId}", 987654321)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void post() {
        FootballManager fm = createFootballManager();
        FootballManager saved = given()
                .contentType(ContentType.JSON)
                .body(fm)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(FootballManager.class);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void postFailNoFirstName() {
        FootballManager fm = new FootballManager();
        fm.setName(null);
        given()
                .contentType(ContentType.JSON)
                .body(fm)
                .post()
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void put() {
        FootballManager fm = createFootballManager();
        FootballManager saved = given()
                .contentType(ContentType.JSON)
                .body(fm)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(FootballManager.class);
        saved.setName("NameUpdated");
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{fmId}", saved.getId())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void putFailNoSurname() {
        FootballManager fm = createFootballManager();
        FootballManager saved = given()
                .contentType(ContentType.JSON)
                .body(fm)
                .post()
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().as(FootballManager.class);
        saved.setSurname(null);
        given()
                .contentType(ContentType.JSON)
                .body(saved)
                .put("/{fmId}", saved.getId())
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    private FootballManager createFootballManager() {
         FootballManager fm = new FootballManager();
         fm.setName(RandomStringUtils.randomAlphabetic(10));
         fm.setSurname(RandomStringUtils.randomAlphabetic(10));
         fm.setAge(Integer.getInteger(RandomStringUtils.randomNumeric(2)));
         fm.setNickname(RandomStringUtils.randomAlphabetic(10));
         return fm;
    }

}