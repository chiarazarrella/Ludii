package parameterResolver;

import java.util.List;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;

public class DynamicParameterResolver implements ParameterResolver {
	
	List<Object> parameters;
	
    public DynamicParameterResolver(List<Object> parameters) {
    	this.parameters = parameters;
	}

	@Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return true;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    	
    	int index = parameterContext.getIndex(); // Get parameter index
        if (index >= parameters.size()) {
            throw new IllegalArgumentException("No parameter available for index " + index);
        }

        return parameters.get(index); // Return correct parameter
    }
	
}

