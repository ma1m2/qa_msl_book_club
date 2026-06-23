package msl.qa.tests.api.review;

import io.qameta.allure.Feature;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.models.review.ReviewReqModel;
import msl.qa.models.review.ReviewRespModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Feature("[API] Review")
@Tag("api")
public class ReviewTests extends TestBase {

  String accessToken;
  //-----------------------------CREATE----------------------------
  @Test
  @DisplayName("[API] Creat Review for owner's club")
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
  //-----------------------------READ----------------------------

  //-----------------------------UPDATE----------------------------

  //-----------------------------DELETE----------------------------
}
