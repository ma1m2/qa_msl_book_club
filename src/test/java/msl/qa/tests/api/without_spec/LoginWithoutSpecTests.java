package msl.qa.tests.api.without_spec;

import io.restassured.http.ContentType;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.login.WrongCredlsLoginRespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class LoginWithoutSpecTests extends TestBase {
  String url = "/auth/token/";
  String basePath = "/api/v1";
  String username = "qamsl";
  String password = "1234";
  RegisterReqModel loginData = new RegisterReqModel(username, password);
  RegisterReqModel wrongPassword = new RegisterReqModel(username, "12345");

  @Test
  public void successfulLoginTest() {
    LoginRespModel loginResp = given()

            .log().all()
            .body(loginData)
            .contentType(ContentType.JSON)
            .basePath(basePath)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(200)
            .body(matchesJsonSchemaInClasspath("schemas/login/login_response_schema.json"))
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
            .basePath(basePath)
            .when()
            .post(url)
            .then()
            .log().all()
            .statusCode(401)
            .body(matchesJsonSchemaInClasspath("schemas/login/wrong_credls_login_response_schema.json"))
            .body("detail", notNullValue())
            .extract().as(WrongCredlsLoginRespModel.class);

    String expectedDetail = "Invalid username or password.";
    String actualDetail = wrongCredlsLoginResp.detail();
    assertThat(expectedDetail).isEqualTo(actualDetail);

  }

}
