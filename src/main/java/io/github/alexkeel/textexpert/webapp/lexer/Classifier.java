package io.github.alexkeel.textexpert.webapp.lexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;

public abstract class Classifier
{
  protected int syllableCount;

  protected abstract void load() throws IOException;

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
