package launcher;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import board.BoardTest;
import parameterResolver.UserInputTestProvider;
import util.Pair;
import util.TestClass;
import util.TestMethod;
import util.TestParameter;
import listener.CustomSummaryListener;

public class TestLauncher{
	Launcher launcher;
	
	
	
	public TestLauncher() {
		launcher = LauncherFactory.create();
	}
	
	
	// change parameters as String game, List<TestClasses>
	public void run(List<TestClass> testClasses) { // K -> name of the method V -> parameters
		
	    Map<String, List<Object>> testInputs = new HashMap<>(); // Store test-specific inputs
	    List<MethodSelector> selectorsList = new ArrayList<>();
		
		// TEST BOARD
		/*List<String> boardTest = tests.get("lineLessOrEqualThanBoardSide");
		if(boardTest != null) {
			ArrayList<Object> paramsboardTest1 = new ArrayList<>();
			paramsboardTest1.add(game);
			String lineString = boardTest.get(0);
			int line = Integer.parseInt(lineString);
			
			paramsboardTest1.add(line);
			//testInputs.put("lineLessOrEqualThanBoardSide", paramsboardTest1);
			//selectorsList.add(DiscoverySelectors.selectMethod("board.BoardTest#lineLessOrEqualThanBoardSide(java.lang.String, int)"));
		}
		
		
		// TEST PLAYER
		List<String> playerTest = tests.get("playerNotDeclared");
		if(playerTest != null) {
			ArrayList<Object> playerTestParam = new ArrayList<>();
			playerTestParam.add(game);
			//testInputs.put("playerNotDeclared", playerTestParam);
			//selectorsList.add(DiscoverySelectors.selectMethod("player.PlayerTest#playerNotDeclared(java.lang.String)"));
		}
		
		// TESTS PIECE
		
		// TEST PIECE pieceDeclaredAsEach
		List<String> pieceTest1 = tests.get("pieceDeclaredAsEach");
		if(pieceTest1 != null) {
			ArrayList<Object> pieceTest1Param = new ArrayList<>();
			pieceTest1Param.add(game);
			//testInputs.put("pieceDeclaredAsEach", pieceTest1Param);
			//selectorsList.add(DiscoverySelectors.selectMethod("piece.PieceTest#pieceDeclaredAsEach(java.lang.String)"));
		}
		
		// TEST PIECE pieceDeclaredAsShared
		List<String> pieceTest2 = tests.get("pieceDeclaredAsShared");
		if(pieceTest2 != null) {
			ArrayList<Object> pieceTest2Param = new ArrayList<>();
			pieceTest2Param.add(game);
			//testInputs.put("pieceDeclaredAsShared", pieceTest2Param);
			//selectorsList.add(DiscoverySelectors.selectMethod("piece.PieceTest#pieceDeclaredAsShared(java.lang.String)"));
		}
		
		// TEST PIECE pieceDeclaredAsNeutral
		List<String> pieceTest3 = tests.get("pieceDeclaredAsNeutral");
		if(pieceTest3 != null) {
			ArrayList<Object> pieceTest3Param = new ArrayList<>();
			pieceTest3Param.add(game);
			//testInputs.put("pieceDeclaredAsNeutral", pieceTest3Param);
			//selectorsList.add(DiscoverySelectors.selectMethod("piece.PieceTest#pieceDeclaredAsNeutral(java.lang.String)"));
		}*/
		
	    List<TestMethod> testMethods = new ArrayList<TestMethod>();
	    
		// with classes - REFACTOR
		for(TestClass testClass: testClasses) {
			
			for(TestMethod method: testClass.getMethods().values()) {
				
				if(method.isChecked()) {
					
					List<Object> paramValues = new ArrayList<>(method.getParameters().size());
					
					for(TestParameter param: method.getParameters().values()) {
						
						paramValues.add(param.getValue());
						
						testInputs.put(method.getName(), paramValues);
						
					}
					
					System.out.println("this is the method: " + testClass.getFullyQualifiedNameForMethod(method.getId()));
					selectorsList.add(DiscoverySelectors.selectMethod(testClass.getFullyQualifiedNameForMethod(method.getId())));
					testMethods.add(method);
				}
				
			}
			
		}
		
		
		// LAUNCH TESTS
		// K: name of method - Pair<Duration, Reason (if failed)>
		Map<String, Pair<String, String>> results = new HashMap<>();
		
		UserInputTestProvider.setUserInputs(testInputs);
		
		MethodSelector[] selectors = selectorsList.toArray(new MethodSelector[0]);
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
					.request()
		            .selectors(selectors)
		            .build();
		
		
		
		List<String> passedTests = new ArrayList<String>();
		List<String> failedTests = new ArrayList<String>();
		
		CustomSummaryListener listener = new CustomSummaryListener();
		launcher.execute(request, listener);

		TestExecutionSummary summary = listener.getSummary();
		//summary.printTo(new PrintWriter(System.out, true)); 

		Map<String, Long> testDurations = listener.getTestDurations();
		Map<String, String> failureMessages = listener.getFailureMessages();

		System.out.println("\n--- Test Results ---");

		// FAILED TESTS
		for (TestExecutionSummary.Failure failure : summary.getFailures()) {
		    String methodName = CustomSummaryListener.extractTestMethodName(failure.getTestIdentifier().getUniqueIdObject());
		    Long duration = testDurations.getOrDefault(methodName, 0L);
		    String failureMessage = failureMessages.getOrDefault(methodName, "Unknown failure");
		    
		    for(TestMethod method: testMethods) {
		    	
		    	if(method.getName().equals(methodName)) {
		    		method.setDuration(duration.toString());
		    		method.setFailureMessage(failureMessage);
		    		
		    	}
		    }
		    		    
		    
		    System.out.println("❌ Failed: " + methodName + " (Time: " + duration + "ms)");
		    System.out.println("   Reason: " + failureMessage);
		    
		    results.put(methodName, new Pair<String, String>(duration.toString(), failureMessage));
		    
		}

		// PASSED TESTS
		for(TestMethod method: testMethods) {
			String name = method.getName();
		    Long duration = testDurations.getOrDefault(name, 0L);
		    
		    if(method.getFailureMessage() != null) continue; // failed method 
		    
	    	if(method.getName().equals(name)) {
	    		method.setDuration(duration.toString());
	    		method.setPassed(true);
	    	}
	    }
		
		
	}
	

	
}
