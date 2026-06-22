package msl.qa.tests.api.user;

import io.qameta.allure.Feature;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegistrationRespModel;
import msl.qa.models.user.PatchUserReqModel;
import msl.qa.models.user.UnauthorisedUserRespModel;
import msl.qa.models.user.UpdateRespModel;
import msl.qa.models.user.UpdateUserReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.AUTHORIZATION_HEADER_DETAIL;
import static msl.qa.tests.TestData.BAD_HEADER_CODE;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("[API] Update User")
@Tag("api")
public class UpdateUserTests extends TestBase {

  UpdateUserReqModel updateUserData;
  PatchUserReqModel patchUpdateUserData;
  String token;

  @AfterEach
  public void cleanupTestData() {
    api.users.deleteUser(token);
  }

  @Test
  @DisplayName("[API] PUT: Successful update user")
  public void updateUserPutTest() {
    step("Update user", () -> {

      RegistrationRespModel respModel = api.users.register(td.registrationData());

      LoginRespModel loginResp = api.auth.successfulLogin(td.loginData());

      token = loginResp.access();
      System.out.println("### token: " + token);

      updateUserData = new UpdateUserReqModel(td.username(),td.firstname(),td.lastname(),td.email());

      UpdateRespModel updatedUser = api.users.updateUserPUT(updateUserData, token);

      assertThat(updatedUser.firstName()).isEqualTo(td.firstname());
      assertThat(updatedUser.id()).isEqualTo(respModel.id());
    });
  }

  @Test
  @DisplayName("[API] PATCH: Successful update user")
  public void updateUserPatchTest() {

    step("Update user with PATCH method", () -> {

      api.users.register(td.registrationData());

      LoginRespModel loginResp = api.auth.successfulLogin(td.loginData());

      token = loginResp.access();
      System.out.println("### token: " + token);

      patchUpdateUserData = new PatchUserReqModel(null,td.updatedFirstName(),null,null);
      UpdateRespModel updatedUser = api.users.updateUserPATHCH(patchUpdateUserData,token);

      assertThat(updatedUser.firstName()).isEqualTo(td.updatedFirstName());
    });
  }

  @Test
  @Tag("negative")
  @DisplayName("[API] PUT: Unauthorized user can't self-update 401")
  public void updateUnauthorizedUserPutTest() {
    step("Update new user with PUT method without token", () -> {

      api.users.register(td.registrationData());

      token = api.auth.successfulLogin(td.loginData()).access();

      updateUserData = new UpdateUserReqModel(td.username(),td.firstname(),td.lastname(),td.email());

      UnauthorisedUserRespModel updatedUser = api.users.updateUnauthorizedUserPUT(updateUserData);

      assertThat(updatedUser.detail()).isEqualTo(AUTHORIZATION_HEADER_DETAIL);
      assertThat(updatedUser.code()).isEqualTo(BAD_HEADER_CODE);

    });
  }
}
