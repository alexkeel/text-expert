package io.github.alexkeel.textexpert.webapp.lexer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FixClassifier extends Classifier {

  private static final Logger logger = LoggerFactory.getLogger(FixClassifier.class);

  public FixClassifier(String file) throws IOException {
    super(file);
  }

  @Override
  public boolean check(final String word)
  {
    while (validTokens.hasNextLine()) {
      String line = validTokens.nextLine().trim();

      // Skip blank lines
      if (line.isEmpty()) {
        continue;
      }

      int equalsIndex = line.indexOf('=');
      if (equalsIndex == -1 || equalsIndex == 0 || equalsIndex == line.length() - 1) {
        logger.error("Invalid format in line: {}", line);
        return false;
      }

      String fix = line.substring(0, equalsIndex).trim();
      String valueStr = line.substring(equalsIndex + 1).trim();

      if (!valueStr.matches("\\d")) {
        logger.info("\nCan't find valid value for fixit entry in: {}", line);
        return false;
      }

      int i = Integer.parseInt(valueStr);

      if (fix.equals(word)) {
        logger.info("Fix match found");
        syllableCount = i;
        return true; // Found match; stop reading
      }
    }

    return false;
  }
}
