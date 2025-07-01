package io.github.alexkeel.textexpert.webapp.lexer;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApostropheClassifier extends Classifier {

  private static final Logger logger = LoggerFactory.getLogger(ApostropheClassifier.class);

  public ApostropheClassifier(final String file) throws IOException {
    super(file);
  }

  @Override
  public boolean check(final String word) {
    Pattern pattern = Pattern.compile("^(.+?)=(\\d)\\b");

    while (validTokens.hasNextLine()) {
      String line = validTokens.nextLine().trim();
      if (line.isEmpty())
        continue;

      Matcher matcher = pattern.matcher(line);
      if (!matcher.matches())
        continue;

      String alph = matcher.group(1).trim();
      int i = Integer.parseInt(matcher.group(2));

      if (alph.equals(word)) {
        logger.info("Token recalled from alphaaps");
        syllableCount = i;
        return true;
      }
    }
    return false;
  }
}
