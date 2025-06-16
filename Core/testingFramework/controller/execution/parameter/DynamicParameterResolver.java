package controller.execution.parameter;

import org.junit.jupiter.api.extension.*;

import model.TestParameter;

import java.util.List;

/**
 * A custom {@link ParameterResolver} implementation for dynamically injecting parameters into test methods.
 * <p>
 * This resolver uses a provided list of {@link TestParameter} instances to match parameter names in the test
 * method signature with corresponding values and types at runtime.
 * 
 * @author Chiara E. Zarrella
 */
public class DynamicParameterResolver implements ParameterResolver {

	 /** List of parameters to resolve during test execution. */
    private final List<TestParameter> listParam;
    
    /**
     * Constructs a new {@code DynamicParameterResolver} with a given list of parameters.
     *
     * @param listParam the list of {@code TestParameter} objects to use for resolving parameters.
     */
    public DynamicParameterResolver(List<TestParameter> listParam) {
        this.listParam = listParam;
    }

    /**
     * Indicates whether this resolver supports the given parameter. This implementation always returns {@code true},
     * meaning all parameters are supported.
     *
     * @param parameterContext the context for the parameter for which an argument should be resolved
     * @param extensionContext the current extension context
     * @return {@code true}, indicating all parameters are supported
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return true;
    }

    /**
     * Resolves the value for the given parameter using the internal list of test parameters.
     *
     * @param parameterContext the context for the parameter for which an argument should be resolved
     * @param extensionContext the current extension context
     * @return the resolved parameter value
     * @throws IllegalArgumentException if the parameter type is unsupported or not found in the list
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        String paramName = parameterContext.getParameter().getName();
        Object value = null;

        for (TestParameter p : listParam) {
            if (p.getName().equals(paramName)) {
                value = parseValue(p.getValue(), p.getType());
                break;
            }
        }

        return value;
    }

    /**
     * Parses a string value into the given target type.
     *
     * @param value the string representation of the value
     * @param type  the target type
     * @return the parsed object
     * @throws IllegalArgumentException if the type is unsupported
     */
    private Object parseValue(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(value);
        }
        // Extend this for additional supported types as needed
        throw new IllegalArgumentException("Unsupported parameter type: " + type);
    }
}


