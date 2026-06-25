package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;
import msl.qa.pages.ClubPage;

import static com.codeborne.selenide.Selenide.$;

public class ReviewForm {

  private final SelenideElement assesmentInput = $("#assessment").scrollTo();
  private final SelenideElement readPagesInput = $("#readPages").scrollTo();
  private final SelenideElement reviewInput = $("#review").scrollTo();
  private final SelenideElement saveBtn = $(".save-btn").scrollTo();
  private final SelenideElement cancelBtn = $(".cancel-btn").scrollTo();

  public ReviewCard fillReviewForm(Integer assesment, Integer readPages, String review) {
    assesmentInput.setValue(String.valueOf(assesment));
    readPagesInput.setValue(String.valueOf(readPages));
    reviewInput.setValue(review);
    saveBtn.click();
    return new ReviewCard();
  }

}
