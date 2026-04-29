package msl.qa.spec.logout;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static msl.qa.spec.BaseSpec.reqSpec;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LogoutSpec {

  public static RequestSpecification logoutReqSpec =  reqSpec;

  public static ResponseSpecification successLogoutRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(equalTo("{}"))
          .build();

  public static ResponseSpecification wrongTokenLogoutRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(401)
          .expectBody(matchesJsonSchemaInClasspath("schemas/logout/wrong_token_logout_response_schema.json"))
          .expectBody("detail", notNullValue())
          .expectBody("code", notNullValue())
          .build();

}
