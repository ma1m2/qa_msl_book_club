package msl.qa.pages;

import io.qameta.allure.Step;
import msl.qa.api.ApiClient;
import msl.qa.models.localstorage.LocalStorageAuthReqModel;
import msl.qa.models.login.LoginRespModel;
import msl.qa.models.register.RegisterReqModel;
import msl.qa.models.register.RegisterRespModel;
import msl.qa.pages.components.Header;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;

public class BasePage {

  protected Header header = new Header();
  protected ApiClient api = new ApiClient();

  @Step("[UI] User registration[API], session setup[localStorage], and page opening[UI]")
  public LoginRespModel openMainPageWithNewUser(String username, String password) {
    RegisterReqModel loginData = new RegisterReqModel(username, password);
    //register user
    RegisterRespModel user = api.users.register(loginData);
    //login user
    LoginRespModel loginResp = api.auth.successfulLogin(loginData);
    //create localStorage
    String localStorageAuthBody = new LocalStorageAuthReqModel(user,
            loginResp.access(), loginResp.refresh(), true).toJson();

    openFaviconAndSetLocalStorageAndMainPage("book_club_auth", localStorageAuthBody);

    return loginResp;
  }

  @Step("[UI] Открытие /favicon.ico и установка данных в localstorage")
  public void openFaviconAndSetLocalStorageAndMainPage(String key, String value) {
    openFavicon();
    localStorage().setItem(key, value);
    open("/");
  }

  @Step("[UI] Открытие /favicon.ico")
  public void openFavicon () {
    open("/favicon.ico");
  }
}
