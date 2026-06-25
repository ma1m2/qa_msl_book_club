package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {

  private final SelenideElement loginInfo = $(".info-item .value");
  private final SelenideElement editBtn = $(".edit-btn");
  private final SelenideElement logoutBtn = $(".logout-btn");

  @Step("Verify successful authorization on Profile Page")
  public ProfilePage authorisedUserOnProfilePage(String userName) {
    loginInfo.shouldHave(text(userName));
    return this;
  }

  @Step("Verify successful authorization")
  public LoginPage logout() {
    logoutBtn.click();
    return new LoginPage();
  }
}
