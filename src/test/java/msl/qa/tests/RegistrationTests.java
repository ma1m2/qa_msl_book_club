package msl.qa.tests;

import io.restassured.http.ContentType;
import msl.qa.models.registration.ExistingUser400RespModel;
import msl.qa.models.registration.RegistrationReqModel;
import msl.qa.models.registration.RegistrationRespModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests extends TestBase{
  String url = "/users/register/";
  String basePath = "/api/v1";
  String username;
  String password;
  RegistrationReqModel registrationData;

  @BeforeEach
  public void prepareTestData() {
    Faker faker = new Faker();
    username = faker.name().firstName();
    password = faker.name().firstName();
    registrationData = new RegistrationReqModel(username, password);
  }

  @Test
  public void successfulRegistrationTest() {

    RegistrationRespModel respModel = given()
            .log().all()
            .body(registrationData)
            .contentType(ContentType.JSON)
            .basePath(basePath)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(201)
            .body(matchesJsonSchemaInClasspath("schemas/register/register_response_schemas.json"))
            .body("id", notNullValue())
            .body("username", notNullValue())
            .body("remoteAddr", notNullValue())
            .extract().as(RegistrationRespModel.class);

    assertThat(respModel.username()).isEqualTo(username);
    assertThat(respModel.id()).isGreaterThan(0);
    assertThat(respModel.firstName()).isEmpty(); //isEqualTo("");
    assertThat(respModel.lastName()).isEmpty();
    assertThat(respModel.email()).isEmpty();

    String ipRegex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    assertThat(respModel.remoteAddr()).matches(ipRegex);
  }

  @Test
  public void existingUser400RegistrationTest() {
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given()
            .log().all()
            .body(data)
            .contentType(ContentType.JSON)
            .basePath(basePath)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(201)
            .body(matchesJsonSchemaInClasspath("schemas/register/register_response_schemas.json"))
            .body("username", is(username))
            .body("id", notNullValue());

    ExistingUser400RespModel resp = given()
            .log().all()
            .body(data)
            .contentType(ContentType.JSON)
            .basePath(basePath)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(400)
            .body(matchesJsonSchemaInClasspath("schemas/register/existin_user_response_schemas.json"))
            .extract().as(ExistingUser400RespModel.class);

    String expectedError = "A user with that username already exists.";
    String actualdError = resp.username().getFirst();
    assertThat(actualdError).isEqualTo(expectedError);
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
            .basePath(basePath)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(400)
            .body(matchesJsonSchemaInClasspath("schemas/register/invalid_user_response_schemas.json"))
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
            .basePath(basePath)
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
            .basePath(basePath)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(415)
            .body("detail", containsString("Unsupported media type"));
  }
}
