package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ReviewCard {

  private final SelenideElement reviewerName = $(".reviewer-name");
  private final SelenideElement reviewStars = $(".review-rating .stars");
  private final SelenideElement readPages = $(".review-rating .read-pages");
  private final SelenideElement reviewContent = $(".review-content");
  private final SelenideElement reviewDate = $(".review-date");

  @Step("[UI] Verify review data")
  public void assertReviewIsExist(String username, Integer stars, Integer pages, String review, String date) {
    reviewerName.shouldHave(text(username));
    reviewStars.shouldHave(text("★".repeat(stars) + "☆".repeat(5 - stars)));
    readPages.shouldHave(text(pages.toString() + " стр."));
    reviewContent.shouldHave(text(review));
    reviewDate.shouldHave(text(date));
  }

}
