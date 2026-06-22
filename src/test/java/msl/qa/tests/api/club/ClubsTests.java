package msl.qa.tests.api.club;

import msl.qa.models.clubs.CreateClubReqModel;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.PaginatedClubListRespModel;
import msl.qa.models.clubs.PatchClubReqModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static msl.qa.tests.TestData.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;

public class ClubsTests extends TestBase {

  RegisterReqModel loginData = new RegisterReqModel(td.username(), PASSWORD);

  //-----------------------------CREATE----------------------------
  @Test
  public void createClubSuccessTest() {
    String accessToken = registerAndLoginNewUser();
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
  public void getClubsListTest() {
    String token = api.auth.extractAccessToken(loginData);

    PaginatedClubListRespModel clubs = api.clubs.getClubs(token);

    assertThat(clubs.count()).isGreaterThanOrEqualTo(1);
    assertThat(clubs.results()).size().isGreaterThanOrEqualTo(1);
  }

  @Test
  public void getClubsListWithQueryParamsTest() {
    String token = api.auth.extractAccessToken(loginData);

    PaginatedClubListRespModel clubs = api.clubs.getClubs(token,
            Map.of("page_size", 1,"page", 100));

    assertThat(clubs.count()).isGreaterThan(1);
    assertThat(clubs.results().size()).isLessThanOrEqualTo(1);
  }

  //-----------------------------UPDATE----------------------------
  @Test
  public void patchClubDescriptionAndTelegramChatLinkSuccessTest() {
    String accessToken = registerAndLoginNewUser();
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());

    PatchClubReqModel patchData = new PatchClubReqModel(
            null,
            null,
            null,
            td.description(),
            td.telegramChatLink()
    );

    CreateClubRespModel updatedClub = api.clubs.patchClub(accessToken, createdClub.id(), patchData);

    assertThat(updatedClub.description()).isEqualTo(patchData.description());
    assertThat(updatedClub.telegramChatLink()).isEqualTo(patchData.telegramChatLink());
    assertThat(updatedClub.bookTitle()).isEqualTo(createdClub.bookTitle());
    assertThat(updatedClub.bookAuthors()).isEqualTo(createdClub.bookAuthors());
  }

  @Test
  public void putClubChangeOnlyDescriptionSuccessTest() {
    String accessToken = registerAndLoginNewUser();
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());

    CreateClubReqModel putData = new CreateClubReqModel(
            createdClub.bookTitle(),
            createdClub.bookAuthors(),
            createdClub.publicationYear(),
            td.description(),
            createdClub.telegramChatLink()
    );

    CreateClubRespModel updatedClub = api.clubs.putClub(accessToken, createdClub.id(), putData);

    assertThat(updatedClub.description()).isEqualTo(td.newDescription());
    assertThat(updatedClub.telegramChatLink()).isEqualTo(createdClub.telegramChatLink());
    assertThat(updatedClub.bookTitle()).isEqualTo(createdClub.bookTitle());
    assertThat(updatedClub.bookAuthors()).isEqualTo(createdClub.bookAuthors());
    assertThat(updatedClub.publicationYear()).isEqualTo(createdClub.publicationYear());
  }

  //-----------------------------DELETE----------------------------
  @Test
  public void deleteClubSuccessTest() {
    String accessToken = registerAndLoginNewUser();
    CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
    api.clubs.deleteClub(accessToken, createdClub.id());
  }

}

