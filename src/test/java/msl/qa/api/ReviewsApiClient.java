package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.clubs.review.BookReviewRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import static io.restassured.RestAssured.given;
import static msl.qa.spec.review.ReviewsSpec.createReviewRespSpec;
import static msl.qa.spec.review.ReviewsSpec.reviewsReqSpec;

public class ReviewsApiClient {

  private static final String REVIEWS_URL = "/clubs/reviews/";
  //-----------------------------CREATE----------------------------
  @Step("[API] Create new Review")
  public BookReviewRespModel createReview(String accessToken, ReviewReqModel createReviewData) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .body(createReviewData)
            .when()
            .post(REVIEWS_URL)
            .then()
            .spec(createReviewRespSpec)
            .extract().as(BookReviewRespModel.class);
  }
  //-----------------------------READ----------------------------

  //-----------------------------UPDATE----------------------------

  //-----------------------------DELETE----------------------------
}
