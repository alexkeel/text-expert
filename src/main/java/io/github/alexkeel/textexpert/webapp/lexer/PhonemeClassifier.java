package io.github.alexkeel.textexpert.webapp.lexer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhonemeClassifier extends Classifier{

  private static final Logger logger = LoggerFactory.getLogger(PhonemeClassifier.class);

  private int vflag;
  private int length;
  private String word;
  private boolean endingflag;
  private boolean numberflag;

  @Override
  public boolean check(final String word) {
    this.length = word.length();
    this.word = word;

    for (int n = 0; n <= length; n++) {
      char member = word.charAt(n);
      switch (member) {
        case 'a':
          if (vflag == 0) acheck(n);
          vflag++;
          break;
        case 'e':
          if (vflag == 0) echeck(n);
          vflag++;
          break;
        case 'i':
          if (vflag == 0) icheck(n);
          vflag++;
          break;
        case 'o':
          if (vflag == 0) ocheck(n);
          vflag++;
          break;
        case 'u':
          if (vflag == 0) ucheck(n);
          vflag++;
          break;
        case 'y':
          if (vflag == 0) ycheck(n);
          vflag++;
          break;
        default:
          defaultFunc(n);
          vflag = 0;
          break;
      }
    }
    return true;
  }

  public void acheck(int n) {
    logger.info("Detected an a");
    syllableCount++;

    if (n + 1 < length && isVowel(word.charAt(n + 1)) && length >= 2) {
      pass(n);
    }
  }

  public void echeck(int n) {
    if (n < length) {
      logger.info("Detected an e");
      syllableCount++;
    }

    // Silent 'e' in 'every'
    if (length > 3 && n - 2 >= 0 && n + 2 < length &&
        word.charAt(n - 2) == 'e' &&
        word.charAt(n - 1) == 'v' &&
        word.charAt(n + 1) == 'r' &&
        word.charAt(n + 2) == 'y') {
      logger.info("Silent e in 'every'");
      syllableCount--;
    }

    // Silent 'e' in 'ely' endings
    if (length > 3 && n + 2 == length &&
        word.charAt(n + 1) == 'l' &&
        word.charAt(n + 2) == 'y') {
      logger.info("Silent e in 'ely' ending");
      syllableCount--;
    }

    // Special case: 'etc'
    if (length == 2 && n + 2 < word.length() &&
        word.charAt(n + 1) == 't' &&
        word.charAt(n + 2) == 'c') {
      logger.info("Detected 'etc'");
      syllableCount = 4;
    }

    // ea, ee, ei, etc.
    if (n + 1 < length && isVowel(word.charAt(n + 1))) {
      pass(n);
    }

    // ed, es endings
    if (n + 1 == length - 1 &&
        (word.charAt(n + 1) == 'd' || word.charAt(n + 1) == 's') &&
        length >= 3) {
      endingflag = true;
      pass(n);
    }

    // 'le' endings
    if (n == length - 1 && length >= 3 &&
        word.charAt(n - 1) == 'l') {
      endingflag = true;
      syllableCount++;
      pass(n - 1);
    }
  }

  public void icheck(int n) {
    logger.info("Detected an i");
    syllableCount++;

    // Silent 'i' in 'business'
    if (length > 6 && n >= 3 && n + 4 < length &&
        word.charAt(n - 3) == 'b' &&
        word.charAt(n - 2) == 'u' &&
        word.charAt(n - 1) == 's' &&
        word.charAt(n + 1) == 'n' &&
        word.charAt(n + 2) == 'e' &&
        word.charAt(n + 3) == 's' &&
        word.charAt(n + 4) == 's') {
      syllableCount--;
      logger.info("Silent i in 'business'");
    }

    if (n + 1 < length && isVowel(word.charAt(n + 1)) && length >= 2) {
      pass(n);
    }
  }

  public void ocheck(int n) {
    logger.info("Detected an o");
    syllableCount++;

    if (n + 1 < length && isVowel(word.charAt(n + 1)) && length >= 2) {
      pass(n);
    }
  }

  public void ucheck(int n) {
    logger.info("Detected a u");
    syllableCount++;

    if (n + 1 < length && isVowel(word.charAt(n + 1)) && length >= 2) {
      pass(n);
    }
  }

  public void ycheck(int n) {
    logger.info("Detected a y");
    syllableCount++;

    if (n + 1 < length && isVowel(word.charAt(n + 1)) && length >= 2) {
      pass(n);
    }
  }

  public void defaultFunc(int n) {
    char c = word.charAt(n);

    // Symbols @, &
    if (c == '@' || c == '&') {
      logger.info("Detected @ or &");
      syllableCount++;
    }

    // Symbols $, %, #
    if (c == '$' || c == '%' || c == '#') {
      logger.info("Detected $ or % or #");
      syllableCount += 2;
    }

    // Silent 'e' in -ement suffixes (e.g., "gement", "cement")
    if (length > 6 && (c == 'g' || c == 'v' || c == 'r' || c == 'c' || c == 't' || c == 's') &&
        safe(n + 1) == 'e' &&
        safe(n + 2) == 'm' &&
        safe(n + 3) == 'e' &&
        safe(n + 4) == 'n' &&
        safe(n + 5) == 't') {
      logger.info("Silent e in ement");
      syllableCount--;
    }

    // Silent 'e' in prefix "some"
    if (length > 5 &&
        c == 's' &&
        safe(n + 1) == 'o' &&
        safe(n + 2) == 'm' &&
        safe(n + 3) == 'e') {
      logger.info("Silent e in prefix some");
      syllableCount--;
    }

    // "Mr" or "Dr" = 2 syllables
    if (length == 2 &&
        (c == 'm' || c == 'd') &&
        safe(n + 1) == 'r') {
      logger.info("Detected Mr or Dr");
      syllableCount = 2;
    }

    // "Mrs" = 2 syllables
    if (length == 3 &&
        c == 'm' &&
        safe(n + 1) == 'r' &&
        safe(n + 2) == 's') {
      logger.info("Detected Mrs");
      syllableCount = 2;
    }

    // "Ms" = 1 syllable
    if (length == 2 &&
        c == 'm' &&
        safe(n + 1) == 's') {
      logger.info("Detected Ms");
      syllableCount = 1;
    }

    // "Prof" = 3 syllables
    if (length == 4 &&
        n == 3 &&
        safe(n - 3) == 'p' &&
        safe(n - 2) == 'r' &&
        safe(n - 1) == 'o' &&
        c == 'f') {
      logger.info("Detected Prof");
      syllableCount = 3;
    }

    // Handle digits
    numberflag = false;
    switch (c) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '8':
      case '9':
        syllableCount++;
        numberflag = true;
        break;
      case '7':
        syllableCount += 2;
        numberflag = true;
        break;
      default:
        numberflag = false;
    }

    if (numberflag) {
      logger.info("Number detected");
    }
  }

  // Helper Methods
  private boolean isVowel(char c) {
    return "aeiouy".indexOf(c) != -1;
  }

  private void pass(int n) {
    logger.info("Approximation computed");
    // Add manual classification here when IO mode is implemented
  }

  public boolean isEndingflag() {
    return endingflag;
  }

  // Safe character access to avoid IndexOutOfBounds
  private char safe(int index) {
    if (index < 0 || index >= word.length()) return 0;
    return word.charAt(index);
  }
}
