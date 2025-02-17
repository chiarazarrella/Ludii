package launcher;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
	public void run(String game, int line) {
		
		List<Object> parameters = new ArrayList<>();
		parameters.add(game);
		parameters.add(line);
		UserInputTestProvider.setUserInputs(parameters);
		
		MethodSelector methodSelector = DiscoverySelectors.selectMethod("board.BoardTest#lineLessOrEqualThanBoardSide(java.lang.String, int)");
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
		            .selectors(methodSelector)
		            .build();
		
		SummaryGeneratingListener listener = new SummaryGeneratingListener();
		
		//for(#test)  ---> loop to set the input of the test and to run 
			//setUserInputs
		launcher.execute(request, listener); 
        
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
        
	}
}
