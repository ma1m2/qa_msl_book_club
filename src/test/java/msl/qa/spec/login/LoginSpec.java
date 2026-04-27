package msl.qa.spec.login;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import msl.qa.spec.BaseSpec;

import static io.restassured.RestAssured.with;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;

public class LoginSpec extends BaseSpec {

  public static RequestSpecification loginReqSpec = reqSpec;

  public static ResponseSpecification successLoginRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/login/login_response_schemas.json"))
          .expectBody("access", notNullValue())
          .expectBody("refresh", notNullValue())
          .build();

  public static ResponseSpecification wrongCredlsLoginRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(401)
          .expectBody(matchesJsonSchemaInClasspath("schemas/login/wrong_credls_login_response_schemas.json"))
          .expectBody("detail", notNullValue())
          .build();

}
