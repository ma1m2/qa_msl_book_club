package msl.qa.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;

public class ClubPage extends BasePage {
  private final SelenideElement title = $(".club-header h1");
  private final SelenideElement year = $(".club-header span");
  private final SelenideElement authors = $(".authors");
  private final SelenideElement description = $(".description");
  private final SelenideElement leaveBtn = $(".leave-btn");
  private final SelenideElement joinBtn = $(".join-btn");
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

  @Step("[UI] Impossible to leave owner's club ")
  public void joinAnothesClub() {
    joinBtn.click();
    leaveBtn.shouldBe(text(" Покинуть клуб "));
  }

  @Step("[UI] Possible to leave another's club")
  public void leaveAnothersClub() {
    leaveBtn.click();
    confirm();
    telegramBtn.shouldBe(disappear);
  }

  @Step("[UI] Verify club title '{0}', authors '{1}', year '{2}', description '{3}'")
  public void assertClubIsExist(String bookTitle, String bookAuthors, Integer bookYear, String bookDescription) {
    title.shouldHave(text(bookTitle));
    authors.shouldHave(text("Автор(ы): " + bookAuthors));
    year.shouldHave(text(bookYear.toString()));
    description.shouldHave(text(bookDescription));
  }

  @Step("[UI] Verify deleted club is not exist")
  public void checkDeletedClub() {
    error.shouldHave(text(errorClub));
  }

}
