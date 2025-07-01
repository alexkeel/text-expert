package io.github.alexkeel.textexpert.webapp.lexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RomanNumeralClassifier extends Classifier{

  private static final Logger logger = LoggerFactory.getLogger(RomanNumeralClassifier.class);
  private static final Map<String, Integer> romanMap = new HashMap<>();

  static {
    // Initialize Roman numerals and their associated "count" values
    romanMap.put("ii", 1);
    romanMap.put("iii", 1);
    romanMap.put("iv", 1);
    romanMap.put("v", 1);
    romanMap.put("vi", 1);
    romanMap.put("vii", 2);
    romanMap.put("viii", 1);
    romanMap.put("ix", 1);
    romanMap.put("x", 1);
    romanMap.put("xi", 3);
    romanMap.put("xii", 1);
    romanMap.put("xiii", 2);
    romanMap.put("xiv", 2);
    romanMap.put("xv", 2);
    romanMap.put("xvi", 2);
    romanMap.put("xvii", 3);
    romanMap.put("xviii", 2);
    romanMap.put("xix", 2);
    romanMap.put("xx", 2);
    romanMap.put("xxi", 3);
    romanMap.put("xxii", 3);
    romanMap.put("xxiii", 3);
    romanMap.put("xxiv", 3);
    romanMap.put("xxv", 3);
    romanMap.put("xxvi", 3);
    romanMap.put("xxvii", 4);
    romanMap.put("xxviii", 3);
    romanMap.put("xxix", 3);
    romanMap.put("xxx", 2);
    romanMap.put("xxxi", 3);
    romanMap.put("xxxii", 3);
    romanMap.put("xxxiii", 3);
    romanMap.put("xxxiv", 3);
    romanMap.put("xxxv", 3);
    romanMap.put("xxxvi", 3);
    romanMap.put("xxxvii", 4);
    romanMap.put("xxxviii", 3);
    romanMap.put("xxxix", 3);
    romanMap.put("xl", 2);
    romanMap.put("xli", 3);
    romanMap.put("xlii", 3);
    romanMap.put("xliii", 3);
    romanMap.put("xliv", 3);
    romanMap.put("xlv", 3);
    romanMap.put("xlvi", 3);
    romanMap.put("xlvii", 4);
    romanMap.put("xlviii", 3);
    romanMap.put("il", 3); // uncommon/incorrect form, but included for parity
  }

  @Override
  public boolean check(final String word) {
    logger.info("\nSearching for Roman numeral match: ");

    final String lowerCase = word.toLowerCase();  // Make it case-insensitive
    if (romanMap.containsKey(lowerCase)) {
      syllableCount = romanMap.get(lowerCase);
      logger.info("Roman numeral detected");
      return true;
    } else {
      logger.info("No match found");
      return false;
    }
  }
}
