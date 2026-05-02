package msl.qa.spec.user;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static msl.qa.spec.BaseSpec.reqSpec;
import static org.hamcrest.Matchers.notNullValue;

public class UserSpec {

  public static RequestSpecification userReqSpec = reqSpec;

  public static ResponseSpecification updateRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/update/update_resp_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("username", notNullValue())
          .expectBody("firstName", notNullValue())
          .expectBody("lastName", notNullValue())
          .expectBody("email", notNullValue())
          .expectBody("remoteAddr", notNullValue())
          .build();

  public static ResponseSpecification updateUnauthorizedUserRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(401)
          .expectBody(matchesJsonSchemaInClasspath("schemas/update/unauthorised_resp_schema.json"))
          .expectBody("detail", notNullValue())
          .expectBody("code", notNullValue())
          .build();


}
