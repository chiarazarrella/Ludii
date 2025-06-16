package controller.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.MethodSelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import controller.execution.listener.TestSummaryListener;
import controller.execution.parameter.ParametersContextProvider;
import model.TestClass;
import model.TestMethod;
import model.TestParameter;

/**
 * Handles the execution of selected JUnit test methods.
 * <p>
 * This class is responsible for:
 * <ul>
 *   <li>Building a discovery request from selected test methods</li>
 *   <li>Providing parameter values to parameterized tests</li>
 *   <li>Executing tests via JUnit Platform Launcher</li>
 *   <li>Capturing and updating test execution results (pass/fail, duration, message)</li>
 * </ul>
 * 
 * @author Chiara E. Zarrella
 */
public class TestLauncher {

	 /** The JUnit Platform launcher used for executing tests. */
    private final Launcher launcher;

    /**
     * Constructs a new TestLauncher using the default Launcher implementation.
     */
    public TestLauncher() {
        this.launcher = LauncherFactory.create();
    }

    /**
     * Executes the test methods selected in the provided list of {@link TestClass} instances.
     * 
     * @param testClasses the list of test classes whose selected methods will be executed
     */
    public void run(List<TestClass> testClasses) {
        List<MethodSelector> selectorsList = new ArrayList<>();
        List<TestMethod> selectedMethods = new ArrayList<>();
        Map<TestMethod, List<TestParameter>> inputs = new HashMap<>();
        

        collectTestMethods(testClasses, inputs, selectorsList, selectedMethods);

        // Provide user-input parameter values
        ParametersContextProvider.setUserInputs(inputs);

        // Build discovery request from selected methods
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectorsList)
                .build();

        // Execute and record results
        TestSummaryListener listener = new TestSummaryListener();
        launcher.execute(request, listener);

        // Update model with test results
        updateTestResults(selectedMethods, listener);
    }

    /**
     * Collects all selected test methods from the provided test classes and prepares
     * the necessary data structures for test execution.
     *
     * @param testClasses      the list of test classes
     * @param testInputs       output map of test methods to their parameter values
     * @param selectors        output list of method selectors
     * @param selectedMethods  output list of selected test methods
     */
    private void collectTestMethods(
            List<TestClass> testClasses,
            Map<TestMethod, List<TestParameter>> testInputs,
            List<MethodSelector> selectors,
            List<TestMethod> selectedMethods
    ) {
        for (TestClass testClass : testClasses) {
            for (TestMethod method : testClass.getMethods()) {
                if (!method.isSelected()) continue;

                
                testInputs.put(method, method.getParameters());
                int methodId = method.getId();
                String fqMethodName = testClass.getFullyQualifiedNameForMethod(methodId);
                selectors.add(DiscoverySelectors.selectMethod(fqMethodName));

                selectedMethods.add(method);
            }
        }
    }

    /**
     * Updates the test result fields (duration, status, failure message) of each executed method
     * based on the information gathered by the {@link TestSummaryListener}.
     *
     * @param methods  the list of executed test methods
     * @param listener the listener that recorded test execution data
     */
    private void updateTestResults(List<TestMethod> methods, TestSummaryListener listener) {
        Map<String, Long> durations = listener.getTestDurations();
        Map<String, String> failures = listener.getFailureMessages();

        // First, mark failures
        for (TestExecutionSummary.Failure failure : listener.getSummary().getFailures()) {
            String methodName = TestSummaryListener.extractTestMethodName(failure.getTestIdentifier().getUniqueIdObject());
            updateFailure(methods, methodName, durations, failures);
        }

        // Then, mark successes
        for (TestMethod method : methods) {
            if (method.getFailureMessage() != null) continue;

            String name = method.getName();
            method.setDuration(durations.getOrDefault(name, 0L).toString());
            method.setPassed(true);
        }
    }

    /**
     * Marks a test method as failed, setting the duration and failure message.
     *
     * @param methods     the list of test methods
     * @param methodName  the name of the failed method
     * @param durations   a map of method names to their execution durations
     * @param failures    a map of method names to their failure messages
     */
    private void updateFailure(List<TestMethod> methods, String methodName, Map<String, Long> durations, Map<String, String> failures) {
        for (TestMethod method : methods) {
            if (!method.getName().equals(methodName)) continue;

            method.setDuration(durations.getOrDefault(methodName, 0L).toString());
            method.setFailureMessage(failures.getOrDefault(methodName, "Unknown failure"));
            break;
        }
    }
}
