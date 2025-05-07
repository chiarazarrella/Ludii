package controller.execution.parameter;

import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;

import model.TestMethod;
import model.TestParameter;

import java.security.KeyStore.Entry;
import java.util.*;
import java.util.stream.Stream;

public class ParametersContextProvider implements TestTemplateInvocationContextProvider {

  
    private static Map<TestMethod, List<TestParameter>> inputs = new HashMap<>();
    
    /**
     * @param inputs
     */
    public static void setUserInputs(Map<TestMethod, List<TestParameter>> inputs) {
    	ParametersContextProvider.inputs = inputs;
    }

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
        String testMethodName = context.getTestMethod().map(method -> method.getName()).orElse("");
        boolean hasInputs = false;
        
        for(java.util.Map.Entry<TestMethod, List<TestParameter>> entry: inputs.entrySet()) {
        	        
        	hasInputs = entry.getKey().getName().equals(testMethodName);
        	if(hasInputs) {
        		break;
        	}
        }
        
        
        System.out.println(">> supportsTestTemplate called for " + testMethodName + ", has inputs: " + hasInputs);
        return hasInputs; // Ensure inputs exist for this specific test
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
        String testMethodName = context.getTestMethod().map(method -> method.getName()).orElse("");

        System.out.println(">> provideTestTemplateInvocationContexts called for " + testMethodName);
                
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
