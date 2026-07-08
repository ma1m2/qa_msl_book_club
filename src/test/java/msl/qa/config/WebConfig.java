package msl.qa.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:config/${env}.properties",
        "classpath:config/remote.properties"
})
public interface WebConfig extends Config {

  @Key("env")
  @DefaultValue("remote")
  String env();

  @Key("baseURI")
  @DefaultValue("https://book-club.qa.guru")
  String baseURI();

  @Key("basePath")
  @DefaultValue("/api/v1")
  String basePath();

  @Key("uiBaseUrl")
  @DefaultValue("https://book-club.qa.guru")
  String uiBaseUrl();

  @Key("remoteUrl")
  @DefaultValue("https://user1:1234@selenoid.autotests.cloud/wd/hub")
  String remoteUrl();

  @Key("browser")
  @DefaultValue("chrome")
  String browser();

  @Key("browserVersion")
  @DefaultValue("128.0")
  String browserVersion();

  @Key("browserSize")
  @DefaultValue("1920x1080")
  String browserSize();

//===========selenoid:options=============
  @Key("enableVNC")
  @DefaultValue("true")
  boolean enableVNC();

  @Key("enableLog")
  @DefaultValue("true")
  boolean enableLog();

  @Key("enableVideo")
  @DefaultValue("true")
  boolean enableVideo();
  //enableVNC=true
  //enableLog=true
  //enableVideo=true


}
