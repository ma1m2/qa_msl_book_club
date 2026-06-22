package msl.qa.pages;

import io.qameta.allure.Step;
import msl.qa.pages.components.Header;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;

public class MainPage extends BasePage{

  @Step("Verify successful authorization")
  public void authorisedUserOnMainPage() {
    header.profile().shouldHave(text("Профиль"));
    header.mainNav().shouldBe(visible);
  }

  @Step("[UI] Open app with localStorage authorisation")
  public void openWithAuth(String authJson) {
    open("/favicon.ico");
    localStorage().setItem("book_club_auth", authJson);
    open("/");
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
