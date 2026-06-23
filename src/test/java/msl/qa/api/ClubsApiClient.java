package msl.qa.api;

import io.qameta.allure.Step;
import msl.qa.models.clubs.CreateClubReqModel;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.PatchClubReqModel;
import msl.qa.models.clubs.PaginatedClubListRespModel;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static msl.qa.spec.clubs.ClubsSpec.createClubRespSpec;
import static msl.qa.spec.clubs.ClubsSpec.clubsListRespSpec;
import static msl.qa.spec.clubs.ClubsSpec.clubsReqSpec;
import static msl.qa.spec.clubs.ClubsSpec.deleteClubRespSpec;
import static msl.qa.spec.clubs.ClubsSpec.getClubRespSpec;
import static msl.qa.spec.clubs.ClubsSpec.updateClubRespSpec;

public class ClubsApiClient {

  private static final String CLUBS_URL = "/clubs/";
  private static final String CLUB_ID_URL = "/clubs/{id}";
  private static final String MEMBER_URL = "/clubs/{id}/members/me/";

  //-----------------------------CREATE----------------------------
  @Step("[API] Create new club")
  public CreateClubRespModel createClub(String accessToken, CreateClubReqModel createClubData) {
    return given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .body(createClubData)
            .when()
            .post(CLUBS_URL)
            .then()
            .spec(createClubRespSpec)
            .extract().as(CreateClubRespModel.class);
  }
  //-----------------------------CREATE MEMBER----------------------------
  @Step("[API] Join to club")
  public void createMemberOfClub(String accessToken, String clubId) {
    given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", clubId)
            .when()
            .post(MEMBER_URL)
            .then()
            .statusCode(204);
  }
  //-----------------------------READ----------------------------
  @Step("[API] Get clubs list")
  public PaginatedClubListRespModel getClubs(String accessToken) {
    return given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .when()
            .get(CLUBS_URL)
            .then()
            .spec(clubsListRespSpec)
            .extract().as(PaginatedClubListRespModel.class);
  }

  @Step("[API] Get clubs list with query params")
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

  @Step("[API] Get club by ID")
  public CreateClubRespModel getClubById(String accessToken, String clubId) {
    return given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .pathParam("id", clubId)
            .when()
            .get(CLUB_ID_URL)
            .then()
            .spec(getClubRespSpec)
            .extract().as(CreateClubRespModel.class);
  }

  //-----------------------------UPDATE----------------------------
  @Step("[API] Patch club by id")
  public CreateClubRespModel patchClub(String accessToken, Integer clubId, PatchClubReqModel patchData) {
    return given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .body(patchData)
            .when()
            .patch(CLUBS_URL + clubId + "/")
            .then()
            .spec(updateClubRespSpec)
            .extract().as(CreateClubRespModel.class);
  }

  @Step("[API] Put club by id")
  public CreateClubRespModel putClub(String accessToken, Integer clubId, CreateClubReqModel putData) {
    return given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .body(putData)
            .when()
            .put(CLUBS_URL + clubId + "/")
            .then()
            .spec(updateClubRespSpec)
            .extract().as(CreateClubRespModel.class);
  }

  //-----------------------------DELETE----------------------------
  @Step("[API] Delete club by id")
  public void deleteClub(String accessToken, Integer clubId) {
    given(clubsReqSpec)
            .header("Authorization", "Bearer " + accessToken)
            .when()
            .delete(CLUBS_URL + clubId + "/")
            .then()
            .spec(deleteClubRespSpec);
  }
}
