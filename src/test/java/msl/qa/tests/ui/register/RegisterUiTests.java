package msl.qa.tests.ui.register;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import msl.qa.pages.ClubsPage;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

@Feature("[UI] Register")
@Tag("ui")
public class RegisterUiTests extends TestBase {

  ClubsPage clubsPage = new ClubsPage();

  @Test
  @Tag("positive")
  @Owner("SvetaQa")
  @DisplayName("[UI] Successful Registration by UI")
  public void successfulRegistrationUi() {
    open("");
    clubsPage.header()
            .doRegister()
            .fillRegisterForm(td.username(),td.password())
            .submitRegistration()
            .authorisedUserOnMainPage();
  }
}
