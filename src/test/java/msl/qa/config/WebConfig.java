package msl.qa.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "classpath:config/${env}.properties",
        "classpath:config/local.properties"
})
public interface WebConfig extends Config {

  @Key("env")
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
    //@DefaultValue("https://user1:1234@selenoid.autotests.cloud/wd/hub")
  String remoteUrl();

  @Key("remoteUrl")
  String videoUrl();

  @Key("browser")
  @DefaultValue("chrome")
  String browser();

  @Key("browserVersion")
  String browserVersion();

  @Key("browserSize")
  @DefaultValue("1920x1080")
  String browserSize();

//===========selenoid:options=============
  @Key("enableVNC")
  boolean enableVNC();

  @Key("enableLog")
  boolean enableLog();

  @Key("enableVideo")
  boolean enableVideo();
}
