package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
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
    
   
    
    // Utility to quote CSV values and handle nulls
    private static String escapeCsv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
    
    
    private static long parseDuration(String durationMillis) {
        try {
            return Long.parseLong(durationMillis);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
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
    	        // Read all lines
    	        List<String> lines = Files.readAllLines(file.toPath());

    	        // Prepare summary row
    	        String summary = String.join(";",
    	        		 "", "", "", "", "", "", "", "", "", // empty test-specific fields
    	                 String.valueOf(results.size()),
    	                 String.format("%.2f%%", successRate),
    	                 String.valueOf(meanTime)
    	        );

    	        // Ensure at least header exists
    	        if (lines.isEmpty()) {
    	            System.err.println("CSV file is empty. Aborting.");
    	            return;
    	        }

    	        // Insert or replace second line with summary
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
