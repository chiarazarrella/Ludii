package parameterResolver;

import org.junit.jupiter.api.extension.*;

import java.util.List;

public class DynamicParameterResolver implements ParameterResolver {

    private final List<Object> parameters; // Parameters specific to one test execution

    public DynamicParameterResolver(List<Object> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return true; // Supports all parameters
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        int index = parameterContext.getIndex(); // Get parameter index
        
        if (index >= parameters.size()) {
            throw new IllegalArgumentException("No parameter available for index " + index);
        }

        return parameters.get(index); // Return the correct parameter for this test method
    }
}


