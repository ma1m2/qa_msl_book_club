package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class ReviewForm {

  private final SelenideElement assesmentInput = $("#assessment");
  private final SelenideElement readPagesInput = $("#readPages");
  private final SelenideElement reviewInput = $("#review");
  private final SelenideElement saveBtn = $(".save-btn");

  @Step("[UI] Fill and submit review form")
  public void fillReviewForm(Integer assesment, Integer readPages, String review) {
    assesmentInput.scrollTo().setValue(String.valueOf(assesment));
    readPagesInput.scrollTo().setValue(String.valueOf(readPages));
    reviewInput.scrollTo().setValue(review);
    saveBtn.scrollTo().click();
  }

}
