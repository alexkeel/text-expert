package io.github.alexkeel.textexpert.webapp.lexer;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrdinalClassifier extends Classifier {

  private static final Logger logger = LoggerFactory.getLogger(OrdinalClassifier.class);

  private static final Map<String, Integer> ordinalMap = new HashMap<>();

  static {
    // Initialize ordinal-to-count mappings
    ordinalMap.put("1st", 1);
    ordinalMap.put("2nd", 2);
    ordinalMap.put("3rd", 1);
    ordinalMap.put("4th", 1);
    ordinalMap.put("5th", 1);
    ordinalMap.put("6th", 1);
    ordinalMap.put("7th", 2);
    ordinalMap.put("8th", 1);
    ordinalMap.put("9th", 1);
    ordinalMap.put("10th", 1);
    ordinalMap.put("11th", 3);
    ordinalMap.put("12th", 1);
    ordinalMap.put("13th", 2);
    ordinalMap.put("14th", 2);
    ordinalMap.put("15th", 2);
    ordinalMap.put("16th", 2);
    ordinalMap.put("17th", 3);
    ordinalMap.put("18th", 2);
    ordinalMap.put("19th", 2);
    ordinalMap.put("20th", 3);
    ordinalMap.put("21st", 3);
    ordinalMap.put("22nd", 4);
    ordinalMap.put("23rd", 3);
    ordinalMap.put("24th", 3);
    ordinalMap.put("25th", 3);
    ordinalMap.put("26th", 3);
    ordinalMap.put("27th", 4);
    ordinalMap.put("28th", 3);
    ordinalMap.put("29th", 3);
    ordinalMap.put("30th", 3);
    ordinalMap.put("31st", 3);
  }

  @Override
  public boolean check(String word) {

    logger.info("Searching for ordinal match: ");

    if (ordinalMap.containsKey(word)) {
      syllableCount = ordinalMap.get(word);
      logger.info("Ordinal found");
      return true;
    } else {
      logger.info("No ordinal found");
      return false;
    }
  }
}
