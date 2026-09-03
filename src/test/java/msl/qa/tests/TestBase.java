package msl.qa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import msl.qa.allure.Attach;
import msl.qa.api.ApiClient;
import msl.qa.config.WebConfig;
import msl.qa.helper.TestDataBuilder;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.clubs.review.ReviewReqModel;
import msl.qa.models.clubs.review.ReviewRespModel;
import msl.qa.models.localstorage.LocalStorageAuthReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.pages.ClubsPage;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;

public class TestBase {

  private static final WebConfig WEB_CONFIG = ConfigFactory.create(WebConfig.class, System.getProperties());
  protected static final ApiClient api = new ApiClient();

  protected ClubsPage clubsPage = new ClubsPage();

  protected TestDataBuilder td;
  protected TestDataBuilder td2;

  @BeforeAll
  static void setUp() {
    RestAssured.baseURI = WEB_CONFIG.baseURI();
    RestAssured.basePath = WEB_CONFIG.basePath();

    Configuration.baseUrl = WEB_CONFIG.uiBaseUrl();
    Configuration.browser = WEB_CONFIG.browser();
    Configuration.browserSize = WEB_CONFIG.browserSize();
    Configuration.browserVersion = WEB_CONFIG.browserVersion();
    Configuration.timeout = 6000;

    String remote = WEB_CONFIG.remoteUrl();
    if (remote != null && !remote.isEmpty()) {
      Configuration.remote = remote;

      Configuration.browserCapabilities.setCapability("selenoid:options",
              Map.of("enableVNC", WEB_CONFIG.enableVNC(),
                      "enableLog", WEB_CONFIG.enableLog(),
                      "enableVideo",  WEB_CONFIG.enableVideo()));
    }
  }

  @BeforeEach
  public void prepareTestDataAndAddListener() {
    SelenideLogger.addListener("allureSelenide", new AllureSelenide());
    td = new TestDataBuilder();
    td2 = new TestDataBuilder();
  }

  @AfterEach
  void addAttachments() {
    if(webdriver().driver().hasWebDriverStarted()) {
      Attach.screenshotAs("Last screenshot");
      Attach.pageSource();
      Attach.browserConsoleLogs();

      // Проверяем, что в remote
      String env = WEB_CONFIG.env();
      if ("remote".equalsIgnoreCase(env) || "ci".equalsIgnoreCase(env)) {
        Attach.addVideo(WEB_CONFIG);
      }

      closeWebDriver();
    }
  }

  @Step("[UI] User registration[API], session setup[localStorage], and page opening[UI]")
  protected LoginRespModel openClubsPageWithNewUser(String username, String password) {
    RegisterReqModel loginData = new RegisterReqModel(username, password);
    RegisterRespModel user = api.users.register(loginData);
    LoginRespModel loginResp = api.auth.successfulLogin(loginData);
    String localStorageAuthBody = new LocalStorageAuthReqModel(user,
            loginResp.access(), loginResp.refresh(), true).toJson();

    open("/favicon.ico");
    localStorage().setItem("book_club_auth", localStorageAuthBody);
    open("/");

    return loginResp;
  }

  @Step("[API] Register and login new user")
  protected String registerAndLoginNewUser() {
    api.users.register(td.registrationData());
    return api.auth.extractAccessToken(new RegisterReqModel(td.username(), td.password()));
  }

  @Step("[API] Create Review for Owner Club")
  protected ReviewRespModel createReviewOwnerClub(String token){
    //create club
    CreateClubRespModel createdClub = api.clubs.createClub(token, td.createClubData());
    //create review and return
    ReviewReqModel reviewReqModel = td.reviewData(createdClub.id());

    return api.review.createReview(token, reviewReqModel);
  }

}
