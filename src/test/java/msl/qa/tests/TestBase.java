package msl.qa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import msl.qa.allure.Attach;
import msl.qa.api.ApiClient;
import msl.qa.models.clubs.CreateClubReqModel;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.register.RegistrationReqModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static msl.qa.tests.TestData.*;

public class TestBase {

  protected static final ApiClient api = new ApiClient();
  protected String username;
  protected String password;
  protected String firstName;
  protected String updatedFirstName;
  protected String lastName;
  protected String email;
  protected String token;
  protected String bookTitle;
  protected String bookAuthors;
  protected Integer publicationYear;
  protected String description;
  protected String newDescription;
  protected String telegramChatLink;
  protected RegistrationReqModel registrationData;
  protected LoginReqModel loginData;
  protected CreateClubReqModel createClubData;

  @BeforeAll
  public static void setUp() {
    RestAssured.baseURI = "http://localhost:8100";
    RestAssured.basePath = "/api/v1";
    Configuration.browserSize = "1920x1080";
  }

  @BeforeEach
  public void prepareTestDataAndAddListener() {
    SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    Faker faker = new Faker();
    username = faker.name().firstName() + "abc";
    password = PASSWORD;
    firstName = faker.name().firstName();
    updatedFirstName = faker.name().firstName();
    lastName = faker.name().lastName();
    email = faker.internet().emailAddress();
    bookTitle = faker.book().title();
    bookAuthors = faker.book().author();
    publicationYear = faker.number().numberBetween(1950, 2025);
    description = faker.lorem().sentence(10);
    newDescription = faker.lorem().sentence(9);
    telegramChatLink = TELEGRAM_CHAT_LINK;//"https://t.me/" + faker.regexify("[a-z]{10}");

/*    int publicationTimestampSeconds = (int) LocalDate
            .of(faker.number().numberBetween(1970, 2038), 1, 1)
            .atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC);*/
    createClubData = new CreateClubReqModel(
            bookTitle,
            bookAuthors,
            publicationYear,
            description,
            telegramChatLink
    );
  }

  //@AfterEach
  public void addAttachments() {
    Attach.screenshotAs("Last screenshot");
    Attach.pageSource();
    Attach.browserConsoleLogs();
   // Attach.addVideo();

    closeWebDriver();
  }

  protected String registerAndLoginNewUser() {
    registrationData = new RegistrationReqModel(username, password);
    api.users.register(registrationData);
    return api.auth.extractAccessToken(new LoginReqModel(username, password));
  }

  protected CreateClubRespModel createClubByNewUser() {
    String accessToken = registerAndLoginNewUser();
    return api.clubs.createClub(accessToken, createClubData);
  }

}
