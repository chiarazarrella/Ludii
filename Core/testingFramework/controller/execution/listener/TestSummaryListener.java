package controller.execution.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.UniqueId.Segment;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

/**
 * A custom listener extending JUnit's SummaryGeneratingListener
 * to track execution time and failure messages for test methods.
 * 
 * @author Chiara E. Zarrella
 */
public class TestSummaryListener extends SummaryGeneratingListener {
    private final Map<String, Long> testStartTimes = new ConcurrentHashMap<>();
    private final Map<String, Long> testDurations = new HashMap<>();
    private final Map<String, String> failureMessages = new HashMap<>();

    /**
     * Records the start time of a test when execution begins.
     *
     * @param testIdentifier The identifier of the test.
     */
    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        testStartTimes.put(testIdentifier.getUniqueId(), System.currentTimeMillis());
        super.executionStarted(testIdentifier);
    }

    /**
     * Computes test duration and stores failure messages if the test fails.
     *
     * @param testIdentifier      The identifier of the test.
     * @param testExecutionResult The result of the test execution.
     */
    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        long endTime = System.currentTimeMillis();
        Long startTime = testStartTimes.get(testIdentifier.getUniqueId());
        String nameMethod = extractTestMethodName(testIdentifier.getUniqueIdObject());

        // Only register duration for method-level identifiers on the UNIQUE ID
        if (startTime != null && nameMethod != null) {
            long duration = endTime - startTime;
            testDurations.put(nameMethod, duration);
        }

        if (nameMethod != null && testExecutionResult.getStatus() == TestExecutionResult.Status.FAILED) {
            Optional<Throwable> throwable = testExecutionResult.getThrowable();
            throwable.ifPresent(ex -> failureMessages.put(nameMethod, ex.getMessage()));
        }

        super.executionFinished(testIdentifier, testExecutionResult);
    }

    /**
     * @return A map containing the duration of each test method.
     */
    public Map<String, Long> getTestDurations() {
        return testDurations;
    }

    /**
     * @return A map containing failure messages of failed test methods.
     */
    public Map<String, String> getFailureMessages() {
        return failureMessages;
    }

    /**
     * Extracts the test method name from the unique ID.
     * Handles test templates (the only type of tests in this framework)
     *
     * @param uniqueId The UniqueId object from the test identifier.
     * @return The name of the test method, or null if not found.
     */
    public static String extractTestMethodName(UniqueId uniqueId) {
        String methodName = null;

        for (Segment s : uniqueId.getSegments()) {
            if (s.getType().equals("test-template")) {
                int initParameters = s.getValue().indexOf("(");
                methodName = s.getValue().substring(0, initParameters);
            }
        }

        return methodName;
    }
}
    
   

