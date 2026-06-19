package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;

public class ClubPage {
  private final SelenideElement content = $(".club-content");
  private final SelenideElement leaveBtn = $(".leave-btn");
  private final SelenideElement reviewBtn = $(".add-review-btn");
  private final SelenideElement telegramBtn = $(".telegram-btn");
  private final SelenideElement error = $(".error");
  private final String errorMessage = "Не удалось покинуть клуб";

  @Step("Impossible to leave your own club")
  public void leaveYourOwnClub() {
    leaveBtn.click();
    confirm();//close alert
    error.shouldHave(text(errorMessage));

  }

  @Step("Possible to leave another's club")
  public void leaveAnothersClub() {
    leaveBtn.click();
    //confirm();//close alert

  }
}
