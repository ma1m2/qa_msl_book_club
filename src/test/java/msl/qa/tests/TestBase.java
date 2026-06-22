package msl.qa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import msl.qa.allure.Attach;
import msl.qa.api.ApiClient;
import msl.qa.helper.TestDataBuilder;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.pages.ClubPage;
import msl.qa.pages.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.webdriver;

public class TestBase {

  protected static final ApiClient api = new ApiClient();

  protected MainPage mainPage = new MainPage();
  protected ClubPage clubPage = new ClubPage();
  protected TestDataBuilder td;

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

  protected String registerAndLoginNewUser() {
    api.users.register(td.registrationData());
    return api.auth.extractAccessToken(new RegisterReqModel(td.username(), td.password()));
  }

}
