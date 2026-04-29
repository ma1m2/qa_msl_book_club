package msl.qa.spec.login;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static msl.qa.spec.BaseSpec.reqSpec;
import static org.hamcrest.Matchers.notNullValue;

public class LoginSpec {

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

  public static ResponseSpecification emptyPasswordLoginRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(400)
          .expectBody(matchesJsonSchemaInClasspath("schemas/login/empty_password_login_response_schemas.json"))
          .expectBody("password", notNullValue())
          .build();

  public static ResponseSpecification emptyUserLoginRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(400)
          .expectBody(matchesJsonSchemaInClasspath("schemas/login/empty_user_login_response_schemas.json"))
          .expectBody("username", notNullValue())
          .build();

}
