package msl.qa.spec;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;
import static msl.qa.allure.CustomAllureListener.withCustomTemplate;

public class BaseSpec {

  public static RequestSpecification reqSpec = with()
          .filter(withCustomTemplate())
          .log().all()
          .contentType(ContentType.JSON);
}
