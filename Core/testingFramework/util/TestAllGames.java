package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import collector.TestCollector;
import controller.execution.TestLauncher;
import main.FileHandling;
import model.TestClass;
import model.TestMethod;
import model.TestParameter;
import util.TestResultLogger.TestResult;

public class TestAllGames {
	
	
	public static void main(String[] args) {

		// Load from memory
		final String[] choices = FileHandling.listGames();

		for (final String fileName : choices){
			
			if (fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/bad/"))
				continue;
					
			if (fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/wip/"))
				continue;
					
			if (fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/WishlistDLP/"))
				continue;

			if (fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/test/"))
				continue;

			if (fileName.replaceAll(Pattern.quote("\\"), "/").contains("/lud/reconstruction/"))
				continue;
					
		}
		
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
		File csvFile = new File("TestResults_" + timestamp + ".csv");

		// Create the file with header only once
		try (FileWriter writer = new FileWriter(csvFile)) {
		    writer.write("Game;Test Name;Duration (ms);Passed;Failure Message;Type\n");
		} catch (IOException e) {
		    System.err.println("Failed to initialize CSV file: " + e.getMessage());
		}
		
		for(final String game: choices) {
			List<TestClass> results = runAllTests(game);
			saveResults(csvFile, results, game);
		}
		
	}
	
	private static List<TestClass> runAllTests(String gameName) {
		
		System.out.println("STARTING ALL TEST FOR: " + gameName);
		List<TestClass> testClassList = TestCollector.collectTestClasses();
		
		for(TestClass testClass: testClassList) {
			for(TestMethod testMethod: testClass.getMethods()) {
				
				testMethod.setSelected(true); // to be run by the Launcher
				testMethod.setValue("gameName", gameName);
				
				for(TestParameter testParameter: testMethod.getParameters()) {
					if(testParameter.getValue() == null) {
						testParameter.setValue("0"); // handle cases where the user must provide inputs 
					}
				}
				
			}
		}
		
		TestLauncher launcher = new TestLauncher();
		launcher.run(testClassList);
		return testClassList;
	}
	
	public static void saveResults(File csvFile, List<TestClass> testClassList, String gameName) {
		
	    List<TestResult> allResults = new ArrayList<>();
	    for (TestClass testClass : testClassList) {
	        for (TestMethod method : testClass.getSelectedMethods()) {
	  
	            TestResult result = new TestResult(
	                method.getName(),
	                method.getDuration(),
	                method.isPassed(),
	                method.getFailureMessage(),
	                method.isStatic()
	            );
	            allResults.add(result);
	        }
	    }
	    String fileName = extractGameName(gameName) + "_test_results";
	    TestResultLogger.appendTestResultsToCSV(csvFile, fileName, allResults);

	}
	
	private static String extractGameName(String path) {
	    // Extract the filename (e.g., Adugo.lud)
	    String fileName = path.substring(path.lastIndexOf("/") + 1);
	    
	    // Remove the extension (e.g., .lud)
	    int dotIndex = fileName.lastIndexOf(".");
	    if (dotIndex != -1) {
	        return fileName.substring(0, dotIndex);
	    } else {
	        return fileName;
	    }
	}
	
}
