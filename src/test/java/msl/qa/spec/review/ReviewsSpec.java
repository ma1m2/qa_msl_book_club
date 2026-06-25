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

  public static ResponseSpecification getReviewRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/review/review_list_respons_schema.json"))
          .expectBody("count", notNullValue())
          .expectBody("results", notNullValue())
          .build();

  public static ResponseSpecification getReviewByIdRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/review/create_review_response_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("club", notNullValue())
          .expectBody("created", notNullValue())
          .build();

  public static ResponseSpecification updatedReviewRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(matchesJsonSchemaInClasspath("schemas/review/create_review_response_schema.json"))
          .expectBody("id", notNullValue())
          .expectBody("club", notNullValue())
          .expectBody("created", notNullValue())
          .build();

  public static ResponseSpecification deleteReviewRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(204)
          .build();

  public static ResponseSpecification nonExistReviewId = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(404)
          .expectBody(matchesJsonSchemaInClasspath("schemas/detail_response_schema.json"))
          .build();

  public static ResponseSpecification noPermission = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(403)
          .expectBody(matchesJsonSchemaInClasspath("schemas/detail_response_schema.json"))
          .build();
}
