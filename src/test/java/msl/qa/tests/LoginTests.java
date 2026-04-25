package msl.qa.tests;

import io.restassured.http.ContentType;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.login.WrongCredlsLoginRespModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTests extends TestBase{
  String url = "/api/v1/auth/token/";
  String username = "qamsl";
  String password = "1234";
  LoginReqModel data = new LoginReqModel(username, password);
  LoginReqModel wrongPassword = new LoginReqModel(username, "12345");

  @Test
  public void successfulLoginTest() {
    LoginRespModel loginResp = given()
            .log().all()
            .body(data)
            .contentType(ContentType.JSON)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/login_response_schemas.json"))
            .body("access", notNullValue())
            .body("refresh", notNullValue())
            .extract().as(LoginRespModel.class);

    String expectedTokenStart = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    String actualAccess = loginResp.access();
    String actualRefresh = loginResp.refresh();
    assertThat(actualAccess).startsWith(expectedTokenStart);
    assertThat(actualRefresh).startsWith(expectedTokenStart);
    assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  public void wrongCredentialsLoginTest() {
    WrongCredlsLoginRespModel wrongCredlsLoginResp = given()
            .log().all()
            .body(wrongPassword)
            .contentType(ContentType.JSON)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(401)
            .body(matchesJsonSchemaInClasspath("schemas/wrong_credls_login_response_schemas.json"))
            .body("detail", notNullValue())
            .extract().as(WrongCredlsLoginRespModel.class);

    String expectedDetail = "Invalid username or password.";
    String actualDetail = wrongCredlsLoginResp.detail();
    assertThat(expectedDetail).isEqualTo(actualDetail);

  }

}
