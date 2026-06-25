package msl.qa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import msl.qa.allure.Attach;
import msl.qa.api.ApiClient;
import msl.qa.helper.TestDataBuilder;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.review.ReviewRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.pages.ClubsPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.webdriver;

public class TestBase {

  protected static final ApiClient api = new ApiClient();

  protected ClubsPage clubsPage = new ClubsPage();

  protected TestDataBuilder td;
  protected TestDataBuilder td2;

  @BeforeAll
  public static void setUp() {
    RestAssured.baseURI = "http://localhost:8100";
    RestAssured.basePath = "/api/v1";
    Configuration.baseUrl = "http://localhost:8100";
    Configuration.browserSize = "1920x1080";
  }

  @BeforeEach
  public void prepareTestDataAndAddListener() {
    SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    td = new TestDataBuilder();
    td2 = new TestDataBuilder();
  }

  @AfterEach
  void addAttachments() {
    if(webdriver().driver().hasWebDriverStarted()) {
      Attach.screenshotAs("Last screenshot");
      Attach.pageSource();
      Attach.browserConsoleLogs();
//          Attach.addVideo();
      closeWebDriver();
    }
  }

  @Step("[API] Register and login new user")
  protected String registerAndLoginNewUser() {
    api.users.register(td.registrationData());
    return api.auth.extractAccessToken(new RegisterReqModel(td.username(), td.password()));
  }

  @Step("[API] Create Club")
  protected CreateClubRespModel createClub(String token){
    return api.clubs.createClub(token, td.createClubData());
  }

  @Step("[API] Create Review for Owner Club")
  protected ReviewRespModel createReviewOwnerClub(String token){
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(token, td.createClubData());
    //create review and return
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());

    return api.review.createReview(token, reviewReqModel);
  }

  @Step("[API] Create Review For Second User Club")
  protected ReviewRespModel createReviewForSecondUserClubClub(String token){
    //register second user
    RegisterRespModel secondUser = api.users.register(td2.registrationData());
    //login second user
    String secondAccessToken = api.auth.extractAccessToken(td2.loginData());
    //create club for second user
    CreateClubRespModel createdClub = api.clubs.createClub(secondAccessToken, td2.createClubData());
    //create review and return
    ReviewReqModel reviewReqModel = new ReviewReqModel(createdClub.id(),td.review(),td.assessment(),td.readPages());

    return api.review.createReview(token, reviewReqModel);
  }


}
