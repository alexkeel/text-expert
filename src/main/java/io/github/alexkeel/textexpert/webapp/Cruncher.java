package io.github.alexkeel.textexpert.webapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * Contains methods for handling text processing and analysis.
 */
public class Cruncher {
  private final List<Character> defaultHardstops = List.of('!', '?', ':', '.');

  private final Scanner content;
  private final boolean romanNumeralDetection;
  private final List<Character> hardStops;
  private final int outputPrecision;
  private final boolean fullResultsEnabled;

  private String results;

  private Scanner alpha3;
  private Scanner alpha4;
  private Scanner alpha5;
  private Scanner alpha6;
  private Scanner alphaAaps;
  private Scanner alphaEnd;
  private Scanner fixit;

  private static final Logger logger = LoggerFactory.getLogger(Cruncher.class);

  private String currentWord;
  private boolean apostropheFound;
  private int sentences;

  Cruncher(final InputStream content,
           final boolean romanNumeralDetection,
           final List<Character> hardStops,
           final int outputPrecision,
           final boolean fullResultsEnabled) {
    this.content = new Scanner(content, StandardCharsets.UTF_8);
    this.romanNumeralDetection = romanNumeralDetection;
    this.hardStops = hardStops.isEmpty() ? defaultHardstops : hardStops;
    this.outputPrecision = outputPrecision;
    this.fullResultsEnabled = fullResultsEnabled;
  }

  /**
   * Performs analysis on given text and returns results as string.
   */
  public String run() {
    prepFiles();

    return "Nothing yet";
  }

  private void loop() {
    accept();
  }

  private void accept() {
    content.useDelimiter(""); // Read character by character

    StringBuilder word = new StringBuilder();
    boolean wordStarted = false;

    while (content.hasNext()) {
      char ch = content.next().charAt(0);

      // Check for end of word
      if (isWordBoundary(ch)) {
        if (wordStarted) {
          if (this.hardStops.contains(ch)) {
            sentences++;
          }
          break; // End of word
        }
        continue; // Skip leading separators
      }

      if (word.length() >= 99) {
        logger.info("Warning: word cut at 99 characters");
        break;
      }

      wordStarted = true;
      word.append(ch);
      logger.info(String.valueOf(ch));
    }

    currentWord = word.toString();

    // Remove trailing apostrophe if it’s a possessive ('s)
    if (currentWord.endsWith("'") || currentWord.endsWith("’")) {
      currentWord = currentWord.substring(0, currentWord.length() - 1);
    }

    apostropheFound = currentWord.contains("'") || currentWord.contains("’");

    logger.info("\nAccepted word: {}", currentWord);
    logger.info("Contains apostrophe: {}", apostropheFound);

    content.close();
  }

  private boolean isWordBoundary(char ch) {
    return !Character.isLetterOrDigit(ch) && ch != '\'' && ch != '’';
  }

  private void prepFiles() {
    try {
      alpha3 = new Scanner(new File(readFile("ALPHA3.txt")), StandardCharsets.UTF_8);
      alpha3.useDelimiter("\\s+");
      alpha4 = new Scanner(new File(readFile("ALPHA4.txt")), StandardCharsets.UTF_8);
      alpha4.useDelimiter("\\s+");
      alpha5 = new Scanner(new File(readFile("ALPHA5.txt")), StandardCharsets.UTF_8);
      alpha5.useDelimiter("\\s+");
      alpha6 = new Scanner(new File(readFile("ALPHA6.txt")), StandardCharsets.UTF_8);
      alpha6.useDelimiter("\\s+");
      alphaAaps = new Scanner(new File(readFile("ALPHAAAPS.txt")), StandardCharsets.UTF_8);
      alphaAaps.useDelimiter("\\s+");
      alphaEnd = new Scanner(new File(readFile("ALPHAEND.txt")), StandardCharsets.UTF_8);
      alphaEnd.useDelimiter("\\s+");
      fixit = new Scanner(new File(readFile("FIXIT.txt")), StandardCharsets.UTF_8);
      fixit.useDelimiter("\\s+");
    } catch (IOException exception) {
      logger.error(exception.getMessage());
    }
  }

  private String readFile(final String filename) throws IOException {
    final ClassPathResource resource = new ClassPathResource("data/" + filename);
    try (final BufferedReader reader = new BufferedReader(
          new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }
}
