package util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestResultLogger {

    public static class TestResult {
        String name;
        String durationMillis;
        boolean passed;
        String failureMessage;
        boolean isStatic;

        public TestResult(String name, String durationMillis, boolean passed, String failureMessage, boolean isStatic) {
            this.name = name;
            this.durationMillis = durationMillis;
            this.passed = passed;
            this.failureMessage = failureMessage;
            this.isStatic = isStatic;
        }
    }

    public static void saveTestResultsToTimestampedFile(String baseName, List<TestResult> results) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String fileName = baseName + "_" + timestamp + ".txt";

        int passedCount = 0;
        long totalTime = 0;

        for (TestResult result : results) {
            if (result.passed) passedCount++;
            totalTime += parseDuration(result.durationMillis);
        }

        double successRate = results.isEmpty() ? 0 : 100.0 * passedCount / results.size();
        long meanTime = results.isEmpty() ? 0 : totalTime / results.size();

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("=== Summary ===\n");
            writer.write("- Total Tests: " + results.size() + "\n");
            writer.write("- Success Rate: " + String.format("%.2f", successRate) + " %\n");
            writer.write("- Mean Execution Time: " + meanTime + " ms\n\n");

            writer.write("=== Test Results ===\n\n");

            for (TestResult result : results) {
                writer.write("Test Name: " + result.name + "\n");
                writer.write("Duration: " + result.durationMillis + " ms\n");
                writer.write("Passed: " + result.passed + "\n");
                if (!result.passed && result.failureMessage != null && !result.failureMessage.isEmpty()) {
                    writer.write("Failure Message: " + result.failureMessage + "\n");
                }
                writer.write("Type: " + (result.isStatic ? "Static" : "Dynamic") + "\n");
                writer.write("\n");
            }

            System.out.println("Test results saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

  
    private static long parseDuration(String durationMillis) {
        try {
            return Long.parseLong(durationMillis);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
