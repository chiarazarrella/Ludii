package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Utility class for logging test results into a CSV file.
 * Supports recording individual test entries and writing a summary.
 * 
 * @author Chiara E. Zarrella
 */
public class TestResultLogger {
	
	/**
     * Represents the result of a single test execution.
     */
    public static class TestResult {
        String name;
        String durationMillis;
        boolean passed;
        String failureMessage;
        boolean isStatic;
        
        /**
         * Constructs a new {@code TestResult}.
         *
         * @param name           Name of the test.
         * @param durationMillis Duration of the test in milliseconds.
         * @param passed         Whether the test passed.
         * @param failureMessage Message describing the failure, if any.
         * @param isStatic       {@code true} if the test is static; {@code false} if dynamic.
         */
        public TestResult(String name, String durationMillis, boolean passed, String failureMessage, boolean isStatic) {
            this.name = name;
            this.durationMillis = durationMillis;
            this.passed = passed;
            this.failureMessage = failureMessage;
            this.isStatic = isStatic;
        }
    }
    
    /**
     * Escapes a string for safe inclusion in a CSV file.
     *
     * @param value The string to escape.
     * @return The escaped string, or an empty string if the input is {@code null}.
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    
    /**
     * Parses a duration string in milliseconds to a long.
     *
     * @param durationMillis The duration string.
     * @return The duration in milliseconds, or 0 if parsing fails.
     */
    private static long parseDuration(String durationMillis) {
        try {
            return Long.parseLong(durationMillis);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    /**
     * Creates a CSV file for storing test results. The file is initialized with a header row
     * and a placeholder for summary information.
     *
     * @param gameName The name of the game being tested (used in the file name). If {@code null}, it means that the file will involve more than a game.
     * @return The created file object.
     */
    public static File createFile(String gameName) {
    	
    	String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    	File file;
    	if(gameName == null)
    		file = new File("Test_results_" + timestamp + ".csv");
    	else
    		file = new File(gameName.replace(".lud", "_") + timestamp + ".csv");
    	
    	try (FileWriter writer = new FileWriter(file, false)) {
            // Header
            writer.write("Game;Test Name;Duration (ms);Passed;Failure Message;Type;;;;Total Tests;Success Rate;Mean Execution Time\n");
            writer.write("\n");
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    	
    	return file;
    }
    
    /**
     * Writes a summary line in the second row of the CSV file, containing:
     * total number of tests, success rate, and mean execution time.
     *
     * @param file    The file to update.
     * @param results The list of test results to summarize.
     */
    public static void writeSummary(File file, List<TestResult> results) {
    	
    	 	int passedCount = 0;
    	    long totalTime = 0;

    	    for (TestResult result : results) {
    	        if (result.passed) passedCount++;
    	        totalTime += parseDuration(result.durationMillis);
    	    }

    	    double successRate = results.isEmpty() ? 0 : 100.0 * passedCount / results.size();
    	    long meanTime = results.isEmpty() ? 0 : totalTime / results.size();

    	    try {
    	    	
    	        List<String> lines = Files.readAllLines(file.toPath());

    	        String summary = String.join(";",
    	        		 "", "", "", "", "", "", "", "", "", // empty test-specific fields
    	                 String.valueOf(results.size()),
    	                 String.format("%.2f%%", successRate),
    	                 String.valueOf(meanTime)
    	        );

    	        
    	        if (lines.isEmpty()) {
    	            System.err.println("CSV file is empty. Aborting.");
    	            return;
    	        }

    	        if (lines.size() >= 2) {
    	            lines.set(1, summary); // overwrite existing second line
    	        } else {
    	            lines.add(summary); // append if no second line
    	        }

    	        // Write all lines back
    	        Files.write(file.toPath(), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

    	    } catch (IOException e) {
    	        System.err.println("Error updating summary row: " + e.getMessage());
    	    }
    }
    
    /**
     * Appends a list of test results to the CSV file, each as a new row.
     *
     * @param file     The file to append to.
     * @param gameName The name of the game tested.
     * @param results  The test results to append.
     */
    public static void appendResults(File file, String gameName, List<TestResult> results) {
    	
        try (FileWriter writer = new FileWriter(file, true)) {

            // Individual test results
            for (TestResult result : results) {
                writer.write(String.join(";",
                    escapeCsv(gameName),
                    escapeCsv(result.name),
                    String.valueOf(result.durationMillis),
                    String.valueOf(result.passed),
                    escapeCsv(result.failureMessage),
                    result.isStatic ? "Static" : "Dynamic",
                    "", "", "", // empty summary fields
                    "", "", ""  // again leave summary fields empty for test lines
                ));
                writer.write("\n");
            }

        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }


}
