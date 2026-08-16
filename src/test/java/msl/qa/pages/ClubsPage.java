package msl.qa.pages;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

public class ClubsPage extends BasePage {

  @Step("[UI] Open clubs page")
  public ClubsPage open() {
    Selenide.open("/");
    return this;
  }

  @Step("[UI] Open login from header")
  public LoginPage openLogin() {
    return header.doLogin();
  }

  @Step("[UI] Open registration from header")
  public RegisterPage openRegister() {
    return header.doRegister();
  }

  @Step("Verify successful authorization")
  public void authorisedUserOnMainPage() {
    header.verifyUserIsLoggedIn();
  }

  @Step("[UI] Open club with id '{id}'")
  public ClubPage openClubById(Integer id) {
    return new ClubPage().open(id);
  }

  @Step("[UI] Open Profile")
  public ProfilePage openProfilePage() {
    return new ProfilePage().open();
  }
}
