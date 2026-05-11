package msl.qa.spec.clubs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static msl.qa.spec.BaseSpec.reqSpec;
import static org.hamcrest.Matchers.notNullValue;

public class ClubsSpec {

  public static RequestSpecification clubsReqSpec = reqSpec;

  public static ResponseSpecification clubsListRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/clubs_list_response_schema.json"))
          .expectBody("count", notNullValue())
          .expectBody("results", notNullValue())
          .build();
}

