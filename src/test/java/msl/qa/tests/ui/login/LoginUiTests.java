package msl.qa.tests.ui.login;

import io.qameta.allure.Feature;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@Feature("[UI] Login")
@Tag("ui")
public class LoginUiTests extends TestBase {

  @Test
  @Tag("positive")
  @Tag("smoke")
  @DisplayName("[UI] Successful Login")
  public void successfulLoginTest() {
    api.users.register(td.registrationData());
    open("");
    clubsPage.header()
            .doLogin()
            .login(td.username(), td.password())
            .authorisedUserOnMainPage();
  }

}
