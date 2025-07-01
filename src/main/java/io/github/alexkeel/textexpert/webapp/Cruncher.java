package io.github.alexkeel.textexpert.webapp;

import io.github.alexkeel.textexpert.webapp.lexer.AcronymClassifier;
import io.github.alexkeel.textexpert.webapp.lexer.ApostropheClassifier;
import io.github.alexkeel.textexpert.webapp.lexer.FixClassifier;
import io.github.alexkeel.textexpert.webapp.lexer.OrdinalClassifier;
import io.github.alexkeel.textexpert.webapp.lexer.PhonemeClassifier;
import io.github.alexkeel.textexpert.webapp.lexer.RomanNumeralClassifier;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  private AcronymClassifier acronymClassifier;
  private FixClassifier fixClassifier;
  private RomanNumeralClassifier romanNumeralClassifier;
  private OrdinalClassifier ordinalClassifier;
  private ApostropheClassifier apostropheClassifier;
  private PhonemeClassifier phonemeClassifier;

  private Scanner alpha3;
  private Scanner alpha4;
  private Scanner alpha5;
  private Scanner alpha6;
  private Scanner alphaAaps;
  private Scanner alphaEnd;

  private static final Logger logger = LoggerFactory.getLogger(Cruncher.class);

  private String currentWord;
  private boolean apostropheFound;
  private boolean phonemeFound;
  private int sentences;
  private boolean passed;
  private boolean acronymMatch;
  private boolean romanMatch;
  private boolean acroEof;
  private boolean fixMatch;
  private boolean fixEof;
  private boolean ordinalMatch;
  private boolean apsMatch;
  private int syllableCount;
  private int vflag;
  private boolean endingFlag;

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

    loop();

    return "Nothing yet";
  }

  private void loop() {
    accept();
  }

  /**
   * Lexer
   */
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
      logger.info("Appended {} to word", ch);
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

  // Detection, correction, and classification.
  public void detect() throws IOException {
    passed = false;
    acronymMatch = false;
    romanMatch = false;
    acroEof = false;
    fixMatch = false;
    fixEof = false;
    ordinalMatch = false;
    apsMatch = false;
    syllableCount = 0;
    vflag = 0;
    endingFlag = false;

    // Prepare word data
    var word = currentWord;

    // Acronym check
    acronymMatch = acronymClassifier.check(word);
    if (acronymMatch) {
      logger.info("Acronym match found");
      syllableCount += acronymClassifier.getSyllableCount();
      return;
    }

    // Search fixes
    fixMatch = fixClassifier.check(word);
    if(fixMatch) {
      logger.info("Match found in fixes");
      syllableCount += fixClassifier.getSyllableCount();
      return;
    }

    // Roman numeral check
    romanMatch = romanNumeralClassifier.check(word);
    if(romanMatch) {
      logger.info("Match found in roman numerals");
      syllableCount += romanNumeralClassifier.getSyllableCount();
      return;
    }

    // Ordinal check
    ordinalMatch = ordinalClassifier.check(word);
    if(ordinalMatch) {
      logger.info("Ordinal found");
      syllableCount += ordinalClassifier.getSyllableCount();
      return;
    }

    // Apostrophe check
    apostropheFound = apostropheClassifier.check(word);
    if(apostropheFound) {
      logger.info("Apostrophe found");
      syllableCount = apostropheClassifier.getSyllableCount();
      return;
    }

    // Vowel/consonant analysis
    phonemeFound = phonemeClassifier.check(word);
    if(phonemeFound) {
      logger.info("Phoneme found");
      syllableCount = phonemeClassifier.getSyllableCount();
      return;
    }

    if (syllableCount == 0) {
      syllableCount = 1;
      logger.info("Minimum count set");
    }
  }

  private boolean isWordBoundary(char ch) {
    return !Character.isLetterOrDigit(ch) && ch != '\'' && ch != '’';
  }

  private void prepFiles() {
    try {
      romanNumeralClassifier = new RomanNumeralClassifier();
      ordinalClassifier = new OrdinalClassifier();
      acronymClassifier = new AcronymClassifier("acronym.txt");
      fixClassifier = new FixClassifier("FIXIT.txt");
      apostropheClassifier = new ApostropheClassifier("ALPHAAPS.txt");
      phonemeClassifier = new PhonemeClassifier();

      // Convert each below to their own classes

      //alpha3 = new Scanner(new File(readFile("ALPHA3.txt")), StandardCharsets.UTF_8);
      //alpha3.useDelimiter("\\s+");
      //alpha4 = new Scanner(new File(readFile("ALPHA4.txt")), StandardCharsets.UTF_8);
      //alpha4.useDelimiter("\\s+");
      //alpha5 = new Scanner(new File(readFile("ALPHA5.txt")), StandardCharsets.UTF_8);
      //alpha5.useDelimiter("\\s+");
      //alpha6 = new Scanner(new File(readFile("ALPHA6.txt")), StandardCharsets.UTF_8);
      //alpha6.useDelimiter("\\s+");
      //alphaAaps = new Scanner(new File(readFile("ALPHAAAPS.txt")), StandardCharsets.UTF_8);
      //alphaAaps.useDelimiter("\\s+");
      //alphaEnd = new Scanner(new File(readFile("ALPHAEND.txt")), StandardCharsets.UTF_8);
      //alphaEnd.useDelimiter("\\s+");
    } catch (IOException exception) {
      logger.error(exception.getMessage());
    }
  }
}
