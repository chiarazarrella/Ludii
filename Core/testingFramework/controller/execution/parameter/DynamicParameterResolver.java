package controller.execution.parameter;

import org.junit.jupiter.api.extension.*;

import model.TestParameter;

import java.util.List;

public class DynamicParameterResolver implements ParameterResolver {

    private final List<TestParameter> listParam;
    
    public DynamicParameterResolver(List<TestParameter> listParam) {
        this.listParam = listParam;
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return true; // Supports all parameters
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    	
        String paramName = parameterContext.getParameter().getName();
        Object value = null;
        
        for(TestParameter p: listParam) {
        	if(p.getName().equals(paramName)) {
        		value = parseValue(p.getValue(), p.getType());
        		break;
        	}
        }
        
        //System.out.println(">> Value for param " + paramName + " with value " + value);

        
        return value;
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
}


