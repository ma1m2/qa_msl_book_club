package msl.qa.tests.ui.review;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import msl.qa.models.clubs.review.ReviewRespModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static msl.qa.helper.Util.formatDate;
import static msl.qa.helper.Util.getDateFromJson;
import static msl.qa.helper.Util.today;

@Feature("[UI] Review")
@Tag("ui")
public class ReviewUiTests extends TestBase {
  String token;
  //-----------------CREATE------------------------------
  @Test
  @DisplayName("[UI] Create review for owner club by UI")
  public void createReviewForOwnerClubByUi() {

    CreateClubRespModel club = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = openClubsPageWithNewUser(td.username(), td.password());
      return createClub(loginResp.access());
    });

    step("[UI] Open club by Id and verify review data", () -> {
      clubsPage
              .openClubById(club.id())
              .addReview(td.assessment(), td.readPages(), td.review())
              .assertReviewIsExist(td.username(), td.assessment(), td.readPages(), td.review(),
                      formatDate(today()));
    });
  }

  @Test
  @DisplayName("[UI] Create review for owner club by API and display on UI")
  public void createReviewForOwnerClub() {

    ReviewRespModel review = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = openClubsPageWithNewUser(td.username(), td.password());
      return createReviewOwnerClub(loginResp.access());
    });

    step("[UI] Open club by Id and verify review data", () -> {
      clubsPage
              .openClubById(review.club())
              .assertReviewIsExist(review.user().username(), review.assessment(), review.readPages(),
                      review.review(), formatDate(getDateFromJson(review.created())));
    });
  }

  @Test
  @DisplayName("[UI] Create review for another club by API and display on UI")
  public void createReviewForAnotherClub() {

    ReviewRespModel review = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = openClubsPageWithNewUser(td.username(), td.password());
      return createReviewForSecondUserClubClub(loginResp.access());
    });

    step("[UI] Open club by Id and verify review data", () -> {
      clubsPage
              .openClubById(review.club())
              .assertReviewIsExist(review.user().username(), review.assessment(), review.readPages(),
                      review.review(), formatDate(getDateFromJson(review.created())));
    });
  }

  //-----------------UPDATE------------------------------
  @Test
  @DisplayName("[UI] Update review for own club by API and check by UI")
  public void updateOwnReview() {

    ReviewRespModel review = step("[UI] Open app with API token in localStorage, create club review by API", () -> {
      LoginRespModel loginResp = openClubsPageWithNewUser(td.username(), td.password());
      token = loginResp.access();
      return createReviewOwnerClub(token);
    });

    step("[UI] Open review by Id, verify data, update by API, and check on UI", () -> {
      clubsPage
              .openClubById(review.club())
              .assertReviewIsExist(td.username(), td.assessment(), td.readPages(), td.review(),
                      formatDate(getDateFromJson(review.created())));
    });

    ReviewReqModel updateBody = td2.reviewData(review.club());
    ReviewRespModel updatedReview = api.review.updatePutReview(token, review.id(), updateBody);
    step("[UI] Refresh club page", Selenide::refresh);

    step("[UI] Verify updated data by UI", () -> {
      clubsPage
              .openClubById(updatedReview.club())
              .assertReviewIsExist(td.username(), td2.assessment(), td2.readPages(), td2.review(),
                      formatDate(getDateFromJson(updatedReview.modified())));
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
    ReviewReqModel reviewReqModel = td.reviewData(createdClub.id());

    return api.review.createReview(token, reviewReqModel);
  }

}
