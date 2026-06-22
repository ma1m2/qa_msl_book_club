package msl.qa.tests.api.user;

import io.qameta.allure.Feature;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.user.DetailCodeRespModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.USER_NOT_FOUND_CODE;
import static msl.qa.tests.TestData.USER_NOT_FOUND_DETAIL;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("[API] Delete User")
@Tag("api")
public class DeleteUserTests extends TestBase {

  @Test
  @DisplayName("[API] Self-Delete User 204")
  public void deleteUserTest() {
    step("Delete user using token", () -> {

      api.users.register(td.registrationData());

      LoginRespModel loginResp = api.auth.successfulLogin(td.loginData());

      String token = loginResp.access();

      api.users.deleteUser(token);
    });
  }

  @Test
  @DisplayName("[API] Self-deleting Deleted User 401")
  @Tag("negative")
  public void deletingDeletedUserTest() {
    step("Try delete deleted user", () -> {

      api.users.register(td.registrationData());

      LoginRespModel loginResp = api.auth.successfulLogin(td.loginData());

      String token = loginResp.access();

      api.users.deleteUser(token);

      DetailCodeRespModel unauthResp = api.users.deleteUnfoundUser401(token);

      assertThat(USER_NOT_FOUND_DETAIL).isEqualTo(unauthResp.detail());
      assertThat(USER_NOT_FOUND_CODE).isEqualTo(unauthResp.code());
    });
  }
}
