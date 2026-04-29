package msl.qa.spec.register;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static msl.qa.spec.BaseSpec.reqSpec;
import static org.hamcrest.Matchers.notNullValue;

public class RegistrationSpec {

  public static RequestSpecification registerReqSpec = reqSpec;

  public static RequestSpecification noContentTypeReqSpec = with()
          .log().all();

  public static ResponseSpecification registerRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(201)
          .expectBody(matchesJsonSchemaInClasspath("schemas/register/register_response_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("username", notNullValue())
          .expectBody("remoteAddr", notNullValue())
          .build();

  public static ResponseSpecification existingUserRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(400)
          .expectBody(matchesJsonSchemaInClasspath("schemas/register/existin_user_response_schema.json"))
          .build();

  public static ResponseSpecification invalidUserNameRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(400)
          .expectBody(matchesJsonSchemaInClasspath("schemas/register/invalid_user_response_schema.json"))
          .build();

  public static ResponseSpecification wrongUrlRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(500)
          .build();

  public static ResponseSpecification noContentTypeRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(415)
          .expectBody(matchesJsonSchemaInClasspath("schemas/register/detail_response_schema.json"))
          .build();

}
