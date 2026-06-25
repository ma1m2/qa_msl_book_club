package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class ReviewCard {

  private final SelenideElement reviewerName = $(".reviewer-name");
  private final SelenideElement reviewStars = $(".review-rating .stars");
  private final SelenideElement readPages = $(".review-rating .read-pages");

  private final SelenideElement reviewContent = $(".review-content");
  private final SelenideElement reviewDate = $(".review-date");//24 июня 2026 г.
  private final SelenideElement deleteReviewBtn = $(".delete-review-btn");
  private final SelenideElement editReviewBtn = $(".edit-review-btn");

  @Step("[UI] Get reviewer name")
  public String getReviewName() {
    return reviewerName.getText();
  }

  @Step("[UI] Count review stars")
  public int getReviewStars() {
    return reviewStars.text().replace("☆", "").length();
  }

  @Step("[UI] Get read pages")
  public Integer getReviewPages() {
    String[] page = readPages.getText().split(" ");
    return Integer.parseInt(page[0]);
  }

  @Step("[UI] Get review content")
  public String getReviewContent() {
    return reviewContent.getText();
  }

  @Step("[UI] Get review date")
  public LocalDate getReviewDate() {
    String dateText = reviewDate.getText();
    return LocalDate.parse(
            dateText.replace(" г.", ""),
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.of("ru")));
  }

  @Step("[UI] Verify review data")
  public void assertReviewIsExist(String username, Integer stars, Integer pages, String review, String date) {
    reviewerName.shouldHave(text(username));
    reviewStars.shouldHave(text("★".repeat(stars) + "☆".repeat(5-stars)));
    readPages.shouldHave(text(pages.toString() + " стр."));
    reviewContent.shouldHave(text(review));
    reviewDate.shouldHave(text(date));
  }

}
