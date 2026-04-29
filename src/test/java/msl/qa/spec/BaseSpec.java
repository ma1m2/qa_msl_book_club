package msl.qa.spec;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

public class BaseSpec {
  public final static String basePath = "/api/v1";

  public static RequestSpecification reqSpec = with()
          .log().all()
          .contentType(ContentType.JSON)
          .basePath(basePath);
}
