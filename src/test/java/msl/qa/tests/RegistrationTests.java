package msl.qa.tests;

import io.restassured.http.ContentType;
import msl.qa.models.registration.ExistingUser400RespModel;
import msl.qa.models.registration.RegistrationReqModel;
import msl.qa.models.registration.RegistrationRespModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests {
  String url = "https://book-club.qa.guru/api/v1/users/register/";
  String username;
  String password;
  RegistrationReqModel data;

  @BeforeEach
  public void prepareTestData() {
    Faker faker = new Faker();
    username = faker.name().firstName();
    password = faker.name().firstName();
    data = new RegistrationReqModel(username, password);
  }

  @Test
  public void successfulRegistrationTest() {

    RegistrationRespModel respModel = given()
            .log().all()
            .body(data)
            .contentType(ContentType.JSON)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(201)
            .extract().as(RegistrationRespModel.class);
    assertEquals(username, respModel.username());
    assertEquals("", respModel.firstName());
    assertEquals("", respModel.lastName());
    assertEquals("", respModel.email());
  }

  @Test
  public void existingUser400RegistrationTest() {
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given()
            .log().all()
            .body(data)
            .contentType(ContentType.JSON)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(201)
            .body("username", is(username))
            .body("id", notNullValue());

    ExistingUser400RespModel resp = given()
            .log().all()
            .body(data)
            .contentType(ContentType.JSON)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(400)
            .extract().as(ExistingUser400RespModel.class);
    String expectedError = "A user with that username already exists.";
    assertEquals(expectedError, resp.username().get(0));
  }

  @Test
  public void invalidUsername400RegistrationTest() {
    Faker faker = new Faker();
    String username = faker.name().firstName() + "#";
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given()
            .log().all()
            .body(data)
            .contentType(ContentType.JSON)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(400)
            .body("username[0]", containsString("Enter a valid username."));
  }

  @Test
  public void error500RegistrationTest() {
    String wrongUrl = "https://book-club.qa.guru/api/v1/users/register";
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given()
            .log().all()
            .body(data)
            .contentType("application/json")
            .when()
            .post(wrongUrl)
            .then()
            .log().all()
            .statusCode(500);
  }

  @Test
  public void wrongContentType415RegistrationTest() {
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given()
            .log().all()
            .body(data)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(415)
            .body("detail", containsString("Unsupported media type"));
  }
}
