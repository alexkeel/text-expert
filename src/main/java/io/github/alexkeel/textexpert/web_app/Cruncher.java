package io.github.alexkeel.textexpert.web_app;

import java.util.List;

public class Cruncher
{
    final String content;
    final boolean romanNumeralDetection;
    final List<String> hardStops;
    final int outputPrecision;
    final boolean fullResultsEnabled;

    Cruncher(final String content,
             final boolean romanNumeralDetection,
             final List<String> hardStops,
             final int outputPrecision,
             final boolean fullResultsEnabled) {
        this.content = content;
        this.romanNumeralDetection = romanNumeralDetection;
        this.hardStops = hardStops;
        this.outputPrecision = outputPrecision;
        this.fullResultsEnabled = fullResultsEnabled;
    }
}
