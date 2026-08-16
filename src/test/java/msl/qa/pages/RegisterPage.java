package msl.qa.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage {

  private final SelenideElement username = $("[data-testid=username-input]");
  private final SelenideElement password = $("[data-testid=password-input]");
  private final SelenideElement confirmPassword = $("[data-testid=confirm-password-input]");
  private final SelenideElement signupBtn = $("[data-testid=signup-button]");

  @Step("[UI] Open registration page")
  public RegisterPage open() {
    Selenide.open("/signup");
    return this;
  }

  @Step("Fill Register Form with name '{name}' and password '{secret}'")
  public RegisterPage fillRegisterForm(String name, String secret) {
    username.setValue(name);
    password.setValue(secret);
    confirmPassword.setValue(secret);
    return this;
  }

  @Step("Submit Registration")
  public ClubsPage submitRegistration() {
    signupBtn.click();
    return new ClubsPage();
  }

}
