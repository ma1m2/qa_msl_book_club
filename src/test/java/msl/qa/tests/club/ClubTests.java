package msl.qa.tests.club;

import msl.qa.tests.TestBase;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.disappear;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;

public class ClubTests extends TestBase {

  @Test
  public void cantLeaveClubAsAdminUiTest() {
    //register user
    System.out.println("### " + username + " " + password);
    open("http://localhost:8100/signup");//https://book-club.qa.guru
    $("[data-testid=username-input]").setValue(username);
    $("[data-testid=password-input]").setValue(password);
    $("[data-testid=confirm-password-input]").setValue(password).pressEnter();
    $("[data-testid=signup-button]").should(disappear);

    //create club
    $("[data-testid=create-club-link]").click();
    $("#bookTitle").setValue(bookTitle);
    $("#bookAuthors").setValue(bookAuthors);
    $("#publicationYear").setValue(String.valueOf(publicationYear));
    $("#description").setValue(description);
    $("#telegramChatLink").setValue(telegramChatLink);
    $("button.submit-btn").click();

    // open club
    $$(".club-card h2").findBy(text(bookTitle)).scrollTo().parent().parent().$(".open-btn").click();

    //wrong leaving club
    $(".club-content").shouldBe(visible);
    $(".leave-btn").click();
    confirm();
    $(".error").shouldHave(text("Не удалось покинуть клуб"));
  }
}
