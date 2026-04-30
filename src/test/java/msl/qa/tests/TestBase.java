package msl.qa.tests;

import io.restassured.RestAssured;
import msl.qa.api.ApiClient;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

  protected static final ApiClient api = new ApiClient();

  @BeforeAll
  public static void setUp() {
    RestAssured.baseURI = "https://book-club.qa.guru";
    RestAssured.basePath = "/api/v1";
  }

}
