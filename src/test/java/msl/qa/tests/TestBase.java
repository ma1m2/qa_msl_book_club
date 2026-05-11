package msl.qa.tests;

import io.restassured.RestAssured;
import msl.qa.api.ApiClient;
import msl.qa.models.clubs.CreateClubReqModel;
import msl.qa.models.clubs.CreateClubRespModel;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.register.RegistrationReqModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.ZoneOffset;

import static msl.qa.tests.TestData.PASSWORD;

public class TestBase {

  protected static final ApiClient api = new ApiClient();
  protected String username;
  protected String password;
  protected String firstName;
  protected String updatedFirstName;
  protected String lastName;
  protected String email;
  protected String token;
  protected String description;
  protected String newDescription;
  protected String telegramChatLink;
  protected RegistrationReqModel registrationData;
  protected LoginReqModel loginData;
  protected CreateClubReqModel createClubData;

  @BeforeAll
  public static void setUp() {
    RestAssured.baseURI = "https://book-club.qa.guru";
    RestAssured.basePath = "/api/v1";
  }

  @BeforeEach
  public void prepareTestData() {
    Faker faker = new Faker();
    username = faker.name().firstName() + "sssss";
    password = PASSWORD;
    firstName = faker.name().firstName();
    updatedFirstName = faker.name().firstName();
    lastName = faker.name().lastName();
    email = faker.internet().emailAddress();
    description = faker.lorem().sentence(8);
    newDescription = faker.lorem().sentence(9);
    telegramChatLink = "https://t.me/" + faker.regexify("[a-z]{10}");

    int publicationTimestampSeconds = (int) LocalDate
            .of(faker.number().numberBetween(1970, 2038), 1, 1)
            .atStartOfDay()
            .toEpochSecond(ZoneOffset.UTC);
    createClubData = new CreateClubReqModel(
            "QA Club " + faker.number().digits(6),
            faker.book().author(),
            publicationTimestampSeconds,
            faker.lorem().sentence(10),
            "https://t.me/" + faker.regexify("[a-z]{10}")
    );
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
