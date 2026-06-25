package msl.qa.tests.ui.review;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import msl.qa.models.clubs.review.ReviewRespModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.pages.components.ReviewCard;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static io.qameta.allure.Allure.step;
import static msl.qa.helper.Util.*;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("[UI] Review")
@Tag("ui")
public class ReviewUiTests extends TestBase {
  String token;
  //-----------------CREATE------------------------------
  @Test
  @DisplayName("[UI] Create review for owner club by UI")
  public void createReviewForOwnerClubByUi() {

    CreateClubRespModel club = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = clubsPage.openClubsPageWithNewUser(td.username(), td.password());
      String token = loginResp.access();
      return createClub(token);
    });

    step("[UI] Open club by Id and verify review data", () -> {
      ReviewCard rc = clubsPage
              .openClubById(club.id())
              .addReview()
              .fillReviewForm(td.assessment(), td.readPages(), td.review());

      assertThat(rc.getReviewName()).isEqualTo(td.username());
      assertThat(rc.getReviewPages()).isEqualTo(td.readPages());
      assertThat(rc.getReviewStars()).isEqualTo(td.assessment());
      assertThat(rc.getReviewContent()).isEqualTo(td.review());
      assertThat(rc.getReviewDate()).isEqualTo(LocalDate.now());

    });
  }

  @Test
  @DisplayName("[UI] Create review for owner club by API and display on UI")
  public void createReviewForOwnerClub() {

    ReviewRespModel review = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = clubsPage.openClubsPageWithNewUser(td.username(), td.password());
      String token = loginResp.access();
      return createReviewOwnerClub(token);
    });

    step("[UI] Open club by Id and verify review data", () -> {
      Integer clubId = review.club();
      ReviewCard rc = clubsPage
              .openClubById(clubId)
              .reviewCard();

      LocalDate apiDate = ZonedDateTime.parse(review.created()).toLocalDate();

      assertThat(rc.getReviewName()).isEqualTo(review.user().username());
      assertThat(rc.getReviewPages()).isEqualTo(review.readPages());
      assertThat(rc.getReviewStars()).isEqualTo(review.assessment());
      assertThat(rc.getReviewContent()).isEqualTo(review.review());
      assertThat(rc.getReviewDate()).isEqualTo(apiDate);

    });
  }

  @Test
  @DisplayName("[UI] Create review for another club by API and display on UI")
  public void createReviewForAnotherClub() {

    ReviewRespModel review = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = clubsPage.openClubsPageWithNewUser(td.username(), td.password());
      String token = loginResp.access();
      return createReviewForSecondUserClubClub(token);
    });

    step("[UI] Open club by Id and verify review data", () -> {
      Integer clubId = review.club();
      ReviewCard rc = clubsPage
              .openClubById(clubId)
              .reviewCard();

      LocalDate apiDate = ZonedDateTime.parse(review.created()).toLocalDate();

      assertThat(rc.getReviewName()).isEqualTo(review.user().username());
      assertThat(rc.getReviewPages()).isEqualTo(review.readPages());
      assertThat(rc.getReviewStars()).isEqualTo(review.assessment());
      assertThat(rc.getReviewContent()).isEqualTo(review.review());
      assertThat(rc.getReviewDate()).isEqualTo(apiDate);

    });
  }

  //-----------------UPDATE------------------------------
  @Test
  @DisplayName("[UI] Update review for own club by API and check by UI")
  public void updateOwnReview() {

    ReviewRespModel review = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = clubsPage.openClubsPageWithNewUser(td.username(), td.password());
      token = loginResp.access();
      return createReviewOwnerClub(token);
    });

    step("[UI] Open review by Id, verify data, update by API, and check on UI", () -> {
      LocalDate apiDate = getDateFromJson(review.created());
      Integer clubId = review.club();
      ReviewCard rc = clubsPage
              .openClubById(clubId)
              .reviewCard();

      rc.assertReviewIsExist(td.username(), td.assessment(), td.readPages(), td.review(), formatDate(apiDate));
    });

    ReviewReqModel updateBody = new ReviewReqModel(review.club(), td2.review(), td2.assessment(), td2.readPages());
    ReviewRespModel updatedReview = api.review.updatePutReview(token, review.id(), updateBody);
    step("[UI] Refresh club page", Selenide::refresh);

    step("[UI] Verify updated data by UI", () -> {
      LocalDate apiDate = getDateFromJson(updatedReview.modified());
      Integer clubId = updatedReview.club();
      ReviewCard rc = clubsPage
              .openClubById(clubId)
              .reviewCard();

      rc.assertReviewIsExist(td.username(), td2.assessment(), td2.readPages(), td2.review(), formatDate(apiDate));
    });
  }

  @Step("[API] Create Club")
  private CreateClubRespModel createClub(String token){
    return api.clubs.createClub(token, td.createClubData());
  }

  @Step("[API] Create Review For Second User Club")
  private ReviewRespModel createReviewForSecondUserClubClub(String token){
    //register second user
    RegisterRespModel secondUser = api.users.register(td2.registrationData());
    //login second user
    String secondAccessToken = api.auth.extractAccessToken(td2.loginData());
    //create club for second user
    CreateClubRespModel createdClub = api.clubs.createClub(secondAccessToken, td2.createClubData());
    //create review and return
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());

    return api.review.createReview(token, reviewReqModel);
  }


}
