package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage extends BasePage {

  private final SelenideElement usernameInput = $("[data-testid=username-input]");
  private final SelenideElement passwordInput = $("[data-testid=password-input]");
  private final SelenideElement loginButton = $("[data-testid=submit-button]");

  @Step("Login with name '{username}' and password '{password}'")
  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    loginButton.click();
    return new MainPage();
  }

  @Step("[UI] Verify logout redirects to login page")
  public void checkUserLogout() {
    loginButton.shouldHave(visible);
    header.profile().shouldHave(text("Регистрация"));
  }

}
