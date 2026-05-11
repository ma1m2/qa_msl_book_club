package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.clubs.PaginatedClubListRespModel;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.clubs.ClubsSpec.clubsListRespSpec;
import static msl.qa.spec.clubs.ClubsSpec.clubsReqSpec;

public class ClubsApiClient {

  private static final String CLUBS_URL = "/clubs/";

  //-----------------------------CREATE----------------------------


  //-----------------------------READ----------------------------
  @Step("Get clubs list")
  public PaginatedClubListRespModel getClubs(String accessToken) {
    return given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .when()
            .get(CLUBS_URL)
            .then()
            .spec(clubsListRespSpec)
            .extract().as(PaginatedClubListRespModel.class);
  }

  @Step("Get clubs list with query params")
  public PaginatedClubListRespModel getClubs(String accessToken, Map<String, ?> queryParams) {
    return given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .queryParams(queryParams)
            .when()
            .get(CLUBS_URL)
            .then()
            .spec(clubsListRespSpec)
            .extract().as(PaginatedClubListRespModel.class);
  }

  //-----------------------------UPDATE----------------------------

  //-----------------------------DELETE----------------------------
}
