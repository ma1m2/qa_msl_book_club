package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
  private final SelenideElement username = $("[data-testid=username-input]");
  private final SelenideElement password = $("[data-testid=password-input]");
  private final SelenideElement confirmPassword = $("[data-testid=confirm-password-input]");
  private final SelenideElement signupBtn = $("[data-testid=signup-button]");

  @Step("Fill username field with '{0}'")
  public void fillUsername(String name) {
    username.setValue(name);
  }

  @Step("Fill password field with '{0}'")
  public void fillPassword(String secret) {
    password.setValue(secret);
  }

  @Step("Fill confirmPassword field with '{0}'")
  public void fillConfirmPassword(String secret) {
    confirmPassword.setValue(secret);
  }

  @Step("Fill Register Form with name '{name}' and password '{secret}'")
  public void fillRegisterForm(String name, String secret) {
    username.setValue(name);
    password.setValue(secret);
    confirmPassword.setValue(secret);
  }

  @Step("Submit Registration")
  public MainPage submitRegistration() {
    signupBtn.click();
    return new  MainPage();
  }

}
