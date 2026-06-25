package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;
import msl.qa.pages.LoginPage;
import msl.qa.pages.RegisterPage;

import static com.codeborne.selenide.Selenide.$;

public class Header {

  private final SelenideElement mainNav = $("[data-testid=main-nav]");
  private final SelenideElement clubs = $("a[data-testid=clubs-link]");
  private final SelenideElement signin = $("a[data-testid=signin-link]");
  private final SelenideElement signup = $("a[data-testid=signup-link]");
  private final SelenideElement profile = $("[data-testid=profile-link]");

  public RegisterPage doRegister(){
    signup.click();
    return new RegisterPage();
  }

  public LoginPage doLogin(){
    signin.click();
    return new LoginPage();
  }

  public SelenideElement mainNav() {
    return mainNav;
  }

  public SelenideElement profile(){
    return profile;
  }

  public SelenideElement clubs() {
    return clubs;
  }

  public SelenideElement signin() {
    return signin;
  }

  public SelenideElement signup() {
    return signup;
  }

}
