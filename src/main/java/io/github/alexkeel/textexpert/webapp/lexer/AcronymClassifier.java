package io.github.alexkeel.textexpert.webapp.lexer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcronymClassifier extends Classifier
{
  private static final Logger logger = LoggerFactory.getLogger(AcronymClassifier.class);

  public AcronymClassifier(final String acronymListLocation) throws IOException {
    super(acronymListLocation);
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
        logger.error("Invalid line format: {}", line);
        return false;
      }

      String acronym = line.substring(0, equalsIndex).trim();
      String valueStr = line.substring(equalsIndex + 1).trim();

      if (!valueStr.matches("\\d")) {
        logger.error("\nCan't find valid value for acronym entry in: {}", line);
        return false;
      }

      int i = Integer.parseInt(valueStr);

      if (acronym.equals(word)) {
        logger.info("Acronym match found");
        syllableCount = i;
        return true;  // Stop after finding the match
      }
    }

    return false;
  }
}
