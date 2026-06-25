package msl.qa.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import msl.qa.pages.components.ReviewCard;
import msl.qa.pages.components.ReviewForm;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;

public class ClubPage extends BasePage {

  private final SelenideElement clubHeader = $(".club-header h1");
  private final SelenideElement year = $(".club-header span");
  private final SelenideElement authors = $(".authors");
  private final SelenideElement description = $(".description");
  private final SelenideElement telegramBtn = $(".telegram-btn");
  private final SelenideElement leaveBtn = $(".leave-btn");
  private final ElementsCollection statItem = $$(".stat-item");

  private final SelenideElement reviewBtn = $(".add-review-btn");

  private final SelenideElement error = $(".error");
  private final String errorUser = "Не удалось покинуть клуб";
  private final String errorClub = "Не удалось загрузить информацию о клубе";

  private ReviewForm reviewForm;
  private ReviewCard reviewCard;

  public ReviewForm reviewForm() {
    if (reviewForm == null) {
      reviewForm = new ReviewForm();
    }
    return reviewForm;
  }

  public ReviewCard reviewCard() {
    if (reviewCard == null) {
      reviewCard = new ReviewCard();
    }
    return reviewCard;
  }

  @Step("[UI] Impossible to leave owner's club ")
  public void leaveOwnClub() {
    leaveBtn.click();
    confirm();//close alert
    error.shouldHave(text(errorUser));
  }

  @Step("[UI] Possible to leave another's club")
  public void leaveAnothersClub() {
    leaveBtn.click();
    confirm();
    telegramBtn.shouldBe(disappear);
  }

  @Step("[UI] Verify club title, authors, year, description")
  public void assertClubIsExist(String bookTitle, String bookAuthors, Integer bookYear, String bookDescription) {
    clubHeader.shouldHave(text(bookTitle));
    authors.shouldHave(text("Автор(ы): " + bookAuthors));
    year.shouldHave(text(bookYear.toString()));
    description.shouldHave(text(bookDescription));
  }

  @Step("[UI] Verify deleted club is not exist")
  public void checkDeletedClub() {
    error.shouldHave(text(errorClub));
  }

  @Step("[UI] Count club members")
  public int getMembersCount() {
    String str = statItem.findBy(text("Участников:")).$(".stat-value").getText();
    return Integer.parseInt(str);
  }

  @Step("[UI] Count club reviews")
  public int getReviewsCount() {
    String str = statItem.findBy(text("Отзывов:")).$(".stat-value").getText();
    return Integer.parseInt(str);
  }

  @Step("[UI] Open write review form")
  public ReviewForm addReview() {
    reviewBtn.scrollTo().click();
    return new  ReviewForm();
  }

}
