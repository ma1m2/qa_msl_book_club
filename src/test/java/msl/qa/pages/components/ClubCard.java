package msl.qa.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import msl.qa.pages.ClubPage;

import static com.codeborne.selenide.Selenide.$;

public class ClubCard {

  private final SelenideElement clubHeader = $(".club-header h2"),
          joinBtn = $(".join-btn"),
          openBtn = $(".open-btn");

  @Step("[UI] Open club where you are member")
  public ClubPage openClub(){
    openBtn.click();
    return new ClubPage();
  }

  @Step("[UI] Open club where you are member")
  public ClubPage joinClub(){
    joinBtn.click();
    return new ClubPage();
  }

}
