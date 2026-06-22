package msl.qa.helper;

import msl.qa.models.clubs.CreateClubReqModel;
import msl.qa.models.register.RegisterReqModel;
import net.datafaker.Faker;

import static msl.qa.tests.TestData.*;

public class TestDataBuilder {
  Faker faker = new Faker();

  private final String username;
  private final String password;
  private final String firstName;
  private final String updatedFirstName;
  private final String lastName;
  private final String email;
  private final String bookTitle;
  private final String bookAuthors;
  private final Integer publicationYear;
  private final String description;
  private final String udatedDescription;
  private final String telegramChatLink;
  private final String updatedTelegramChatLink;
  private final RegisterReqModel registrationData;
  private final RegisterReqModel loginData;
  private final CreateClubReqModel createClubData;

  public TestDataBuilder() {
    String suffix = "_" + System.currentTimeMillis() / 1000;
    this.username = faker.name().firstName() + suffix;
    this.password = PASSWORD;
    this.firstName = faker.name().firstName();
    this.updatedFirstName = faker.name().firstName();
    this.lastName = faker.name().lastName();
    this.email = faker.internet().emailAddress();
    this.bookTitle = faker.book().title() + suffix;
    this.bookAuthors = faker.book().author() + suffix;
    this.publicationYear = faker.number().numberBetween(1950, 2025);
    this.description = faker.lorem().sentence(10);
    this.udatedDescription = faker.lorem().sentence(9);
    this.telegramChatLink = TELEGRAM_CHAT_LINK;
    this.updatedTelegramChatLink = "https://t.me/" + faker.regexify("[a-z]{10}");
    this.registrationData = new RegisterReqModel(username, password);
    this.loginData = registrationData;
    this.createClubData = new CreateClubReqModel(
            bookTitle,
            bookAuthors,
            publicationYear,
            description,
            telegramChatLink
    );
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }

  public String firstname() {
    return firstName;
  }

  public String updatedFirstName() {
    return updatedFirstName;
  }

  public String lastname() {
    return lastName;
  }

  public String email() {
    return email;
  }

  public String bookTitle() {
    return bookTitle;
  }

  public String bookAuthors() {
    return bookAuthors;
  }

  public Integer publicationYear() {
    return publicationYear;
  }

  public String description() {
    return description;
  }

  public String updatedDescription() {
    return udatedDescription;
  }

  public String telegramChatLink() {
    return telegramChatLink;
  }

  public String updatedTelegramChatLink() {
    return updatedTelegramChatLink;
  }

  public RegisterReqModel registrationData() {
    return registrationData;
  }

  public RegisterReqModel loginData() {
    return loginData;
  }

  public CreateClubReqModel createClubData() {
    return createClubData;
  }
}
