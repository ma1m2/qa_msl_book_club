package msl.qa.tests;

import msl.qa.models.login.LoginReqModel;
import msl.qa.models.logout.LogoutReqModel;
import msl.qa.models.logout.WrongTokenLogoutRespModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.loginReqSpec;
import static msl.qa.spec.login.LoginSpec.successLoginRespSpec;
import static msl.qa.spec.logout.LogoutSpec.logoutReqSpec;
import static msl.qa.spec.logout.LogoutSpec.successLogoutRespSpec;
import static msl.qa.spec.logout.LogoutSpec.wrongTokenLogoutRespSpec;
import static msl.qa.tests.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LogoutTests extends TestBase{

  String urlLogin = "/auth/token/";
  String urlLogout = "/auth/logout/";
  LoginReqModel loginData = new LoginReqModel(USERNAME, PASSWORD);
  LogoutReqModel wrongLogoutData;
  LogoutReqModel partLogoutData;

  @Test
  public void successfulLogoutTest() {
    String refreshToken = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("refresh");

    wrongLogoutData = new LogoutReqModel(refreshToken);

    given(logoutReqSpec)
            .body(wrongLogoutData)
            .when()
            .post(urlLogout)
            .then()
            .spec(successLogoutRespSpec);
  }

  @Test
  public void wrongRefreshTokenLogoutTest() {
    String accessToken = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("access");

    wrongLogoutData = new LogoutReqModel(accessToken);

    WrongTokenLogoutRespModel wrongLogoutResp = given(logoutReqSpec)
            .body(wrongLogoutData)
            .when()
            .post(urlLogout)
            .then()
            .spec(wrongTokenLogoutRespSpec)
            .extract().as(WrongTokenLogoutRespModel.class);

    String actualDetail = wrongLogoutResp.detail();
    assertThat(TOKEN_HAS_WRONG_TYPE).isEqualTo(actualDetail);
    String actualCode = wrongLogoutResp.code();
    assertThat(CODE).isEqualTo(actualCode);
  }

  @Test
  public void partRefreshTokenLogoutTest() {
    String refreshToken = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("refresh");

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
  }

}
