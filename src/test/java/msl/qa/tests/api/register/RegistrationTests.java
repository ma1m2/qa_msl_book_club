package msl.qa.tests.api.register;

import io.qameta.allure.Feature;
import msl.qa.models.DetailRespModel;
import msl.qa.models.register.ExistingUser400RespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.tests.TestBase;
import net.datafaker.Faker;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.EXISTING_USER_ERROR;
import static msl.qa.tests.TestData.IP_REGEXP;
import static msl.qa.tests.TestData.UNSUPPORTED_MEDIA_TYPE;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("[API] Register")
@Tag("api")
public class RegistrationTests extends TestBase {

  @Test
  @DisplayName("[API] Successful Registration 201")
  public void successfulRegistrationTest() {
      final RegisterRespModel respModel = api.users.register(td.registrationData());

      assertThat(respModel.username()).isEqualTo(td.username());
      assertThat(respModel.id()).isGreaterThan(0);
      assertThat(respModel.firstName()).isEmpty(); //isEqualTo("");
      assertThat(respModel.lastName()).isEmpty();
      assertThat(respModel.email()).isEmpty();

      assertThat(respModel.remoteAddr()).matches(IP_REGEXP);
  }

  @Test
  @DisplayName("[API]  Existing user registration fails 400")
  public void existingUser400RegistrationTest() {

    step("Register existing user", () -> {
      api.users.register(td.registrationData());
      ExistingUser400RespModel resp = api.users.registerExistingUser(td.registrationData());

      String actualdError = resp.username().getFirst();
      assertThat(actualdError).isEqualTo(EXISTING_USER_ERROR);
    });
  }

  @Test
  @DisplayName("[API] Invalid Username registration fails 400")
  public void invalidUsername400RegistrationTest() {
    Faker faker = new Faker();
    String username = faker.name().firstName() + "#";
    RegisterReqModel data = new RegisterReqModel(username, td.password());

    api.users.invalidUserName(data);
  }

  @Test
  @DisplayName("[API] Wrong Url registration fails 500")
  @Disabled("java.net.ConnectException: Connection timed out: connect")
  public void wrongUrl500RegistrationTest() {
    String wrongUrl = "https://book-club.qa.guru/api/v1/users/register";
    api.users.registerWithWrongUrl(td.registrationData(), wrongUrl);
  }

  @Test
  @DisplayName("[API] No Content Type registration fails 415")
  public void noContentType415RegistrationTest() {
      DetailRespModel detailRespModel = api.users.noContentType(td.registrationData());

      assertThat(detailRespModel.detail()).contains(UNSUPPORTED_MEDIA_TYPE);

  }
}
