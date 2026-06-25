package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.DetailRespModel;
import msl.qa.models.clubs.review.ReviewRespModel;
import msl.qa.models.clubs.review.PaginatedReviewListRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import static io.restassured.RestAssured.given;
import static msl.qa.spec.review.ReviewsSpec.*;

public class ReviewsApiClient {

  private static final String REVIEWS_URL = "/clubs/reviews/";
  private static final String REVIEWS_ID_URL = "/clubs/reviews/{id}/";

  //-----------------------------CREATE----------------------------
  @Step("[API] Create new Review")
  public ReviewRespModel createReview(String accessToken, ReviewReqModel createReviewData) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .body(createReviewData)
            .when()
            .post(REVIEWS_URL)
            .then()
            .spec(createReviewRespSpec)
            .extract().as(ReviewRespModel.class);
  }
  //-----------------------------READ----------------------------
  @Step("[API] Get all Review")
  public PaginatedReviewListRespModel getReview(String accessToken) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .when()
            .get(REVIEWS_URL)
            .then()
            .spec(getReviewRespSpec)
            .extract().as(PaginatedReviewListRespModel.class);
  }

  @Step("[API] Get review by Id")
  public ReviewRespModel getReviewById(String accessToken, Integer reviewId) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", reviewId)
            .when()
            .get(REVIEWS_ID_URL)
            .then()
            .spec(getReviewByIdRespSpec)
            .extract().as(ReviewRespModel.class);
  }

  @Step("[API] Impossible to get review by non existing ID")
  public DetailRespModel getReviewByWrongId(String accessToken, Integer reviewId) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", reviewId)
            .when()
            .get(REVIEWS_ID_URL)
            .then()
            .spec(nonExistReviewId)
            .extract().as(DetailRespModel.class);
  }
  //-----------------------------UPDATE----------------------------
  @Step("[API] PUT Update Review")
  public ReviewRespModel updatePutReview(String accessToken, Integer reviewId, ReviewReqModel updateReviewData) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", reviewId)
            .body(updateReviewData)
            .when()
            .put(REVIEWS_ID_URL)
            .then()
            .spec(updatedReviewRespSpec)
            .extract().as(ReviewRespModel.class);
  }

  @Step("[API] PATCH Update Review")
  public ReviewRespModel updatePatchReview(String accessToken, Integer reviewId, ReviewReqModel updateReviewData) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", reviewId)
            .body(updateReviewData)
            .when()
            .patch(REVIEWS_ID_URL)
            .then()
            .spec(updatedReviewRespSpec)
            .extract().as(ReviewRespModel.class);
  }

  @Step("[API] PATCH No permission update another review")
  public DetailRespModel cantUpdateAnotherReview(String accessToken, Integer reviewId, ReviewReqModel updateReviewData) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", reviewId)
            .body(updateReviewData)
            .when()
            .patch(REVIEWS_ID_URL)
            .then()
            .spec(noPermission)
            .extract().as(DetailRespModel.class);
  }
  //-----------------------------DELETE----------------------------
  @Step("[API] Delete Review")
  public void deleteReview(String accessToken, Integer reviewId) {
    given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", reviewId)
            .when()
            .delete(REVIEWS_ID_URL)
            .then()
            .spec(deleteReviewRespSpec);
  }

  @Step("[API] No permission to delete another review")
  public DetailRespModel cantDeleteAnotherReview(String accessToken, Integer reviewId) {
    return given(reviewsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", reviewId)
            .when()
            .delete(REVIEWS_ID_URL)
            .then()
            .spec(noPermission)
            .extract().as(DetailRespModel.class);
  }
}
