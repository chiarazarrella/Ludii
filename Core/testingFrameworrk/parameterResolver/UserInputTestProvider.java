package parameterResolver;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import java.util.*;
import java.util.stream.Stream;

public class UserInputTestProvider implements TestTemplateInvocationContextProvider {

    private static Map<String, List<Object>> testInputs = new HashMap<>(); // Store test-specific inputs

    // Method to set user inputs before running tests
    /**
     * @param inputs
     */
    public static void setUserInputs(Map<String, List<Object>> inputs) {
        testInputs = inputs;
    }

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        String testMethodName = context.getTestMethod().map(method -> method.getName()).orElse("");
        boolean hasInputs = testInputs.containsKey(testMethodName);

        //System.out.println(">> supportsTestTemplate called for " + testMethodName + ", has inputs: " + hasInputs);
        return hasInputs; // Ensure inputs exist for this specific test
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        String testMethodName = context.getTestMethod().map(method -> method.getName()).orElse("");

        //System.out.println(">> provideTestTemplateInvocationContexts called for " + testMethodName);
        
        if(testInputs.get(testMethodName).isEmpty()) System.out.println("this is empty");
        
        
        List<Object> parameters = testInputs.getOrDefault(testMethodName, Collections.emptyList()); 

        return Stream.of(new TestTemplateInvocationContext() {
            @Override
            public List<Extension> getAdditionalExtensions() {
           	 List<Extension> resolvers = new ArrayList<>();
           	 resolvers.add(new DynamicParameterResolver(parameters));
                return resolvers;
            }
        });
    }
}
