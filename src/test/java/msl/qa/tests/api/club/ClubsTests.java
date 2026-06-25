package msl.qa.tests.api.club;

import io.qameta.allure.Feature;
import msl.qa.models.clubs.CreateClubReqModel;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.PaginatedClubListRespModel;
import msl.qa.models.clubs.PatchClubReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("[API] Club")
@Tag("api")
public class ClubsTests extends TestBase {
  String accessToken;
  Integer clubId;

  //-----------------------------CREATE----------------------------
  @Test
  @DisplayName("[API] Successful creating Club")
  public void createClubSuccessTest() {
    accessToken = registerAndLoginNewUser();
    CreateClubReqModel createData = td.createClubData();

    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, createData);

    assertThat(createdClub.id()).isGreaterThan(0);
    assertThat(createdClub.bookTitle()).isEqualTo(createData.bookTitle());
    assertThat(createdClub.bookAuthors()).isEqualTo(createData.bookAuthors());
    assertThat(createdClub.publicationYear()).isEqualTo(createData.publicationYear());
    assertThat(createdClub.description()).isEqualTo(createData.description());
    assertThat(createdClub.telegramChatLink()).isEqualTo(createData.telegramChatLink());
    assertThat(createdClub.owner()).isGreaterThan(0);
    assertThat(createdClub.members()).isNotEmpty();
    assertThat(createdClub.reviews()).isEmpty();
    assertThat(createdClub.created()).isNotBlank();
  }

  //-----------------------------READ----------------------------
  @Test
  @DisplayName("[API] GET all Clubs")
  public void getClubsListTest() {

    accessToken = registerAndLoginNewUser();

    PaginatedClubListRespModel clubs = api.clubs.getClubs(accessToken);

    assertThat(clubs.count()).isGreaterThanOrEqualTo(1);
    assertThat(clubs.results()).size().isGreaterThanOrEqualTo(1);
  }

  @Test
  @DisplayName("[API] GET Club List With Query Params")
  public void getClubsListWithQueryParamsTest() {
    accessToken = registerAndLoginNewUser();

    PaginatedClubListRespModel clubs = api.clubs.getClubs(accessToken,
            Map.of("page_size", 10,"page", 1));

    assertThat(clubs.count()).isGreaterThan(1);
    assertThat(clubs.results().size()).isGreaterThan(1);
  }

  //-----------------------------UPDATE----------------------------
  @Test
  @DisplayName("[API] PATCH Club Description And TelegramChatLink Successfully")
  public void patchClubDescriptionAndTelegramChatLinkSuccessTest() {
    String accessToken = registerAndLoginNewUser();
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());

    PatchClubReqModel patchData = new PatchClubReqModel(
            null,
            null,
            null,
            td.updatedDescription(),
            td.updatedTelegramChatLink()

    );

    clubId = createdClub.id();
    CreateClubRespModel updatedClub = api.clubs.patchClub(accessToken, clubId, patchData);

    assertThat(updatedClub.description()).isEqualTo(patchData.description());
    assertThat(updatedClub.telegramChatLink()).isEqualTo(patchData.telegramChatLink());
    assertThat(updatedClub.bookTitle()).isEqualTo(createdClub.bookTitle());
    assertThat(updatedClub.bookAuthors()).isEqualTo(createdClub.bookAuthors());
  }

  @Test
  @DisplayName("[API] PUT Club Change Description Successfully")
  public void putClubChangeOnlyDescriptionSuccessTest() {
    String accessToken = registerAndLoginNewUser();
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());

    CreateClubReqModel putData = new CreateClubReqModel(
            createdClub.bookTitle(),
            createdClub.bookAuthors(),
            createdClub.publicationYear(),
            td.updatedDescription(),
            createdClub.telegramChatLink()
    );

    clubId = createdClub.id();
    CreateClubRespModel updatedClub = api.clubs.putClub(accessToken, clubId, putData);

    assertThat(updatedClub.description()).isEqualTo(td.updatedDescription());
    assertThat(updatedClub.telegramChatLink()).isEqualTo(createdClub.telegramChatLink());
    assertThat(updatedClub.bookTitle()).isEqualTo(createdClub.bookTitle());
    assertThat(updatedClub.bookAuthors()).isEqualTo(createdClub.bookAuthors());
    assertThat(updatedClub.publicationYear()).isEqualTo(createdClub.publicationYear());
  }

  //-----------------------------DELETE----------------------------
  @Test
  @DisplayName("[API] DELETE Club Successfully")
  public void deleteClubSuccessTest() {
    String accessToken = registerAndLoginNewUser();
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    clubId = createdClub.id();
    api.clubs.deleteClub(accessToken, clubId);
  }

}

