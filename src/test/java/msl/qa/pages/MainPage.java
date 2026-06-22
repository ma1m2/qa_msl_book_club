package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import msl.qa.allure.Attach;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;

public class MainPage {

  private final SelenideElement profile = $("[data-testid=profile-link]");
  private final SelenideElement header = $("[data-testid=header]");

  @Step("Сhecking successful authorization")
  public void authorisedUserOnMainPage() {
    profile.shouldHave(text("Профиль"));
    header.shouldHave(visible);
  }

  @Step("[UI] Open app with localStorage authorisation")
  public void openWithAuth(String authJson) {
    //Attach.attachAsText("JSON for authorisation of localStorage",authJson);
    open("/favicon.ico");
    localStorage().setItem("book_club_auth", authJson);
    open("/");
  }

  @Step("[UI] Open club with id '{id}'")
  public ClubPage openClubById(Integer id) {
    open("/clubs/" + id);
    return new ClubPage();
  }

}
