package launcher;

import java.io.PrintWriter;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import board.BoardTest;

public class TestLauncher{
	Launcher launcher;
	
	
	public TestLauncher() {
		launcher = LauncherFactory.create();
	}
	
	
	public void run(String game, int line) {
		
		 
		 MethodSelector methodSelector = DiscoverySelectors.selectMethod("board.BoardTest#lineLessOrEqualThanBoardSide(java.lang.String, int)");
		 LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
		            .selectors(methodSelector)
		            .build();
		 
		//BoardTest.parametersProvider(game, line);

		SummaryGeneratingListener listener = new SummaryGeneratingListener();
		
		long startTime = System.nanoTime();
        launcher.execute(request, listener);
        long endTime = System.nanoTime();
        
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));
        
        
	}
}
