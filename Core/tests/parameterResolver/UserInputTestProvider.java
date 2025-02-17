package parameterResolver;

import org.junit.jupiter.api.extension.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UserInputTestProvider implements TestTemplateInvocationContextProvider {

    private static List<Object> userInputs; // Store the list of (gameName, lineLength) pairs

    // Method to set user inputs before running tests
    public static void setUserInputs(List<Object> inputs) {
        userInputs = inputs;
    }

    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {
    	 System.out.println(">> supportsTestTemplate called, userInputs size: " + userInputs.size());
         return !userInputs.isEmpty(); // Ensure inputs exist    
    }

       
    @Override
     public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {
          System.out.println(">> provideTestTemplateInvocationContexts called");

          return Stream.of(new TestTemplateInvocationContext() {
                 @Override
                 public List<Extension> getAdditionalExtensions() {
                	 List<Extension> resolvers = new ArrayList<>();
                	 resolvers.add(new DynamicParameterResolver(userInputs));
                     return resolvers;
                 }
             });
       }

   
}
