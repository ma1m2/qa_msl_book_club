package msl.qa.spec.review;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static msl.qa.spec.BaseSpec.reqSpec;
import static org.hamcrest.Matchers.notNullValue;

public class ReviewsSpec {

  public static RequestSpecification reviewsReqSpec = reqSpec;

  public static ResponseSpecification createReviewRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(201)
          .expectBody(matchesJsonSchemaInClasspath("schemas/review/create_review_response_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("club", notNullValue())
          .expectBody("created", notNullValue())
          .build();

}
