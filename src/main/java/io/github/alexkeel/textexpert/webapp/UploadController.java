package io.github.alexkeel.textexpert.webapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles web requests.
 */
@Controller
public class UploadController {

  @GetMapping("/")
  public String home() {
    return "upload";
  }

  /**
  * Handles a text processing request.
  *
  * @param file The text file to parse and analyse
  * @param romanNumeralDetection Flag to determine if to use Roman numeral detection
  * @param hardStops List of comma separated values as a string
  * @param outputPrecision Output precision as decimal point
  * @param fullResults Flag to determine if to produce full results
  * @param model Spring model
  * @return Results request
  */
  @PostMapping("/process")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
                               @RequestParam(defaultValue = "false") boolean romanNumeralDetection,
                               @RequestParam(required = true) String hardStops,
                               @RequestParam(required = true) int outputPrecision,
                               @RequestParam(defaultValue = "false") boolean fullResults,
                               Model model) {
    try {
      String content;
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
        content = reader.lines().collect(Collectors.joining("\n"));
      }

      // Process the text and options here
      model.addAttribute("fileText", content);
    } catch (final Exception ex) {
      model.addAttribute("error", "Failed to process file: " + ex.getMessage());
    }

    return "result";
  }
}