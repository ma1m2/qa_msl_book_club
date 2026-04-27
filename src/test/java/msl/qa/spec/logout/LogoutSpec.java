package msl.qa.spec.logout;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import msl.qa.spec.BaseSpec;

import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;

public class LogoutSpec extends BaseSpec {

  public static RequestSpecification logoutReqSpec =  reqSpec;

  public static ResponseSpecification successLogoutRespSpec = new ResponseSpecBuilder()
          .log(LogDetail.ALL)
          .expectStatusCode(200)
          .expectBody(equalTo("{}"))
          .build();

}
