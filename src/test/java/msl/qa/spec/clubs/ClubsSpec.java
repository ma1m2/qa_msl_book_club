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

  public static ResponseSpecification createClubRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(201)
          .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/create_club_response_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("owner", notNullValue())
          .build();

  public static ResponseSpecification getClubRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/create_club_response_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("owner", notNullValue())
          .build();

  public static ResponseSpecification updateClubRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/create_club_response_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("owner", notNullValue())
          .build();

  public static ResponseSpecification deleteClubRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(204)
          .build();
}

