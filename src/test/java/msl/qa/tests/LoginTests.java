package msl.qa.tests;

import msl.qa.models.login.EmptyPasswordLoginRespModel;
import msl.qa.models.login.EmptyUserLoginRespModel;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.login.WrongCredlsLoginRespModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.BLANK_FIELD;
import static msl.qa.tests.TestData.PASSWORD;
import static msl.qa.tests.TestData.TOKEN_PREFIX;
import static msl.qa.tests.TestData.USERNAME;
import static msl.qa.tests.TestData.WRONG_CREDLS_DETAIL;
import static msl.qa.tests.TestData.WRONG_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginTests extends TestBase{
  LoginReqModel loginData = new LoginReqModel(USERNAME, PASSWORD);
  LoginReqModel wrongPasswordData = new LoginReqModel(USERNAME, WRONG_PASSWORD);
  LoginReqModel emptyPasswordData = new LoginReqModel(USERNAME, "");
  LoginReqModel emptyUserData = new LoginReqModel("", PASSWORD);
  LoginReqModel loginDataForTrim = new LoginReqModel(" " + USERNAME + " ", " " + PASSWORD + " ");
  LoginReqModel upperCaseUserData = new LoginReqModel(USERNAME.toUpperCase(), PASSWORD);

  @Test
  public void successfulLoginTest() {
      LoginRespModel loginResp = api.auth.successfulLogin(loginData);

      String actualAccess = loginResp.access();
      String actualRefresh = loginResp.refresh();
      assertThat(actualAccess).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  public void checkTrimFunctionLoginTest() {
      LoginRespModel loginResp = api.auth.checkTrimFunction(loginDataForTrim);

      String actualAccess = loginResp.access();
      String actualRefresh = loginResp.refresh();
      assertThat(actualAccess).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  public void upperCaseUserLoginTest() {

      WrongCredlsLoginRespModel wrongCredlsLoginResp = api.auth.upperCaseUser(upperCaseUserData);

      String actualDetail = wrongCredlsLoginResp.detail();
      assertThat(WRONG_CREDLS_DETAIL).isEqualTo(actualDetail);
  }

  @Test
  public void wrongPasswordLoginTest() {

      WrongCredlsLoginRespModel wrongCredlsLoginResp = api.auth.wrongPassword(wrongPasswordData);

      String actualDetail = wrongCredlsLoginResp.detail();
      assertThat(WRONG_CREDLS_DETAIL).isEqualTo(actualDetail);
  }

  @Test
  public void emptyPasswordLoginTest() {
      EmptyPasswordLoginRespModel emptyPasswordLoginResp = api.auth.emptyPassword(emptyPasswordData);

      List<String> actualDetail = emptyPasswordLoginResp.password();
      assertThat(BLANK_FIELD).isEqualTo(actualDetail.getFirst());
  }

  @Test
  public void emptyUserLoginTest() {
    step("Username is empty", () -> {
      EmptyUserLoginRespModel emptyUserLoginResp = api.auth.emptyUser(emptyUserData);

      List<String> actualDetail = emptyUserLoginResp.username();
      assertThat(BLANK_FIELD).isEqualTo(actualDetail.getFirst());
    });
  }

}
