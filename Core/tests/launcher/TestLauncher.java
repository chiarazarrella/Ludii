package launcher;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class TestLauncher{
	Launcher launcher;
	
	
	public TestLauncher() {
		launcher = LauncherFactory.create();
	}
	
	// run will have as params a data structure containing info about tests and parameters (prob an hashmap)
	public void run(String game, Map<String, List<String>> tests) { // K -> name of the method V -> parameters
		
		List<String> paramTest1 = tests.get("lineLessOrEqualThanBoardSide");
		
		String lineString = paramTest1.get(0);
		//System.out.println("this is line " + lineString);
		int line = Integer.parseInt(lineString);
		
		List<Object> parameters = new ArrayList<>();
		parameters.add(game);
		parameters.add(line);
		
		UserInputTestProvider.setUserInputs(parameters);
		
		MethodSelector methodSelector = DiscoverySelectors.selectMethod("board.BoardTest#lineLessOrEqualThanBoardSide(java.lang.String, int)");
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
		            .selectors(methodSelector)
		            .configurationParameter(game, lineString)
		            .build();
		
		SummaryGeneratingListener listener = new SummaryGeneratingListener();
		
		//for(#test)  ---> loop to set the input of the test and to run 
			//setUserInputs
		launcher.execute(request, listener); 
        
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
        
	}
}
