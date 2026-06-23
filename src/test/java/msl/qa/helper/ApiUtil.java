package msl.qa.helper;

import io.qameta.allure.Step;

import java.util.List;
import java.util.Random;

public class ApiUtil {

  @Step("Get random number from list")
  public static Integer getRandomIntegerFromList(List<Integer> list) {
    if (list == null || list.isEmpty()) {
      throw new RuntimeException("### Список пуст! " + list);
    }
    Random rand = new Random();
    return list.get(rand.nextInt(list.size()));
  }
}
