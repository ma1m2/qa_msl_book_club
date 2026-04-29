package msl.qa.tests;

import msl.qa.models.login.LoginReqModel;
import msl.qa.models.logout.LogoutReqModel;
import msl.qa.models.logout.WrongTokenLogoutRespModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.loginReqSpec;
import static msl.qa.spec.login.LoginSpec.successLoginRespSpec;
import static msl.qa.spec.logout.LogoutSpec.logoutReqSpec;
import static msl.qa.spec.logout.LogoutSpec.successLogoutRespSpec;
import static msl.qa.spec.logout.LogoutSpec.wrongTokenLogoutRespSpec;
import static msl.qa.tests.TestData.CODE;
import static msl.qa.tests.TestData.INVALID_TOKEN_DETAIL;
import static msl.qa.tests.TestData.PASSWORD;
import static msl.qa.tests.TestData.TOKEN_HAS_WRONG_TYPE;
import static msl.qa.tests.TestData.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

public class LogoutTests extends TestBase{

  String urlLogin = "/auth/token/";
  String urlLogout = "/auth/logout/";
  LoginReqModel loginData = new LoginReqModel(USERNAME, PASSWORD);
  LogoutReqModel logoutData;
  LogoutReqModel partLogoutData;

  @Test
  public void successfulLogoutTest() {
    String refreshToken = step("Login and get refresh-token", () ->
            given(loginReqSpec)
                    .body(loginData)
                    .when()
                    .post(urlLogin)
                    .then()
                    .spec(successLoginRespSpec)
                    .extract().path("refresh"));

    logoutData = new LogoutReqModel(refreshToken);

    step("Logout with refresh-token", () -> {
      given(logoutReqSpec)
              .body(logoutData)
              .when()
              .post(urlLogout)
              .then()
              .spec(successLogoutRespSpec);
    });
  }

  @Test
  public void wrongRefreshTokenLogoutTest() {

    step("Login and get wrong-token (accessToken)", () -> {
      String accessToken = given(loginReqSpec)
              .body(loginData)
              .when()
              .post(urlLogin)
              .then()
              .spec(successLoginRespSpec)
              .extract().path("access");

      logoutData = new LogoutReqModel(accessToken);
    });

    step("Logout with wrong-token (accessToken)", () -> {
      WrongTokenLogoutRespModel wrongLogoutResp = given(logoutReqSpec)
              .body(logoutData)
              .when()
              .post(urlLogout)
              .then()
              .spec(wrongTokenLogoutRespSpec)
              .extract().as(WrongTokenLogoutRespModel.class);
      String actualDetail = wrongLogoutResp.detail();
      assertThat(TOKEN_HAS_WRONG_TYPE).isEqualTo(actualDetail);
      String actualCode = wrongLogoutResp.code();
      assertThat(CODE).isEqualTo(actualCode);
    });
  }

  @Test
  public void partRefreshTokenLogoutTest() {
    String refreshToken = step("Login and get refresh-token", () ->
            given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("refresh"));

    step("Logout with part of refresh-token (length/5*4)", () -> {
      int length = refreshToken.length();
      String partOfString = refreshToken.substring(0, length/5*4);

      partLogoutData = new LogoutReqModel(partOfString);

      WrongTokenLogoutRespModel wrongLogoutResp = given(logoutReqSpec)
              .body(partLogoutData)
              .when()
              .post(urlLogout)
              .then()
              .spec(wrongTokenLogoutRespSpec)
              .extract().as(WrongTokenLogoutRespModel.class);

      String actualDetail = wrongLogoutResp.detail();
      assertThat(INVALID_TOKEN_DETAIL).isEqualTo(actualDetail);
      String actualCode = wrongLogoutResp.code();
      assertThat(CODE).isEqualTo(actualCode);
    });
  }

}
