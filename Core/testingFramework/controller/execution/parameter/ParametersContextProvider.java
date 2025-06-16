package controller.execution.parameter;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import model.TestMethod;
import model.TestParameter;

import java.util.*;
import java.util.stream.Stream;

/**
 * A custom {@link TestTemplateInvocationContextProvider} for providing test template contexts based on
 * user-defined test inputs. This allows dynamic parameterized tests using {@link TestMethod} and {@link TestParameter}.
 * 
 * @author Chiara E. Zarrella
 */
public class ParametersContextProvider implements TestTemplateInvocationContextProvider {

    /** 
     * Static map containing input data for test methods.
     * Each {@link TestMethod} is mapped to a list of {@link TestParameter} to be injected during test execution.
     */
    private static Map<TestMethod, List<TestParameter>> inputs = new HashMap<>();

    /**
     * Sets the user-defined input parameters for test methods.
     *
     * @param inputs a map associating test methods with their respective list of parameters
     */
    public static void setUserInputs(Map<TestMethod, List<TestParameter>> inputs) {
        ParametersContextProvider.inputs = inputs;
    }

    /**
     * Checks whether the current test method has registered inputs and is eligible for template execution.
     *
     * @param context the current extension context
     * @return {@code true} if the method has associated parameters, {@code false} otherwise
     */
    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        String testMethodName = context.getTestMethod()
                                       .map(method -> method.getName())
                                       .orElse("");
        boolean hasInputs = false;

        for (Map.Entry<TestMethod, List<TestParameter>> entry : inputs.entrySet()) {
            hasInputs = entry.getKey().getName().equals(testMethodName);
            if (hasInputs) break;
        }

        return hasInputs;
    }

    /**
     * Provides the {@link TestTemplateInvocationContext} for test methods that have been matched with input parameters.
     * This context includes a {@link DynamicParameterResolver} to resolve the method arguments at runtime.
     *
     * @param context the current extension context
     * @return a stream containing a single invocation context for the test method
     */
    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        String testMethodName = context.getTestMethod()
                                       .map(method -> method.getName())
                                       .orElse("");

        List<TestParameter> matchedParamList = null;
        for (Map.Entry<TestMethod, List<TestParameter>> entry : inputs.entrySet()) {
            if (entry.getKey().getName().equals(testMethodName)) {
                matchedParamList = entry.getValue();
                break;
            }
        }

        final List<TestParameter> finalParamList = matchedParamList != null ? matchedParamList : new ArrayList<>();

        return Stream.of(new TestTemplateInvocationContext() {
            @Override
            public List<Extension> getAdditionalExtensions() {
                List<Extension> resolvers = new ArrayList<>();
                resolvers.add(new DynamicParameterResolver(finalParamList));
                return resolvers;
            }
        });
    }
}
