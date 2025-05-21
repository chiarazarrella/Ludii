package util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestResultLogger {

    public enum Section {
        STATIC,
        DYNAMIC
    }

    public static class TestResult {
        String name;
        long durationMillis;
        boolean passed;
        String failureMessage;
        Section section;

        public TestResult(String name, long durationMillis, boolean passed, String failureMessage, Section section) {
            this.name = name;
            this.durationMillis = durationMillis;
            this.passed = passed;
            this.failureMessage = failureMessage;
            this.section = section;
        }
    }

    public static void saveTestResultsToTimestampedFile(String baseName, List<TestResult> results) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = baseName + "_" + timestamp + ".txt";

        Map<Section, List<TestResult>> grouped = new EnumMap<>(Section.class);
        for (TestResult result : results) {
            grouped.computeIfAbsent(result.section, k -> new ArrayList<>()).add(result);
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            for (Section section : Section.values()) {
                List<TestResult> sectionResults = grouped.get(section);
                if (sectionResults == null || sectionResults.isEmpty()) continue;

                writer.write("=== " + capitalize(section.name()) + " Tests ===\n\n");

                for (TestResult result : sectionResults) {
                    writer.write("Test Name: " + result.name + "\n");
                    writer.write("Duration: " + result.durationMillis + " ms\n");
                    writer.write("Passed: " + result.passed + "\n");
                    if (!result.passed && result.failureMessage != null && !result.failureMessage.isEmpty()) {
                        writer.write("Failure Message: " + result.failureMessage + "\n");
                    }
                    writer.write("\n");
                }
            }
            System.out.println("Test results saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    private static String capitalize(String s) {
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

 
}
