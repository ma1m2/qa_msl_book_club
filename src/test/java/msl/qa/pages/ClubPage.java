package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;

public class ClubPage extends BasePage {
  private final SelenideElement content = $(".club-content");
  private final SelenideElement leaveBtn = $(".leave-btn");
  private final SelenideElement reviewBtn = $(".add-review-btn");
  private final SelenideElement telegramBtn = $(".telegram-btn");
  private final SelenideElement error = $(".error");
  private final String errorUser = "Не удалось покинуть клуб";
  private final String errorClub = "Не удалось загрузить информацию о клубе";

  @Step("[UI] Impossible to leave owner's club ")
  public void leaveOwnresClub() {
    leaveBtn.click();
    confirm();//close alert
    error.shouldHave(text(errorUser));
  }

  @Step("[UI] Possible to leave another's club")
  public void leaveAnothersClub() {
    leaveBtn.click();
    confirm();
    //check something
  }

  @Step("[UI] Verify deleted club is not exist")
  public void checkDeletedClub() {
    error.shouldHave(text(errorClub));
  }

}
