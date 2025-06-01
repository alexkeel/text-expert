package io.github.alexkeel.textexpert.webapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
  private final List<String> defaultHardstops = List.of("!", "?", ":", ".");

  private final String content;
  private final boolean romanNumeralDetection;
  private final List<String> hardStops;
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

  Cruncher(final String content,
           final boolean romanNumeralDetection,
           final List<String> hardStops,
           final int outputPrecision,
           final boolean fullResultsEnabled) {
    this.content = content;
    this.romanNumeralDetection = romanNumeralDetection;
    this.hardStops = hardStops.isEmpty() ? defaultHardstops : hardStops;
    this.outputPrecision = outputPrecision;
    this.fullResultsEnabled = fullResultsEnabled;

    prepFiles();
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
