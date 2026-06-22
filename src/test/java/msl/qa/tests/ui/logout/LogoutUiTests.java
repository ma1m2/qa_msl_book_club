package msl.qa.tests.ui.logout;

import io.qameta.allure.Feature;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Feature("[UI] Logout")
@Tag("ui")
public class LogoutUiTests extends TestBase {

  @Test
  @Tag("positive")
  @Tag("regression")
  @DisplayName("[UI] Successful Logout")
  public void successfulLogoutTest() {
    mainPage.openMainPageWithNewUser(td.username(), td.password());
    mainPage.openProfilePage()
            .authorisedUserOnProfilePage(td.username())
            .logout()
            .checkUserLogout();
  }

}
