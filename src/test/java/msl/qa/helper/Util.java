package msl.qa.helper;

import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Util {

  @Step("Format date with pattern 'd MMMM yyyy'")
  public static String formatDate(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru"));
    return date.format(formatter) + " г.";
  }

  @Step("Format date with pattern 'd MMMM yyyy'")
  public static LocalDate getDateFromJson(String date) {
    return ZonedDateTime.parse(date).toLocalDate();
  }

}
