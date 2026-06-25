package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ReviewForm {

  private final SelenideElement assesmentInput = $("#assessment");
  private final SelenideElement readPagesInput = $("#readPages");
  private final SelenideElement reviewInput = $("#review");
  private final SelenideElement saveBtn = $(".save-btn");
  private final SelenideElement cancelBtn = $(".cancel-btn");

  public ReviewCard fillReviewForm(Integer assesment, Integer readPages, String review) {
    assesmentInput.scrollTo().setValue(String.valueOf(assesment));
    readPagesInput.scrollTo().setValue(String.valueOf(readPages));
    reviewInput.scrollTo().setValue(review);
    saveBtn.scrollTo().click();
    return new ReviewCard();
  }

  public ReviewForm setAssessment(Integer assesment) {
    assesmentInput.setValue(String.valueOf(assesment));
    return this;
  }

  public ReviewForm setReadPages(Integer pages) {
    readPagesInput.setValue(String.valueOf(pages));
    return this;
  }

  public ReviewForm setReview(String review) {
    reviewInput.setValue(review);
    return this;
  }

  public ReviewCard clickSave() {
    saveBtn.click();
    return new  ReviewCard();
  }

}
