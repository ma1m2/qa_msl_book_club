package msl.qa.tests.api.review;

import io.qameta.allure.Feature;
import msl.qa.models.DetailRespModel;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.review.PaginatedReviewListRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import msl.qa.models.clubs.review.ReviewRespModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.tests.TestBase;
import msl.qa.tests.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.qameta.allure.Allure.step;
import static msl.qa.tests.TestData.NO_PERMISSION;
import static msl.qa.tests.TestData.NO_BOOK_REVIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Feature("[API] Review")
@Tag("api")
public class ReviewApiTests extends TestBase {

  String accessToken;
  RegisterRespModel user;
  //-----------------------------CREATE----------------------------
  @Test
  @DisplayName("[API] Creat review for own club")
  public void createReviewOwnerClubTest() {
    //register
    RegisterRespModel user = api.users.register(td.registrationData());
    //login
    accessToken = api.auth.extractAccessToken(td.loginData());
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    //create review
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());
    ReviewRespModel reviewResponse = api.review.createReview(accessToken, reviewReqModel);

    Instant responseInstant = Instant.parse(reviewResponse.created());
    Instant currentInstant = Instant.now().truncatedTo(ChronoUnit.MICROS);

    assertThat(reviewResponse.id()).isGreaterThan(0);
    assertThat(reviewResponse.club()).isEqualTo(createdClub.id());
    assertThat(reviewResponse.user().id()).isEqualTo(user.id());
    assertThat(reviewResponse.user().username()).isEqualTo(td.username());
    assertThat(reviewResponse.review()).isEqualTo(td.review());
    assertThat(reviewResponse.assessment()).isEqualTo(td.assessment());
    assertThat(reviewResponse.readPages()).isEqualTo(td.readPages());
    assertThat(responseInstant).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
    assertThat(reviewResponse.modified()).isNull();
  }

  @Test
  @DisplayName("[API] Creat review for another club")
  public void createReviewForAnotherClubTest() {

    step("[API] Register and login first user", () ->{
      //register
      user = api.users.register(td.registrationData());
      //login
      accessToken = api.auth.extractAccessToken(td.loginData());
    });

    CreateClubRespModel createdClub = step("[API] Register second user, login, create club", () ->{
      api.users.register(td2.registrationData());
      //login second user
      String secondAccessToken = api.auth.extractAccessToken(td2.loginData());
      //create club for second user
      return api.clubs.createClub(secondAccessToken, td2.createClubData());
    });

    ReviewRespModel reviewResponse = step("[API] First user join second user club and create review", () ->{
      //first user join another club
      api.clubs.createMemberOfClub(accessToken, String.valueOf(createdClub.id()));
      //create review
      ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());
      return api.review.createReview(accessToken, reviewReqModel);
    });

    step("[API] Verify response data BookReview", () ->{
    Instant responseInstant = Instant.parse(reviewResponse.created());
    Instant currentInstant = Instant.now().truncatedTo(ChronoUnit.MICROS);

    assertThat(reviewResponse.id()).isGreaterThan(0);
    assertThat(reviewResponse.club()).isEqualTo(createdClub.id());
    assertThat(reviewResponse.user().id()).isEqualTo(user.id());
    assertThat(reviewResponse.user().username()).isEqualTo(td.username());
    assertThat(reviewResponse.review()).isEqualTo(td.review());
    assertThat(reviewResponse.assessment()).isEqualTo(td.assessment());
    assertThat(reviewResponse.readPages()).isEqualTo(td.readPages());
    assertThat(responseInstant).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
    assertThat(reviewResponse.modified()).isNull();
    });
  }

  @Test
  @DisplayName("[API] Non member club can creat review")
  public void createReviewNonMemberTest() {
    step("[API] Register and login first user", () ->{
      //register
      user = api.users.register(td.registrationData());
      //login
      accessToken = api.auth.extractAccessToken(td.loginData());
    });

    CreateClubRespModel createdClub = step("[API] Register second user, login, create club", () ->{
      api.users.register(td2.registrationData());
      //login second user
      String secondAccessToken = api.auth.extractAccessToken(td2.loginData());
      //create club for second user
      return api.clubs.createClub(secondAccessToken, td2.createClubData());
    });

    ReviewRespModel reviewResponse = step("[API] First user non member club can create review", () ->{
      //create review
      ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());
      return api.review.createReview(accessToken, reviewReqModel);
    });

    step("[API] Verify that user non member club but review is exist", () ->{

      CreateClubRespModel clubRespModel = api.clubs.getClubById(accessToken, String.valueOf(createdClub.id()));
      //user isn't member
      assertThat(clubRespModel.members())
              .doesNotContain(user.id());
      //review is exist
      assertThat(clubRespModel.reviews())
              .extracting(review -> review.user().id())
              .contains(user.id());

    });

  }

  //-----------------------------READ----------------------------
  @Test
  @DisplayName("[API] GET all Clubs")
  public void getReviewListTest() {

    accessToken = registerAndLoginNewUser();

    PaginatedReviewListRespModel reviews = api.review.getReview(accessToken);

    assertThat(reviews.count()).isGreaterThanOrEqualTo(1);
    assertThat(reviews.results()).size().isGreaterThanOrEqualTo(1);
  }
  //-----------------------------UPDATE----------------------------
  @Test
  @DisplayName("[API] PUT Update own review")
  public void updateOwnReviewTest() {
    //register
    user = api.users.register(td.registrationData());
    //login
    accessToken = api.auth.extractAccessToken(td.loginData());
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    //create review
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());
    ReviewRespModel reviewResponse = api.review.createReview(accessToken, reviewReqModel);
    //update review
    ReviewReqModel updatedReviewReq = new ReviewReqModel(createdClub.id(),td2.review(),td2.assessment(),td2.readPages());
    ReviewRespModel updatedResponse = api.review.updatePutReview(accessToken,reviewResponse.id(),updatedReviewReq);

    Instant responseInstant = Instant.parse(updatedResponse.modified());
    Instant currentInstant = Instant.now().truncatedTo(ChronoUnit.MICROS);

    assertThat(updatedResponse.id()).isEqualTo(reviewResponse.id());
    assertThat(updatedResponse.club()).isEqualTo(createdClub.id());
    assertThat(updatedResponse.user().id()).isEqualTo(user.id());
    assertThat(updatedResponse.user().username()).isEqualTo(td.username());
    assertThat(updatedResponse.review()).isEqualTo(td2.review());
    assertThat(updatedResponse.assessment()).isEqualTo(td2.assessment());
    assertThat(updatedResponse.readPages()).isEqualTo(td2.readPages());
    assertThat(responseInstant).isCloseTo(currentInstant, within(1, ChronoUnit.SECONDS));
  }

  @Test
  @DisplayName("[API] PATCH cannot update another review")
  @Tag("negative")
  public void updateAnotherReviewTest() {
    //register
    RegisterRespModel user = api.users.register(td.registrationData());
    //login
    accessToken = api.auth.extractAccessToken(td.loginData());
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    //create second user
    api.users.register(td2.registrationData());
    //login second user
    String secondAccessToken = api.auth.extractAccessToken(td2.loginData());
    //second user create review
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td2.review(),td2.assessment(),td2.readPages());
    ReviewRespModel reviewResponse = api.review.createReview(secondAccessToken, reviewReqModel);
    //first user tries to update second user review
    ReviewReqModel updatedReviewReq = new ReviewReqModel(createdClub.id(), td.review(), null, td.readPages());
    DetailRespModel detailRespModel = api.review.cantUpdateAnotherReview(accessToken, reviewResponse.id(), updatedReviewReq);

    assertThat(detailRespModel.detail()).isEqualTo(NO_PERMISSION);

  }

  //-----------------------------DELETE----------------------------
  @Test
  @DisplayName("[API] Delete own review")
  public void deleteOwnReviewTest() {
    accessToken = registerAndLoginNewUser();
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    //create review
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());
    ReviewRespModel reviewResponse = api.review.createReview(accessToken, reviewReqModel);
    //delete review
    api.review.deleteReview(accessToken, reviewResponse.id());
    //review not exist more
    DetailRespModel detailRespModel = api.review.getReviewByWrongId(accessToken, reviewResponse.id());

    assertThat(detailRespModel.detail()).contains(NO_BOOK_REVIEW);
  }

  @Test
  @DisplayName("[API] Can't delete another review")
  public void cantDeleteAnotherReviewTest() {
    //register
    RegisterRespModel user = api.users.register(td.registrationData());
    //login
    accessToken = api.auth.extractAccessToken(td.loginData());
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    //create second user
    api.users.register(td2.registrationData());
    //login second user
    String secondAccessToken = api.auth.extractAccessToken(td2.loginData());
    //second user create review
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td2.review(),td2.assessment(),td2.readPages());
    ReviewRespModel reviewResponse = api.review.createReview(secondAccessToken, reviewReqModel);
    //first user tries to delete second user review
    DetailRespModel detailRespModel = api.review.cantDeleteAnotherReview(accessToken, reviewResponse.id());
    assertThat(detailRespModel.detail()).isEqualTo(NO_PERMISSION);
  }

}
