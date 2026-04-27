package msl.qa.tests;

public class TestData {

  //-------------LOGIN-------------------
  public static final String USERNAME = "qamsl";
  public static final String PASSWORD = "1234";
  public static final String WRONG_PASSWORD = "12345";
  public static final String TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
  public static final String WRONG_CREDLS_DETAIL = "Invalid username or password.";
  public static final String BLANK_FIELD = "This field may not be blank.";

  //-------------REGISTER-------------------
  public static final String EXISTING_USER_ERROR = "A user with that username already exists.";
  public static final String IP_REGEXP = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
  public static final String ENTER_VALID_USERNAME = "Enter a valid username.";
  public static final String UNSUPPORTED_MEDIA_TYPE = "Unsupported media type";
}
