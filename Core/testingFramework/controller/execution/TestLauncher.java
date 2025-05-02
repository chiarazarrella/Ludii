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
import controller.execution.parameter.InputTestProvider;
import model.TestClass;
import model.TestMethod;
import model.TestParameter;

public class TestLauncher {

    private final Launcher launcher;

    public TestLauncher() {
        this.launcher = LauncherFactory.create();
    }

    public void run(List<TestClass> testClasses) {
        Map<String, List<Object>> testInputs = new HashMap<>();
        List<MethodSelector> selectorsList = new ArrayList<>();
        List<TestMethod> selectedMethods = new ArrayList<>();

        collectTestMethods(testClasses, testInputs, selectorsList, selectedMethods);

        InputTestProvider.setUserInputs(testInputs);

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectorsList)
                .build();

        TestSummaryListener listener = new TestSummaryListener();
        launcher.execute(request, listener);

        updateTestResults(selectedMethods, listener);
    }

    private void collectTestMethods(
            List<TestClass> testClasses,
            Map<String, List<Object>> testInputs,
            List<MethodSelector> selectors,
            List<TestMethod> selectedMethods
    ) {
        for (TestClass testClass : testClasses) {
            for (TestMethod method : testClass.getMethods().values()) {
                if (!method.isSelected()) continue;

                List<Object> paramValues = parseMethodParameters(method);
                testInputs.put(method.getName(), paramValues);

                int methodId = TestMethod.getId(method.getName());
                String fqMethodName = testClass.getFullyQualifiedNameForMethod(methodId);
                selectors.add(DiscoverySelectors.selectMethod(fqMethodName));

                selectedMethods.add(method);
            }
        }
    }

    private List<Object> parseMethodParameters(TestMethod method) {
        List<Object> paramValues = new ArrayList<>();

        for (TestParameter param : method.getParameters().values()) {
            Object parsedValue = parseValue(param.getValue(), param.getType());
            paramValues.add(parsedValue);
        }

        return paramValues;
    }

    private Object parseValue(String value, Class<?> type) {
        // TODO: make this more generic if needed
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        // Extend this for more types (e.g., boolean, double, etc.)
        throw new IllegalArgumentException("Unsupported parameter type: " + type);
    }

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

    private void updateFailure(List<TestMethod> methods, String methodName, Map<String, Long> durations, Map<String, String> failures) {
        for (TestMethod method : methods) {
            if (!method.getName().equals(methodName)) continue;

            method.setDuration(durations.getOrDefault(methodName, 0L).toString());
            method.setFailureMessage(failures.getOrDefault(methodName, "Unknown failure"));
            break;
        }
    }
}
