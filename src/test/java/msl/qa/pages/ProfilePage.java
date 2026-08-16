package msl.qa.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ProfilePage extends BasePage {

  private final SelenideElement loginInfo = $(".info-item .value");
  private final SelenideElement logoutBtn = $(".logout-btn");

  @Step("[UI] Open profile page")
  public ProfilePage open() {
    Selenide.open("/profile");
    return this;
  }

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
