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
		
		File file = TestResultLogger.createFile(null);
		List<TestResult> overallResults = new ArrayList<>();
		int i = 0;
		for(final String path: choices) {
			
			List<TestClass> testClassWithResults = runAllTests(path);
			List<TestResult> testResults = createResults(testClassWithResults);
			TestResultLogger.appendResults(file, extractGameName(path), testResults);
			overallResults.addAll(testResults);
			i++;
			if(i==50)
				break;
		}
		
		TestResultLogger.writeSummary(file, overallResults);
		
	}
	
	private static List<TestClass> runAllTests(String gameName) {
		
		//System.out.println("STARTING ALL TEST FOR: " + gameName);
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
	
	private static List<TestResult> createResults(List<TestClass> results){
		
		 List<TestResult> allResults = new ArrayList<>();
		    for (TestClass testClass : results) {
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
		    
		 return allResults;
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
