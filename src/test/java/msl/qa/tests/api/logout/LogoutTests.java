package msl.qa.tests.api.logout;

import io.qameta.allure.Feature;
import msl.qa.models.logout.LogoutReqModel;
import msl.qa.models.logout.WrongTokenLogoutRespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.CODE;
import static msl.qa.tests.TestData.INVALID_TOKEN_DETAIL;
import static msl.qa.tests.TestData.PASSWORD;
import static msl.qa.tests.TestData.TOKEN_HAS_WRONG_TYPE;
import static msl.qa.tests.TestData.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("[API] Logout")
@Tag("api")
public class LogoutTests extends TestBase {

  RegisterReqModel loginData = new RegisterReqModel(USERNAME, PASSWORD);
  LogoutReqModel logoutData;
  LogoutReqModel partLogoutData;

  @Test
  @DisplayName("[API] Successful Logout")
  public void successfulLogoutTest() {

    step("Login and Logout with refresh-token", () -> {
      String refreshToken = api.auth.extractRefreshToken(loginData);
      logoutData = new LogoutReqModel(refreshToken);
      api.auth.logout(logoutData);
    });
  }

  @Test
  @Tag("negative")
  @DisplayName("[API] Logout with Access Token is not allowed")
  public void wrongRefreshTokenLogoutTest() {

    step("Login and get wrong-token for logout (accessToken)", () -> {
      String accessToken = api.auth.extractAccessToken(loginData);
      logoutData = new LogoutReqModel(accessToken);

      WrongTokenLogoutRespModel wrongLogoutResp = api.auth.logoutWithWrongToken(logoutData);

      String actualDetail = wrongLogoutResp.detail();
      assertThat(TOKEN_HAS_WRONG_TYPE).isEqualTo(actualDetail);
      String actualCode = wrongLogoutResp.code();
      assertThat(CODE).isEqualTo(actualCode);
    });
  }

  @Test
  @Tag("negative")
  @DisplayName("[API] Logout with part Refresh Token is not allowed")
  public void partRefreshTokenLogoutTest() {

    step("Logout with part of refresh-token (length/5*4)", () -> {
      String refreshToken = api.auth.extractRefreshToken(loginData);

      int length = refreshToken.length();
      String partOfString = refreshToken.substring(0, length/5*4);
      partLogoutData = new LogoutReqModel(partOfString);

      WrongTokenLogoutRespModel wrongLogoutResp = api.auth.logoutWithWrongToken(partLogoutData);

      String actualDetail = wrongLogoutResp.detail();
      assertThat(INVALID_TOKEN_DETAIL).isEqualTo(actualDetail);
      String actualCode = wrongLogoutResp.code();
      assertThat(CODE).isEqualTo(actualCode);
    });
  }

}
