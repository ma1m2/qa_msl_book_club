package msl.qa.tests;

import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegistrationReqModel;
import msl.qa.models.register.RegistrationRespModel;
import msl.qa.models.user.UnauthorisedUserRespModel;
import msl.qa.models.user.UpdateRespModel;
import msl.qa.models.user.UpdateUserReqModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
  String lastName;
  String email;
  RegistrationReqModel registrationData;
  UpdateUserReqModel updateUserData;
  LoginReqModel loginData;

  @BeforeEach
  public void prepareTestData() {
    Faker faker = new Faker();
    username = faker.name().firstName() + "sssss";
    password = PASSWORD;
    firstName = faker.name().firstName();
    lastName = faker.name().lastName();
    email = faker.internet().emailAddress();
  }

  @Test
  public void updateUserPutTest() {
    //create new user
    registrationData = new RegistrationReqModel(username, password);
    RegistrationRespModel respModel = given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(urlRegister)
            .then()
            .spec(registerRespSpec)
            .extract().as(RegistrationRespModel.class);
    //authorise new user
    loginData = new LoginReqModel(username, PASSWORD);
    LoginRespModel loginResp = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);
    String token = loginResp.access();
    System.out.println("### token: " + token);
    //update user with token
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

  }

  @Test
  public void updateUserPatchTest() {
    //create new user
    registrationData = new RegistrationReqModel(username, password);
    RegistrationRespModel respModel = given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(urlRegister)
            .then()
            .spec(registerRespSpec)
            .extract().as(RegistrationRespModel.class);
    //authorise new user
    loginData = new LoginReqModel(username, PASSWORD);
    LoginRespModel loginResp = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);
    String token = loginResp.access();
    System.out.println("### token: " + token);
    //update user with token
    updateUserData = new UpdateUserReqModel(username,firstName,lastName,email);
    UpdateRespModel updatedUser = given(userReqSpec)
            .header("Authorization", "Bearer " + token)
            .body(updateUserData)
            .when()
            .patch(urlUpdate)
            .then()
            .spec(updateRespSpec)
            .extract().as(UpdateRespModel.class);

    assertThat(updatedUser.firstName()).isEqualTo(firstName);
    assertThat(updatedUser.id()).isEqualTo(respModel.id());
  }

  @Test
  public void updateUnauthorizedUserPutTest() {
    //create new user
    registrationData = new RegistrationReqModel(username, password);
    RegistrationRespModel respModel = given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(urlRegister)
            .then()
            .spec(registerRespSpec)
            .extract().as(RegistrationRespModel.class);
    //authorise new user
    loginData = new LoginReqModel(username, PASSWORD);
    LoginRespModel loginResp = given(loginReqSpec)
            .body(loginData)
            .when()
            .post(urlLogin)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);
    //update user without token
    updateUserData = new UpdateUserReqModel(username,firstName,lastName,email);
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

  }
}
