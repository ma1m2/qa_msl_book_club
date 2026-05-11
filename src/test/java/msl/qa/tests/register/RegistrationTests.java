package msl.qa.tests.register;

import msl.qa.models.register.DetailRespModel;
import msl.qa.models.register.ExistingUser400RespModel;
import msl.qa.models.register.RegistrationReqModel;
import msl.qa.models.register.RegistrationRespModel;
import msl.qa.tests.TestBase;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.EXISTING_USER_ERROR;
import static msl.qa.tests.TestData.IP_REGEXP;
import static msl.qa.tests.TestData.UNSUPPORTED_MEDIA_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationTests extends TestBase {
  String username;
  String password;
  RegistrationReqModel registrationData;

  @BeforeEach
  public void prepareTestData() {
    Faker faker = new Faker();
    username = faker.name().firstName() + "qa";
    password = "1234";
    registrationData = new RegistrationReqModel(username, password);
  }

  @Test
  public void successfulRegistrationTest() {
      final RegistrationRespModel respModel = api.users.register(registrationData);

      assertThat(respModel.username()).isEqualTo(username);
      assertThat(respModel.id()).isGreaterThan(0);
      assertThat(respModel.firstName()).isEmpty(); //isEqualTo("");
      assertThat(respModel.lastName()).isEmpty();
      assertThat(respModel.email()).isEmpty();

      assertThat(respModel.remoteAddr()).matches(IP_REGEXP);
  }

  @Test
  public void existingUser400RegistrationTest() {

    step("Register existing user", () -> {
      api.users.register(registrationData);
      ExistingUser400RespModel resp = api.users.registerExistingUser(registrationData);

      String actualdError = resp.username().getFirst();
      assertThat(actualdError).isEqualTo(EXISTING_USER_ERROR);
    });
  }

  @Test
  public void invalidUsername400RegistrationTest() {
    Faker faker = new Faker();
    String username = faker.name().firstName() + "#";
    RegistrationReqModel data = new RegistrationReqModel(username, password);

    api.users.invalidUserName(data);
  }

  @Test
  public void wrongUrl500RegistrationTest() {
    String wrongUrl = "https://book-club.qa.guru/api/v1/users/register";
    api.users.registerWithWrongUrl(registrationData, wrongUrl);
  }

  @Test
  public void noContentType415RegistrationTest() {
      DetailRespModel detailRespModel = api.users.noContentType(registrationData);

      assertThat(detailRespModel.detail()).contains(UNSUPPORTED_MEDIA_TYPE);

  }
}
