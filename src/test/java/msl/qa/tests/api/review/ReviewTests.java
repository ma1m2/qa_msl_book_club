package msl.qa.tests.api.review;

import io.qameta.allure.Feature;
import msl.qa.helper.ApiUtil;
import msl.qa.helper.TestDataBuilder;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.PaginatedClubListRespModel;
import msl.qa.models.clubs.review.BookReviewRespModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static msl.qa.helper.ApiUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@Feature("[API] Review")
@Tag("api")
public class ReviewTests extends TestBase {

  TestDataBuilder td2 = new TestDataBuilder();
  String accessToken;
  //-----------------------------CREATE----------------------------
  @Test
  @DisplayName("[API] Creat review for owner's club")
  public void createReviewOwnerClubTest() {
    //register
    RegisterRespModel user = api.users.register(td.registrationData());
    //login
    accessToken = api.auth.extractAccessToken(td.loginData());
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    //create review
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());
    BookReviewRespModel reviewResponse = api.review.createReview(accessToken, reviewReqModel);

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
  public void createReviewAnotherClubTest() {
    //register
    RegisterRespModel user = api.users.register(td.registrationData());
    //login
    accessToken = api.auth.extractAccessToken(td.loginData());
    //get id club to join
    PaginatedClubListRespModel clubList = api.clubs.getClubs(accessToken);
    List<Integer> ids = clubList.results().stream().map(CreateClubRespModel::id).toList();
    Integer randomClubID = getRandomIntegerFromList(ids);
    //join another club
    api.clubs.createMemberOfClub(accessToken, randomClubID.toString());
    //create review
    ReviewReqModel reviewReqModel = new ReviewReqModel(randomClubID,td.review(),td.assessment(),td.readPages());
    BookReviewRespModel reviewResponse = api.review.createReview(accessToken, reviewReqModel);

    Instant responseInstant = Instant.parse(reviewResponse.created());
    Instant currentInstant = Instant.now().truncatedTo(ChronoUnit.MICROS);

    assertThat(reviewResponse.id()).isGreaterThan(0);
    assertThat(reviewResponse.club()).isEqualTo(randomClubID);
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
