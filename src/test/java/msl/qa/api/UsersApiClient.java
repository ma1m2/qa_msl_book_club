package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.register.DetailRespModel;
import msl.qa.models.register.ExistingUser400RespModel;
import msl.qa.models.register.RegistrationReqModel;
import msl.qa.models.register.RegistrationRespModel;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.register.RegistrationSpec.existingUserRespSpec;
import static msl.qa.spec.register.RegistrationSpec.invalidUserNameRespSpec;
import static msl.qa.spec.register.RegistrationSpec.noContentTypeReqSpec;
import static msl.qa.spec.register.RegistrationSpec.noContentTypeRespSpec;
import static msl.qa.spec.register.RegistrationSpec.registerReqSpec;
import static msl.qa.spec.register.RegistrationSpec.registerRespSpec;
import static msl.qa.spec.register.RegistrationSpec.wrongUrlRespSpec;
import static msl.qa.tests.TestData.ENTER_VALID_USERNAME;
import static org.hamcrest.Matchers.containsString;

public class UsersApiClient {
  private final String URL =  "/users/register/";

  @Step("Successful register new user")
  public RegistrationRespModel register(RegistrationReqModel registrationData) {
    return given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(URL)
            .then()
            .spec(registerRespSpec)
            .extract().as(RegistrationRespModel.class);
  }

  @Step("Have error trying register exist user")
  public ExistingUser400RespModel registerExistingUser(RegistrationReqModel registrationData) {
    return given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(URL)
            .then()
            .spec(existingUserRespSpec)
            .extract().as(ExistingUser400RespModel.class);
  }

  @Step("Register new user with invalid username")
  public void invalidUserName(RegistrationReqModel data) {
    given(registerReqSpec)
            .body(data)
            .when()
            .post(URL)
            .then()
            .spec(invalidUserNameRespSpec)
            .body("username[0]", containsString(ENTER_VALID_USERNAME));
  }

  @Step("Have 500 status code using wrong URL (no / in the end ")
  public void registerWithWrongUrl(RegistrationReqModel registrationData, String url) {
    given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(url)
            .then()
            .spec(wrongUrlRespSpec);
  }

  @Step("Register new user without ContentType JSON")
  public DetailRespModel noContentType (RegistrationReqModel registrationData) {
    return given(noContentTypeReqSpec)
            .body(registrationData)
            .when()
            .post(URL)
            .then()
            .spec(noContentTypeRespSpec)
            .extract().as(DetailRespModel.class);
  }

}
