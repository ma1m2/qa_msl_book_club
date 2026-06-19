package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

  private final SelenideElement usernameInput = $("[data-testid=username-input]");
  private final SelenideElement passwordInput = $("[data-testid=password-input]");
  private final SelenideElement loginButton = $("[data-testid=login-button]");

  @Step("Login with name '{username}' and password '{password}'")
  public void login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    loginButton.click();
  }

}
