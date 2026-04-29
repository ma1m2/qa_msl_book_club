package msl.qa.tests;

import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegistrationReqModel;
import msl.qa.models.register.RegistrationRespModel;
import msl.qa.models.user.PatchUserReqModel;
import msl.qa.models.user.UnauthorisedUserRespModel;
import msl.qa.models.user.UpdateRespModel;
import msl.qa.models.user.UpdateUserReqModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.*;
import static msl.qa.spec.register.RegistrationSpec.*;
import static msl.qa.spec.user.UserSpec.*;
import static msl.qa.tests.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserTests extends TestBase {
  String urlUpdate = "/users/me/";
  String urlRegister = "/users/register/";
  String urlLogin = "/auth/token/";
  String username;
  String password;
  String firstName;
  String updatedFirstName;
  String lastName;
  String email;
  RegistrationReqModel registrationData;
  UpdateUserReqModel updateUserData;
  PatchUserReqModel patchUpdateUserData;
  LoginReqModel loginData;

  @BeforeEach
  public void prepareTestData() {
    Faker faker = new Faker();
    username = faker.name().firstName() + "sssss";
    password = PASSWORD;
    firstName = faker.name().firstName();
    updatedFirstName = faker.name().firstName();
    lastName = faker.name().lastName();
    email = faker.internet().emailAddress();
  }

  @Test
  public void updateUserPutTest() {
    registrationData = new RegistrationReqModel(username, password);

    RegistrationRespModel respModel = step("Create new user", () ->
      given(registerReqSpec)
              .body(registrationData)
              .when()
              .post(urlRegister)
              .then()
              .spec(registerRespSpec)
              .extract().as(RegistrationRespModel.class));

    loginData = new LoginReqModel(username, PASSWORD);

    LoginRespModel loginResp = step("Authorise new user", () ->
      given(loginReqSpec)
              .body(loginData)
              .when()
              .post(urlLogin)
              .then()
              .spec(successLoginRespSpec)
              .extract().as(LoginRespModel.class));

    String token = loginResp.access();
    System.out.println("### token: " + token);

    step("Update user with PUT method", () -> {
    updateUserData = new UpdateUserReqModel(username,firstName,lastName,email);
      UpdateRespModel updatedUser = given(userReqSpec)
              .header("Authorization", "Bearer " + token)
              .body(updateUserData)
              .when()
              .put(urlUpdate)
              .then()
              .spec(updateRespSpec)
              .extract().as(UpdateRespModel.class);

      assertThat(updatedUser.firstName()).isEqualTo(firstName);
      assertThat(updatedUser.id()).isEqualTo(respModel.id());
    });
  }

  @Test
  public void updateUserPatchTest() {
    registrationData = new RegistrationReqModel(username, password);

    RegistrationRespModel respModel = step("Create new user", () ->
      given(registerReqSpec)
              .body(registrationData)
              .when()
              .post(urlRegister)
              .then()
              .spec(registerRespSpec)
              .extract().as(RegistrationRespModel.class));

    loginData = new LoginReqModel(username, PASSWORD);

    LoginRespModel loginResp = step("Authorise new user", () ->
            given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class));

    String token = loginResp.access();
    System.out.println("### token: " + token);

    step("Update user with PATCH method", () -> {
      patchUpdateUserData = new PatchUserReqModel(null,updatedFirstName,null,null);
      PatchUserReqModel updatedUser = given(userReqSpec)
              .header("Authorization", "Bearer " + token)
              .body(patchUpdateUserData)
              .when()
              .patch(urlUpdate)
              .then()
              .spec(updateRespSpec)
              .extract().as(PatchUserReqModel.class);

      assertThat(updatedUser.firstName()).isEqualTo(updatedFirstName);
    });
  }

  @Test
  public void updateUnauthorizedUserPutTest() {

    registrationData = new RegistrationReqModel(username, password);

    step("Create new user", () -> given(registerReqSpec)
              .body(registrationData)
              .when()
              .post(urlRegister)
              .then()
              .spec(registerRespSpec));

    loginData = new LoginReqModel(username, PASSWORD);

    step("Authorise new user", () -> given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec));

    updateUserData = new UpdateUserReqModel(username,firstName,lastName,email);

    step("Update new user with PUT method without token", () -> {
    UnauthorisedUserRespModel updatedUser = given(userReqSpec)
            .header("Authorization", "Bearer ")
            .body(updateUserData)
            .when()
            .put(urlUpdate)
            .then()
            .spec(updateUnauthorizedUserRespSpec)
            .extract().as(UnauthorisedUserRespModel.class);

    assertThat(updatedUser.detail()).isEqualTo(AUTHORIZATION_HEADER_DETAIL);
    assertThat(updatedUser.code()).isEqualTo(BAD_HEADER_CODE);
    });
  }
}
