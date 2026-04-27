package msl.qa.tests;

import msl.qa.models.login.LoginReqModel;
import msl.qa.models.logout.LogoutReqModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.loginReqSpec;
import static msl.qa.spec.login.LoginSpec.successLoginRespSpec;
import static msl.qa.spec.logout.LogoutSpec.logoutReqSpec;
import static msl.qa.spec.logout.LogoutSpec.successLogoutRespSpec;


public class LogoutTests extends TestBase{

  String urlLogin = "/auth/token/";
  String urlLogout = "/auth/logout/";
  String username = "qamsl";
  String password = "1234";
  LoginReqModel loginData = new LoginReqModel(username, password);
  LogoutReqModel logoutData;

  @Test
  public void successfulLogoutTest() {
    String refreshToken = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("refresh");

    logoutData = new LogoutReqModel(refreshToken);

    given(logoutReqSpec)
            .body(logoutData)
            .when()
            .post(urlLogout)
            .then()
            .spec(successLogoutRespSpec);
  }

}
