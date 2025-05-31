package io.github.alexkeel.textexpert.web_app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Controller
public class UploadController {

    @GetMapping("/")
    public String home() {
        return "upload";
    }

    @PostMapping("/process")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(defaultValue = "false") boolean romanNumeralDetection,
                                   @RequestParam(required = true) String hardStops,
                                   @RequestParam(required = true) int outputPrecision,
                                   @RequestParam(defaultValue = "false") boolean fullResults,
                                   Model model) {
        try {
            final String content = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

            // Process the text and options here
            model.addAttribute("fileText", content);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to process file");
        }

        return "result";
    }
}