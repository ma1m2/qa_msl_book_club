package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.register.DetailRespModel;
import msl.qa.models.register.ExistingUser400RespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.models.register.RegistrationRespModel;
import msl.qa.models.user.DetailCodeRespModel;
import msl.qa.models.user.PatchUserReqModel;
import msl.qa.models.user.UnauthorisedUserRespModel;
import msl.qa.models.user.UpdateRespModel;
import msl.qa.models.user.UpdateUserReqModel;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.register.RegistrationSpec.existingUserRespSpec;
import static msl.qa.spec.register.RegistrationSpec.invalidUserNameRespSpec;
import static msl.qa.spec.register.RegistrationSpec.noContentTypeReqSpec;
import static msl.qa.spec.register.RegistrationSpec.noContentTypeRespSpec;
import static msl.qa.spec.register.RegistrationSpec.registerReqSpec;
import static msl.qa.spec.register.RegistrationSpec.registerRespSpec;
import static msl.qa.spec.register.RegistrationSpec.wrongUrlRespSpec;
import static msl.qa.spec.user.UserSpec.updateRespSpec;
import static msl.qa.spec.user.UserSpec.updateUnauthorizedUserRespSpec;
import static msl.qa.spec.user.UserSpec.userReqSpec;
import static msl.qa.tests.TestData.ENTER_VALID_USERNAME;
import static org.hamcrest.Matchers.containsString;

public class UsersApiClient {
  private final String REGISTER_URL =  "/users/register/";
  private final String ME_URL = "/users/me/";

  //-----------------------------CREATE----------------------------
  @Step("[API] Successful register new user")
  public RegistrationRespModel register(RegisterReqModel registrationData) {
    return given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(REGISTER_URL)
            .then()
            .spec(registerRespSpec)
            .extract().as(RegistrationRespModel.class);
  }

  @Step("[API] Have error trying register existing user")
  public ExistingUser400RespModel registerExistingUser(RegisterReqModel registrationData) {
    return given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(REGISTER_URL)
            .then()
            .spec(existingUserRespSpec)
            .extract().as(ExistingUser400RespModel.class);
  }

  @Step("[API] Register new user with invalid username")
  public void invalidUserName(RegisterReqModel data) {
    given(registerReqSpec)
            .body(data)
            .when()
            .post(REGISTER_URL)
            .then()
            .spec(invalidUserNameRespSpec)
            .body("username[0]", containsString(ENTER_VALID_USERNAME));
  }

  @Step("[API] Have 500 status code using wrong URL (no / in the end ")
  public void registerWithWrongUrl(RegisterReqModel registrationData, String url) {
    given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(url)
            .then()
            .spec(wrongUrlRespSpec);
  }

  @Step("[API] Register new user without ContentType JSON")
  public DetailRespModel noContentType (RegisterReqModel registrationData) {
    return given(noContentTypeReqSpec)
            .body(registrationData)
            .when()
            .post(REGISTER_URL)
            .then()
            .spec(noContentTypeRespSpec)
            .extract().as(DetailRespModel.class);
  }

  //-----------------------------READ----------------------------

  //-----------------------------UPDATE----------------------------
  @Step("[API] Update user with PUT method")
  public UpdateRespModel updateUserPUT (UpdateUserReqModel updateUserData, String token) {
    return given(userReqSpec)
            .header("Authorization", "Bearer " + token)
            .body(updateUserData)
            .when()
            .put(ME_URL)
            .then()
            .spec(updateRespSpec)
            .extract().as(UpdateRespModel.class);
  }

  @Step("[API] Update user with PATCH method")
  public UpdateRespModel updateUserPATHCH (PatchUserReqModel patchUpdateUserData, String token) {
    return  given(userReqSpec)
            .header("Authorization", "Bearer " + token)
            .body(patchUpdateUserData)
            .when()
            .patch(ME_URL)
            .then()
            .spec(updateRespSpec)
            .extract().as(UpdateRespModel.class);
  }

  @Step("[API] Update user without token")
  public UnauthorisedUserRespModel updateUnauthorizedUserPUT (UpdateUserReqModel updateUserData) {
    return given(userReqSpec)
            .header("Authorization", "Bearer ")
            .body(updateUserData)
            .when()
            .put(ME_URL)
            .then()
            .spec(updateUnauthorizedUserRespSpec)
            .extract().as(UnauthorisedUserRespModel.class);
  }

  //-----------------------------DELETE----------------------------
  @Step("[API] Delete user status code 204")
  public void deleteUser (String token) {
    given()
    .header("Authorization", "Bearer " + token)
            .spec(userReqSpec)
            .delete(ME_URL)
            .then()
            .log().all()
            .statusCode(204);
  }

  @Step("[API] Deleting deleted user status code 401")
  public DetailCodeRespModel deleteUnfoundUser401 (String token) {
    return given()
            .header("Authorization", "Bearer " + token)
            .spec(userReqSpec)
            .delete(ME_URL)
            .then()
            .spec(updateUnauthorizedUserRespSpec)
            .statusCode(401)
            .extract().as(DetailCodeRespModel.class);
  }

}
