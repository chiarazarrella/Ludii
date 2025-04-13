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
import listener.TestSummaryListener;
import model.TestClass;
import model.TestMethod;
import model.TestParameter;

public class TestLauncher{
	Launcher launcher;
	
	
	
	public TestLauncher() {
		launcher = LauncherFactory.create();
	}
	
	
	public void run(List<TestClass> testClasses) { 
		
	    Map<String, List<Object>> testInputs = new HashMap<>(); // Store test-specific inputs
	    List<MethodSelector> selectorsList = new ArrayList<>();
		
		
	    List<TestMethod> testMethods = new ArrayList<TestMethod>();
	    
		for(TestClass testClass: testClasses) {
			
			for(TestMethod method: testClass.getMethods().values()) {
				
				if(method.isChecked()) {
					
					List<Object> paramValues = new ArrayList<>(method.getParameters().size());
					
					for(TestParameter param: method.getParameters().values()) {
						
						// parse the parameter
						if(param.getType() != String.class) {
							
							paramValues.add(Integer.parseInt(param.getValue())); // TODO: make this generic
						}else {
							paramValues.add(param.getValue());
						}
						
						testInputs.put(method.getName(), paramValues);
						
					}
					
					selectorsList.add(DiscoverySelectors.selectMethod(testClass.getFullyQualifiedNameForMethod(TestMethod.getId(method.getName()))));
					testMethods.add(method);
				}
				
			}
			
		}
		
		
		
		
		UserInputTestProvider.setUserInputs(testInputs);
		
		MethodSelector[] selectors = selectorsList.toArray(new MethodSelector[0]);
		LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
					.request()
		            .selectors(selectors)
		            .build();
		
		
		TestSummaryListener listener = new TestSummaryListener();
		launcher.execute(request, listener);

		TestExecutionSummary summary = listener.getSummary();

		Map<String, Long> testDurations = listener.getTestDurations();
		Map<String, String> failureMessages = listener.getFailureMessages();


		// FAILED TESTS
		for (TestExecutionSummary.Failure failure : summary.getFailures()) {
		    String methodName = TestSummaryListener.extractTestMethodName(failure.getTestIdentifier().getUniqueIdObject());
		    Long duration = testDurations.getOrDefault(methodName, 0L);
		    String failureMessage = failureMessages.getOrDefault(methodName, "Unknown failure");
		    
		    for(TestMethod method: testMethods) {
		    	
		    	if(method.getName().equals(methodName)) {
		    		method.setDuration(duration.toString());
		    		method.setFailureMessage(failureMessage);
		    		
		    	}
		    }
		    		    
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
