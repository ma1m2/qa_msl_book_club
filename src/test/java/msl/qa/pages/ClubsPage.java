package msl.qa.pages;

import io.qameta.allure.Step;
import msl.qa.pages.components.Header;

import static com.codeborne.selenide.Selenide.open;

public class ClubsPage extends BasePage{

  @Step("Verify successful authorization")
  public void authorisedUserOnMainPage() {
    header.verifyUserIsLoggedIn();
  }

  @Step("[UI] Open club with id '{id}'")
  public ClubPage openClubById(Integer id) {
    open("/clubs/" + id);
    return new ClubPage();
  }

  @Step("[UI] Open Profile")
  public ProfilePage openProfilePage() {
    open("/profile");
    return new ProfilePage();
  }

  @Step("[UI] go to header")
  public Header header() {
    return header;
  }
}
