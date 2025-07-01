package io.github.alexkeel.textexpert.webapp.lexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public abstract class Classifier
{
  private static final Logger logger = LoggerFactory.getLogger(Classifier.class);

  protected int syllableCount;
  protected final String tokenListLocation;
  protected Scanner validTokens;

  Classifier(final String tokenListLocation){
    this.tokenListLocation = tokenListLocation;
    try {
      loadFromFile();
    } catch (IOException exception) {
      logger.error("Error reading from file: {}", exception.getMessage());
    }
  }

  Classifier() {
    tokenListLocation = "";
  }

  protected void loadFromFile() throws IOException {
    validTokens = new Scanner(new File(readFile(tokenListLocation)), StandardCharsets.UTF_8);
    validTokens.useDelimiter("\\s+");
  }

  public abstract boolean check(final String word);

  protected String readFile(final String filename) throws IOException {
    final ClassPathResource resource = new ClassPathResource("data/" + filename);
    try (final BufferedReader reader = new BufferedReader(
        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }

  public int getSyllableCount() {
    return syllableCount;
  }
}
