package msl.qa.tests;

import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.login.WrongCredlsLoginRespModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.loginReqSpec;
import static msl.qa.spec.login.LoginSpec.successLoginRespSpec;
import static msl.qa.spec.login.LoginSpec.wrongCredlsLoginRespSpec;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginWithSpecTests extends TestBase{
  String url = "/auth/token/";
  String username = "qamsl";
  String password = "1234";
  LoginReqModel loginData = new LoginReqModel(username, password);
  LoginReqModel wrongPassword = new LoginReqModel(username, "12345");

  @Test
  public void successfulLoginTest() {
    LoginRespModel loginResp = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(url)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);

    String expectedTokenStart = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    String actualAccess = loginResp.access();
    String actualRefresh = loginResp.refresh();
    assertThat(actualAccess).startsWith(expectedTokenStart);
    assertThat(actualRefresh).startsWith(expectedTokenStart);
    assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  public void wrongCredlsLoginTest() {
    WrongCredlsLoginRespModel wrongCredlsLoginResp = given(loginReqSpec)
            .body(wrongPassword)
            .when()
            .post(url)
            .then()
            .spec(wrongCredlsLoginRespSpec)
            .extract().as(WrongCredlsLoginRespModel.class);

    String expectedDetail = "Invalid username or password.";
    String actualDetail = wrongCredlsLoginResp.detail();
    assertThat(expectedDetail).isEqualTo(actualDetail);

  }

}
