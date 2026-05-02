package msl.qa.tests;

import io.restassured.response.Response;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegistrationReqModel;
import msl.qa.models.register.RegistrationRespModel;
import msl.qa.models.user.PatchUserReqModel;
import msl.qa.models.user.UnauthorisedUserRespModel;
import msl.qa.models.user.UpdateRespModel;
import msl.qa.models.user.UpdateUserReqModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.loginReqSpec;
import static msl.qa.spec.login.LoginSpec.successLoginRespSpec;
import static msl.qa.spec.register.RegistrationSpec.registerReqSpec;
import static msl.qa.spec.register.RegistrationSpec.registerRespSpec;
import static msl.qa.spec.user.UserSpec.updateRespSpec;
import static msl.qa.spec.user.UserSpec.updateUnauthorizedUserRespSpec;
import static msl.qa.spec.user.UserSpec.userReqSpec;
import static msl.qa.tests.TestData.AUTHORIZATION_HEADER_DETAIL;
import static msl.qa.tests.TestData.BAD_HEADER_CODE;
import static msl.qa.tests.TestData.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserTests extends TestBase {

  //RegistrationReqModel registrationData;
  UpdateUserReqModel updateUserData;
  PatchUserReqModel patchUpdateUserData;
  //LoginReqModel loginData;

  @AfterEach
  public void cleanupTestData() {
    api.users.deleteUser(token);
  }

  @Test
  public void updateUserPutTest() {
    step("Update user", () -> {
      registrationData = new RegistrationReqModel(username, password);

      RegistrationRespModel respModel = api.users.register(registrationData);

      loginData = new LoginReqModel(username, PASSWORD);

      LoginRespModel loginResp = api.auth.successfulLogin(loginData);

      token = loginResp.access();
      System.out.println("### token: " + token);

      updateUserData = new UpdateUserReqModel(username,firstName,lastName,email);

      UpdateRespModel updatedUser = api.users.updateUserPUT(updateUserData, token);

      assertThat(updatedUser.firstName()).isEqualTo(firstName);
      assertThat(updatedUser.id()).isEqualTo(respModel.id());
    });
  }

  @Test
  public void updateUserPatchTest() {

    step("Update user with PATCH method", () -> {

      registrationData = new RegistrationReqModel(username, password);
      api.users.register(registrationData);

      loginData = new LoginReqModel(username, PASSWORD);

      LoginRespModel loginResp = api.auth.successfulLogin(loginData);

      token = loginResp.access();
      System.out.println("### token: " + token);

      patchUpdateUserData = new PatchUserReqModel(null,updatedFirstName,null,null);
      UpdateRespModel updatedUser = api.users.updateUserPATHCH(patchUpdateUserData,token);

      assertThat(updatedUser.firstName()).isEqualTo(updatedFirstName);
    });
  }

  @Test
  public void updateUnauthorizedUserPutTest() {
    step("Update new user with PUT method without token", () -> {

      registrationData = new RegistrationReqModel(username, password);

      api.users.register(registrationData);

      loginData = new LoginReqModel(username, PASSWORD);

      api.auth.successfulLogin(loginData);

      updateUserData = new UpdateUserReqModel(username,firstName,lastName,email);

      UnauthorisedUserRespModel updatedUser = api.users.updateUnauthorizedUserPUT(updateUserData);

      assertThat(updatedUser.detail()).isEqualTo(AUTHORIZATION_HEADER_DETAIL);
      assertThat(updatedUser.code()).isEqualTo(BAD_HEADER_CODE);

    });
  }
}
