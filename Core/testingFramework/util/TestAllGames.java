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

		final String[] allGames = FileHandling.listGames();

		final List<String> validGames = new ArrayList<>();

		for (final String fileName : allGames) {
			fileName.replaceAll(Pattern.quote("\\"), "/");
			
			
		    if (
		    	/*fileName.contains("/lud/subgame/") ||
			   	fileName.contains("/lud/board/space/") ||
			    fileName.contains("/lud/board/war/") ||
			   	fileName.contains("/lud/board/hunt/") ||
			   	fileName.contains("/lud/board/race/") ||
			   	fileName.contains("/lud/board/sow/") ||
			   	fileName.contains("/lud/dominoes/") ||
			   	fileName.contains("/lud/puzzle/") ||
		    	fileName.contains("/lud/math/") ||*/
			   	fileName.contains("/lud/bad/") ||
		    	fileName.contains("/lud/wip/") ||
		    	fileName.contains("/lud/WishlistDLP/") ||
		   		fileName.contains("/lud/wishlist/") ||
		   		fileName.contains("/lud/test/") ||
		    	fileName.contains("/lud/experimental/") ||
		    	fileName.contains("/lud/reconstruction/") ||
		    	fileName.contains("/lud/simulation/")
		    	)
		    {
		        continue;
		    }

		    validGames.add(fileName);
		}

		final String[] filteredChoices = validGames.toArray(new String[0]);

		
		File file = TestResultLogger.createFile(null);
		List<TestResult> overallResults = new ArrayList<>();
		for(final String path: filteredChoices) {
			
			List<TestClass> testClassWithResults = runAllTests(path);
			List<TestResult> testResults = createResults(testClassWithResults);
			TestResultLogger.appendResults(file, extractGameName(path), testResults);
			overallResults.addAll(testResults);
		
		}
		
		TestResultLogger.writeSummary(file, overallResults);
		
	}
	
	private static List<TestClass> runAllTests(String gameName) {
		
		List<TestClass> testClassList = TestCollector.collectTestClasses();
		
		for(TestClass testClass: testClassList) {
			for(TestMethod testMethod: testClass.getMethods()) {
				
				testMethod.setSelected(true); // necessary to be ran by the Launcher
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
