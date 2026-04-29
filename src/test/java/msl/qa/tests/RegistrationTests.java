package msl.qa.tests;

import msl.qa.models.register.DetailRespModel;
import msl.qa.models.register.ExistingUser400RespModel;
import msl.qa.models.register.RegistrationReqModel;
import msl.qa.models.register.RegistrationRespModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.register.RegistrationSpec.*;
import static msl.qa.tests.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;

public class RegistrationTests extends TestBase{
  String url = "/users/register/";
  String username;
  String password;
  RegistrationReqModel registrationData;

  @BeforeEach
  public void prepareTestData() {
    Faker faker = new Faker();
    username = faker.name().firstName();
    password = "1234";
    registrationData = new RegistrationReqModel(username, password);
  }

  @Test
  public void successfulRegistrationTest() {

    RegistrationRespModel respModel = given(registerReqSpec)
            .body(registrationData)
            .when()
            .post(url)
            .then()
            .spec(registerRespSpec)
            .extract().as(RegistrationRespModel.class);

    assertThat(respModel.username()).isEqualTo(username);
    assertThat(respModel.id()).isGreaterThan(0);
    assertThat(respModel.firstName()).isEmpty(); //isEqualTo("");
    assertThat(respModel.lastName()).isEmpty();
    assertThat(respModel.email()).isEmpty();

    assertThat(respModel.remoteAddr()).matches(IP_REGEXP);
  }

  @Test
  public void existingUser400RegistrationTest() {
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given(registerReqSpec)
            .body(data)
            .when()
            .post(url)
            .then()
            .log().all()
            .spec(registerRespSpec);

    ExistingUser400RespModel resp = given(registerReqSpec)
            .body(data)
            .when()
            .post(url)
            .then()
            .spec(existingUserRespSpec)
            .extract().as(ExistingUser400RespModel.class);

    String actualdError = resp.username().getFirst();
    assertThat(actualdError).isEqualTo(EXISTING_USER_ERROR);
  }

  @Test
  public void invalidUsername400RegistrationTest() {
    Faker faker = new Faker();
    String username = faker.name().firstName() + "#";
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given(registerReqSpec)
            .body(data)
            .when()
            .post(url)
            .then()
            .spec(invalidUserNameRespSpec)
            .body("username[0]", containsString(ENTER_VALID_USERNAME));
  }

  @Test
  public void wrongUrl500RegistrationTest() {
    String wrongUrl = "https://book-club.qa.guru/api/v1/users/register";
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    given(registerReqSpec)
            .body(data)
            .when()
            .post(wrongUrl)
            .then()
            .spec(wrongUrlRespSpec);
  }

  @Test
  public void noContentType415RegistrationTest() {
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    DetailRespModel detailRespModel = given(noContentTypeReqSpec)
            .body(data)
            .when()
            .post(url)
            .then()
            .spec(noContentTypeRespSpec)
            .extract().as(DetailRespModel.class);

    assertThat(detailRespModel.detail()).contains(UNSUPPORTED_MEDIA_TYPE);
  }
}
