package msl.qa.tests;

import msl.qa.models.login.EmptyPasswordLoginRespModel;
import msl.qa.models.login.EmptyUserLoginRespModel;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.login.WrongCredlsLoginRespModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.emptyPasswordLoginRespSpec;
import static msl.qa.spec.login.LoginSpec.emptyUserLoginRespSpec;
import static msl.qa.spec.login.LoginSpec.loginReqSpec;
import static msl.qa.spec.login.LoginSpec.successLoginRespSpec;
import static msl.qa.spec.login.LoginSpec.wrongCredlsLoginRespSpec;
import static msl.qa.tests.TestData.BLANK_FIELD;
import static msl.qa.tests.TestData.PASSWORD;
import static msl.qa.tests.TestData.TOKEN_PREFIX;
import static msl.qa.tests.TestData.USERNAME;
import static msl.qa.tests.TestData.WRONG_CREDLS_DETAIL;
import static msl.qa.tests.TestData.WRONG_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends TestBase{
  String url = "/auth/token/";
  LoginReqModel loginData = new LoginReqModel(USERNAME, PASSWORD);
  LoginReqModel wrongPasswordData = new LoginReqModel(USERNAME, WRONG_PASSWORD);
  LoginReqModel emptyPasswordData = new LoginReqModel(USERNAME, "");
  LoginReqModel emptyUserData = new LoginReqModel("", PASSWORD);
  LoginReqModel loginDataForTrim = new LoginReqModel(" " + USERNAME + " ", " " + PASSWORD + " ");
  LoginReqModel upperCaseUserData = new LoginReqModel(USERNAME.toUpperCase(), PASSWORD);

  @Test
  public void successfulLoginTest() {
    LoginRespModel loginResp = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(url)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);

    String actualAccess = loginResp.access();
    String actualRefresh = loginResp.refresh();
    assertThat(actualAccess).startsWith(TOKEN_PREFIX);
    assertThat(actualRefresh).startsWith(TOKEN_PREFIX);
    assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  public void checkTrimFunctionLoginTest() {
    LoginRespModel loginResp = given(loginReqSpec)
            .body(loginDataForTrim)
            .when()
            .post(url)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);

    String actualAccess = loginResp.access();
    String actualRefresh = loginResp.refresh();
    assertThat(actualAccess).startsWith(TOKEN_PREFIX);
    assertThat(actualRefresh).startsWith(TOKEN_PREFIX);
    assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  public void upperCaseUserLoginTest() {
    WrongCredlsLoginRespModel wrongCredlsLoginResp = given(loginReqSpec)
            .body(upperCaseUserData)
            .when()
            .post(url)
            .then()
            .spec(wrongCredlsLoginRespSpec)
            .extract().as(WrongCredlsLoginRespModel.class);

    String actualDetail = wrongCredlsLoginResp.detail();
    assertThat(WRONG_CREDLS_DETAIL).isEqualTo(actualDetail);
  }

  @Test
  public void wrongPasswordLoginTest() {
    WrongCredlsLoginRespModel wrongCredlsLoginResp = given(loginReqSpec)
            .body(wrongPasswordData)
            .when()
            .post(url)
            .then()
            .spec(wrongCredlsLoginRespSpec)
            .extract().as(WrongCredlsLoginRespModel.class);

    String actualDetail = wrongCredlsLoginResp.detail();
    assertThat(WRONG_CREDLS_DETAIL).isEqualTo(actualDetail);
  }

  @Test
  public void emptyPasswordLoginTest() {
    EmptyPasswordLoginRespModel emptyPasswordLoginResp = given(loginReqSpec)
            .body(emptyPasswordData)
            .when()
            .post(url)
            .then()
            .spec(emptyPasswordLoginRespSpec)
            .extract().as(EmptyPasswordLoginRespModel.class);

    List<String> actualDetail = emptyPasswordLoginResp.password();
    assertThat(BLANK_FIELD).isEqualTo(actualDetail.getFirst());
  }

  @Test
  public void emptyUserLoginTest() {
    EmptyUserLoginRespModel emptyUserLoginResp = given(loginReqSpec)
            .body(emptyUserData)
            .when()
            .post(url)
            .then()
            .spec(emptyUserLoginRespSpec)
            .extract().as(EmptyUserLoginRespModel.class);

    List<String> actualDetail = emptyUserLoginResp.username();
    assertThat(BLANK_FIELD).isEqualTo(actualDetail.getFirst());
  }

}
