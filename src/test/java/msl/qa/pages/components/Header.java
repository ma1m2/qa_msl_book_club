package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import msl.qa.pages.LoginPage;
import msl.qa.pages.RegisterPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class Header {

  private final SelenideElement mainNav = $("[data-testid=main-nav]");
  private final SelenideElement signin = $("a[data-testid=signin-link]");
  private final SelenideElement signup = $("a[data-testid=signup-link]");
  private final SelenideElement profile = $("[data-testid=profile-link]");

  @Step("[UI] Click signup in header")
  public RegisterPage doRegister() {
    signup.click();
    return new RegisterPage();
  }

  @Step("[UI] Click signin in header")
  public LoginPage doLogin() {
    signin.click();
    return new LoginPage();
  }

  @Step("[UI] Verify user is logged in")
  public void verifyUserIsLoggedIn() {
    profile.shouldHave(text("Профиль"));
    mainNav.shouldBe(visible);
  }

  @Step("[UI] Verify signup link is visible")
  public void verifySignupLinkVisible() {
    signup.shouldHave(text("Регистрация"));
  }

}
