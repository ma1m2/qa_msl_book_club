package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.login.*;
import msl.qa.models.logout.*;
import msl.qa.models.register.RegisterReqModel;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.login.LoginSpec.*;
import static msl.qa.spec.logout.LogoutSpec.*;

public class AuthApiClient {
  private static final  String LOGIN_URL = "/auth/token/";
  private static final  String LOGOUT_URL = "/auth/logout/";

  //------------------LOGIN---------------------------
  @Step("[API] Successful login")
  public LoginRespModel successfulLogin(RegisterReqModel loginData) {
    return given(loginReqSpec)
            .body(loginData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);
  }

  @Step("[API] Trim removes edge spaces")
  public LoginRespModel checkTrimFunction(RegisterReqModel loginDataForTrim) {
    return given(loginReqSpec)
            .body(loginDataForTrim)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().as(LoginRespModel.class);
  }

  @Step("[API] Change username from lowCase to upperCase")
  public WrongCredlsLoginRespModel upperCaseUser(RegisterReqModel loginDataForTrim) {
    return given(loginReqSpec)
            .body(loginDataForTrim)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(wrongCredlsLoginRespSpec)
            .extract().as(WrongCredlsLoginRespModel.class);
  }

  @Step("[API] Login with wrong password")
  public WrongCredlsLoginRespModel wrongPassword (RegisterReqModel wrongPasswordData){
    return given(loginReqSpec)
            .body(wrongPasswordData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(wrongCredlsLoginRespSpec)
            .extract().as(WrongCredlsLoginRespModel.class);
  }

  @Step("[API] Password is empty string")
  public EmptyPasswordLoginRespModel emptyPassword (RegisterReqModel emptyPasswordData){
    return given(loginReqSpec)
            .body(emptyPasswordData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(emptyPasswordLoginRespSpec)
            .extract().as(EmptyPasswordLoginRespModel.class);
  }

  @Step("[API] Username is empty string")
  public EmptyUserLoginRespModel emptyUser (RegisterReqModel emptyUserData){
    return given(loginReqSpec)
            .body(emptyUserData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(emptyUserLoginRespSpec)
            .extract().as(EmptyUserLoginRespModel.class);
  }

  @Step("[API] Login and get refresh-token")
  public String extractRefreshToken(RegisterReqModel loginData){
    return given(loginReqSpec)
            .body(loginData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("refresh");
  }

  @Step("[API] Login and get access-token")
  public String extractAccessToken(RegisterReqModel loginData){
    return given(loginReqSpec)
            .body(loginData)
            .when()
            .post(LOGIN_URL)
            .then()
            .spec(successLoginRespSpec)
            .extract().path("access");
  }

  //------------------LOGOUT---------------------------
  @Step("[API] Logout with refresh-token")
  public void logout(LogoutReqModel logoutData) {
    given(logoutReqSpec)
            .body(logoutData)
            .when()
            .post(LOGOUT_URL)
            .then()
            .spec(successLogoutRespSpec);
  }

  @Step("[API] Logout with wrong-token")
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
