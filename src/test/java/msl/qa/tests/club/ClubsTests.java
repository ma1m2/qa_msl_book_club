package msl.qa.tests.club;

import msl.qa.models.clubs.PaginatedClubListRespModel;
import msl.qa.models.login.LoginReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static msl.qa.tests.TestData.PASSWORD;
import static msl.qa.tests.TestData.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubsTests extends TestBase {

  LoginReqModel loginData = new LoginReqModel(USERNAME, PASSWORD);

  //-----------------------------CREATE----------------------------


  //-----------------------------READ----------------------------
  @Test
  public void getClubsListTest() {
    String accessToken = api.auth.extractAccessToken(loginData);

    PaginatedClubListRespModel clubs = api.clubs.getClubs(accessToken);

    assertThat(clubs.count()).isGreaterThanOrEqualTo(1);
    assertThat(clubs.results()).size().isGreaterThanOrEqualTo(1);
  }

  @Test
  public void getClubsListWithQueryParamsTest() {
    String accessToken = api.auth.extractAccessToken(loginData);

    PaginatedClubListRespModel clubs = api.clubs.getClubs(accessToken,
            Map.of("page_size", 1,"page", 100));

    assertThat(clubs.count()).isGreaterThan(1);
    assertThat(clubs.results().size()).isLessThanOrEqualTo(1);
  }

  //-----------------------------UPDATE----------------------------

  //-----------------------------DELETE----------------------------

}

