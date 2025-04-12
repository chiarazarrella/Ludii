package listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.UniqueId.Segment;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public class TestSummaryListener extends SummaryGeneratingListener {
    private final Map<String, Long> testStartTimes = new ConcurrentHashMap<>();
    private final Map<String, Long> testDurations = new HashMap<>();
    private final Map<String, String> failureMessages = new HashMap<>();

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        testStartTimes.put(testIdentifier.getUniqueId(), System.currentTimeMillis());
        super.executionStarted(testIdentifier);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        long endTime = System.currentTimeMillis();
        Long startTime = testStartTimes.get(testIdentifier.getUniqueId());
        String nameMethod = extractTestMethodName(testIdentifier.getUniqueIdObject());
        
        // I check on the nameMethod bc this method executionFinished is executed for every segment of the UNIQUE ID
        if (startTime != null && nameMethod != null) {
            long duration = endTime - startTime; // Calculate duration in milliseconds
            testDurations.put(nameMethod, duration);
        }
        
        
        /*System.out.println("UNIQUE ID");
        System.out.println(testIdentifier.getUniqueId());
        
        for(Segment segment: testIdentifier.getUniqueIdObject().getSegments()) {
        	System.out.println("####");
        	System.out.println(segment.toString());
        }*/
        
        if (nameMethod != null && testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
            Optional<Throwable> throwable = testExecutionResult.getThrowable();
            throwable.ifPresent(ex -> failureMessages.put(nameMethod, ex.getMessage()));
        }

        super.executionFinished(testIdentifier, testExecutionResult);
    }

    public Map<String, Long> getTestDurations() {
        return testDurations;
    }

    public Map<String, String> getFailureMessages() {
        return failureMessages;
    }

    
    public static String extractTestMethodName(UniqueId uniqueId) {
    	String methodName = null;
    	
    	for(Segment s: uniqueId.getSegments()) {
            if(s.getType() == "test-template") {
            	int initParameters = s.getValue().indexOf("(");
            	methodName = s.getValue().substring(0, initParameters);
            }
        }
    	
        return methodName;
    }
    
  
}
    
   

