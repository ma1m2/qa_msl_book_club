package msl.qa.tests.ui.club;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import msl.qa.helper.TestDataBuilder;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.localstorage.LocalStorageAuthReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.tests.TestBase;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@Feature("[UI] Club")
@Tag("ui")
@Owner("SvetaQa")
public class ClubUiTests extends TestBase {

  Integer clubId;
  String accessToken;

  @Test
  @Tag("ui")
  @Description("Register, login, creating club by API, imitate session by localStorage, navigate on Club Page." +
          "Verify fields on ClubPage")
  @DisplayName("Create club by API and verify by UI")
  @Severity(SeverityLevel.CRITICAL)
  public void createClub(){
    step("[UI] Open app with API token in localStorage, create club by API", () -> {
      LoginRespModel loginResp = clubsPage.openClubsPageWithNewUser(td.username(), td.password());
      //create club
      CreateClubRespModel createdClub = api.clubs.createClub(loginResp.access(), td.createClubData());
      clubId = createdClub.id();
      });

    step("[UI] Open club by Id and verify book title and year", () -> {
      clubsPage
              .openClubById(clubId)
              .assertClubIsExist(td.bookTitle(), td.bookAuthors(), td.publicationYear(), td.description());
    });
  }

  @Test
  @DisplayName("[UI] Update club data by API and check by UI")
  @Severity(SeverityLevel.NORMAL)
  public void updateClub(){
    step("[UI] Open app with API token in localStorage, create club by API", () -> {
      LoginRespModel loginResp = clubsPage.openClubsPageWithNewUser(td.username(), td.password());
      //create club
      accessToken = loginResp.access();
      CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
      clubId = createdClub.id();
    });

    step("[UI] Open club by Id and verify book content", () -> {
      clubsPage
              .openClubById(clubId)
              .assertClubIsExist(td.bookTitle(), td.bookAuthors(), td.publicationYear(), td.description());
    });

    CreateClubRespModel updatedClub = api.clubs.putClub(accessToken, clubId, td2.createClubData());
    step("[UI] Refresh club page", Selenide::refresh);

    step("[UI] Open club by Id and verify book content", () -> {
      clubsPage
              .openClubById(clubId)
              .assertClubIsExist(td2.bookTitle(), td2.bookAuthors(), td2.publicationYear(), td2.description());
    });
  }

  @Test
  @DisplayName("[UI] Club deleted by API and check by UI")
  @Severity(SeverityLevel.NORMAL)
  public void deleteClub(){
    step("[UI] Open app with API token in localStorage, create club by API", () -> {
      LoginRespModel loginResp = clubsPage.openClubsPageWithNewUser(td.username(), td.password());
      //create club
      accessToken = loginResp.access();
      CreateClubRespModel createdClub = api.clubs.createClub(accessToken, td.createClubData());
      clubId = createdClub.id();
    });

    step("[UI] Open club by Id and verify book content", () -> {
      clubsPage
              .openClubById(clubId)
              .assertClubIsExist(td.bookTitle(), td.bookAuthors(), td.publicationYear(), td.description());
    });

    api.clubs.deleteClub(accessToken, clubId);
    step("[UI] Refresh club page", Selenide::refresh);
    clubsPage.openClubById(clubId).checkDeletedClub();
  }

  @Test
  @Description("Register, login, creating club by API, imitate session by localStorage, navigate on Club Page." +
  "Error is displayed when owner trying to leave club.")
  @DisplayName("Owner cannot leave club")
  @Severity(SeverityLevel.CRITICAL)
  public void cantLeaveClubAsOwner() {

    step("Authorized app launch with API token from localStorage", () -> {
      //register user
      RegisterRespModel user = step("[API] Register user", () ->
              api.users.register(new RegisterReqModel(td.username(), td.password())));

      //login user
      LoginRespModel loginResp = step("[API] Authorisation and get token", () ->
              api.auth.successfulLogin(td.loginData()));

      //create club
      CreateClubRespModel createdClub = step("[API] Create Club", () ->
              api.clubs.createClub(loginResp.access(), td.createClubData()));

      clubId = createdClub.id();

      //create localStorage
      String localStorageAuthBody = step("[API] Create authorisation JSON for localStorage", () ->
              new LocalStorageAuthReqModel(user, loginResp.access(), loginResp.refresh(), true).toJson());

      step("[UI] Open app with API token in localStorage and verify user is authorized", () -> {
        clubsPage.openWithAuth(localStorageAuthBody);
        clubsPage.authorisedUserOnMainPage();
      });
    });

    //wrong leaving club
    step("[UI] Open owner's club and try to leave it", () -> {
      clubsPage.openClubById(clubId)
              .leaveOwnresClub();
    });
  }

  @Test
  @Disabled
  public void cantLeaveClubAsAdminTest_without_api() {
    //register user
    step("register user", () -> {
      System.out.println("### " + td.username() + " " + td.password());
      open("/signup");//https://book-club.qa.guru
      $("[data-testid=username-input]").setValue(td.username());
      $("[data-testid=password-input]").setValue(td.password());
      $("[data-testid=confirm-password-input]").setValue(td.password()).pressEnter();
      $("[data-testid=signup-button]").should(disappear);
    });

    //create club
    step("create club", () -> {
      $("[data-testid=create-club-link]").click();
      $("#bookTitle").setValue(td.bookTitle());
      $("#bookAuthors").setValue(td.bookAuthors());
      $("#publicationYear").setValue(String.valueOf(td.publicationYear()));
      $("#description").setValue(td.description());
      $("#telegramChatLink").setValue(td.telegramChatLink());
      $("button.submit-btn").click();
    });
    // open club
    step("open club", () -> {
      $$(".club-card h2").findBy(text(td.bookTitle())).scrollTo().parent().parent().$(".open-btn").click();
    });
    //wrong leaving club
    step("wrong leaving club", () -> {
      $(".club-content").shouldBe(visible);
      $(".leave-btn").click();
      confirm();//alert OK
      $(".error").shouldHave(text("Не удалось покинуть клуб"));
    });
  }
}
