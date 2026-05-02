package msl.qa.tests;

import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegistrationReqModel;
import msl.qa.models.user.DetailCodeRespModel;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteUserTests extends TestBase {

  @Test
  public void deleteUserTest() {
    step("Delete user using token", () -> {
      registrationData = new RegistrationReqModel(username, password);

      api.users.register(registrationData);

      loginData = new LoginReqModel(username, PASSWORD);

      LoginRespModel loginResp = api.auth.successfulLogin(loginData);

      token = loginResp.access();

      api.users.deleteUser(token);
    });
  }

  @Test
  public void deletingDeletedUserTest() {
    step("Try delete deleted user", () -> {
      registrationData = new RegistrationReqModel(username, password);

      api.users.register(registrationData);

      loginData = new LoginReqModel(username, PASSWORD);

      LoginRespModel loginResp = api.auth.successfulLogin(loginData);

      token = loginResp.access();

      api.users.deleteUser(token);
      System.out.println();
      DetailCodeRespModel unauthResp = api.users.deleteUnfoundUser401(token);

      assertThat(USER_NOT_FOUND_DETAIL).isEqualTo(unauthResp.detail());
      assertThat(USER_NOT_FOUND_CODE).isEqualTo(unauthResp.code());
    });
  }
}
