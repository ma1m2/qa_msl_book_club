package msl.qa.tests.api.login;

import io.qameta.allure.Feature;
import msl.qa.models.login.EmptyPasswordLoginRespModel;
import msl.qa.models.login.EmptyUserLoginRespModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.login.WrongCredlsLoginRespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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

@Feature("[API] Login")
@Tag("api")
public class LoginTests extends TestBase {
  RegisterReqModel loginData = new RegisterReqModel(USERNAME, PASSWORD);
  RegisterReqModel wrongPasswordData = new RegisterReqModel(USERNAME, WRONG_PASSWORD);
  RegisterReqModel emptyPasswordData = new RegisterReqModel(USERNAME, "");
  RegisterReqModel emptyUserData = new RegisterReqModel("", PASSWORD);
  RegisterReqModel loginDataForTrim = new RegisterReqModel(" " + USERNAME + " ", " " + PASSWORD + " ");
  RegisterReqModel upperCaseUserData = new RegisterReqModel(USERNAME.toUpperCase(), PASSWORD);

  @Test
  @DisplayName("[API] Successful Login")
  public void successfulLoginTest() {
      LoginRespModel loginResp = api.auth.successfulLogin(loginData);

      String actualAccess = loginResp.access();
      String actualRefresh = loginResp.refresh();
      assertThat(actualAccess).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  @DisplayName("[API] Verify trim removes boundary spaces")
  public void checkTrimFunctionLoginTest() {
      LoginRespModel loginResp = api.auth.checkTrimFunction(loginDataForTrim);

      String actualAccess = loginResp.access();
      String actualRefresh = loginResp.refresh();
      assertThat(actualAccess).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).startsWith(TOKEN_PREFIX);
      assertThat(actualRefresh).isNotEqualTo(actualAccess);
  }

  @Test
  @Tag("negative")
  @DisplayName("[API] Login fails with case-modified username")
  public void upperCaseUserLoginTest() {

      WrongCredlsLoginRespModel wrongCredlsLoginResp = api.auth.upperCaseUser(upperCaseUserData);

      String actualDetail = wrongCredlsLoginResp.detail();
      assertThat(WRONG_CREDLS_DETAIL).isEqualTo(actualDetail);
  }

  @Test
  @Tag("negative")
  @DisplayName("[API] Login fails with wrong Password")
  public void wrongPasswordLoginTest() {

      WrongCredlsLoginRespModel wrongCredlsLoginResp = api.auth.wrongPassword(wrongPasswordData);

      String actualDetail = wrongCredlsLoginResp.detail();
      assertThat(WRONG_CREDLS_DETAIL).isEqualTo(actualDetail);
  }

  @Test
  @Tag("negative")
  @DisplayName("[API] Login fails with empty Password")
  public void emptyPasswordLoginTest() {
      EmptyPasswordLoginRespModel emptyPasswordLoginResp = api.auth.emptyPassword(emptyPasswordData);

      List<String> actualDetail = emptyPasswordLoginResp.password();
      assertThat(BLANK_FIELD).isEqualTo(actualDetail.getFirst());
  }

  @Test
  @Tag("negative")
  @DisplayName("[API] Login fails with empty Username")
  public void emptyUserLoginTest() {
    step("Username is empty", () -> {
      EmptyUserLoginRespModel emptyUserLoginResp = api.auth.emptyUser(emptyUserData);

      List<String> actualDetail = emptyUserLoginResp.username();
      assertThat(BLANK_FIELD).isEqualTo(actualDetail.getFirst());
    });
  }

}
