package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.login.EmptyPasswordLoginRespModel;
import msl.qa.models.login.EmptyUserLoginRespModel;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.login.WrongCredlsLoginRespModel;
import msl.qa.models.logout.LogoutReqModel;
import msl.qa.models.logout.WrongTokenLogoutRespModel;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.emptyPasswordLoginRespSpec;
import static msl.qa.spec.login.LoginSpec.emptyUserLoginRespSpec;
import static msl.qa.spec.login.LoginSpec.loginReqSpec;
import static msl.qa.spec.login.LoginSpec.successLoginRespSpec;
import static msl.qa.spec.login.LoginSpec.wrongCredlsLoginRespSpec;
import static msl.qa.spec.logout.LogoutSpec.logoutReqSpec;
import static msl.qa.spec.logout.LogoutSpec.successLogoutRespSpec;
import static msl.qa.spec.logout.LogoutSpec.wrongTokenLogoutRespSpec;

public class AuthApiClient {
  private static final  String LOGIN_URL = "/auth/token/";
  private static final  String LOGOUT_URL = "/auth/logout/";

  //------------------LOGIN---------------------------
  @Step("Successful login")
  public LoginRespModel successfulLogin(LoginReqModel loginData) {
    return given(loginReqSpec)
            .body(loginData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);
  }

  @Step("Check that trim function cut spaces in the end and in the beginning")
  public LoginRespModel checkTrimFunction(LoginReqModel loginDataForTrim) {
    return given(loginReqSpec)
            .body(loginDataForTrim)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);
  }

  @Step("Change username from lowCase to upperCase")
  public WrongCredlsLoginRespModel upperCaseUser(LoginReqModel loginDataForTrim) {
    return given(loginReqSpec)
            .body(loginDataForTrim)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(wrongCredlsLoginRespSpec)
            .extract().as(WrongCredlsLoginRespModel.class);
  }

  @Step("Login with wrong password")
  public WrongCredlsLoginRespModel wrongPassword (LoginReqModel wrongPasswordData){
    return given(loginReqSpec)
            .body(wrongPasswordData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(wrongCredlsLoginRespSpec)
            .extract().as(WrongCredlsLoginRespModel.class);
  }

  @Step("Password is empty string")
  public EmptyPasswordLoginRespModel emptyPassword (LoginReqModel emptyPasswordData){
    return given(loginReqSpec)
            .body(emptyPasswordData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(emptyPasswordLoginRespSpec)
            .extract().as(EmptyPasswordLoginRespModel.class);
  }

  @Step("Username is empty string")
  public EmptyUserLoginRespModel emptyUser (LoginReqModel emptyUserData){
    return given(loginReqSpec)
            .body(emptyUserData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(emptyUserLoginRespSpec)
            .extract().as(EmptyUserLoginRespModel.class);
  }

  @Step("Login and get refresh-token")
  public String extractRefreshToken(LoginReqModel loginData){
    return given(loginReqSpec)
            .body(loginData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("refresh");
  }

  @Step("Login and get access-token")
  public String extractAccessToken(LoginReqModel loginData){
    return given(loginReqSpec)
            .body(loginData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("access");
  }

  //------------------LOGOUT---------------------------
  @Step("Logout with refresh-token")
  public void logout(LogoutReqModel logoutData) {
    given(logoutReqSpec)
            .body(logoutData)
            .when()
            .post(LOGOUT_URL)
            .then()
            .spec(successLogoutRespSpec);
  }

  @Step("Logout with wrong-token")
  public WrongTokenLogoutRespModel logoutWithWrongToken(LogoutReqModel logoutData) {
    return given(logoutReqSpec)
            .body(logoutData)
            .when()
            .post(LOGOUT_URL)
            .then()
            .spec(wrongTokenLogoutRespSpec)
            .extract().as(WrongTokenLogoutRespModel.class);
  }

}
