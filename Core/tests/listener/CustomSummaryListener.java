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

public class CustomSummaryListener extends SummaryGeneratingListener {
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

        if (startTime != null) {
            long duration = endTime - startTime; // Calculate duration in milliseconds
            testDurations.put(extractTestMethodName(testIdentifier.getUniqueIdObject()), duration);
        }
        
        if (testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
            Optional<Throwable> throwable = testExecutionResult.getThrowable();
            System.out.println(testIdentifier.getUniqueId());
            throwable.ifPresent(ex -> failureMessages.put(extractTestMethodName(testIdentifier.getUniqueIdObject()), ex.getMessage()));
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
    
   

