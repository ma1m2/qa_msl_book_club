package msl.qa.tests;

import io.restassured.RestAssured;
import msl.qa.api.ApiClient;
import msl.qa.models.login.LoginReqModel;
import msl.qa.models.register.RegistrationReqModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

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
  protected RegistrationReqModel registrationData;
  protected LoginReqModel loginData;

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
  }

}
