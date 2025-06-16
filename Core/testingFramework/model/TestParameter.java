package model;

/**
 * Represents a parameter of a test method, including its name, type,
 * current value, and optional default value.
 * 
 * @author Chiara E. Zarrella
 */
public class TestParameter {

    /** The type of the parameter (e.g., String.class, int.class). */
    private final Class<?> type;

    /** The current value assigned to the parameter, if any. */
    private String value;

    /** The default value of the parameter, if defined via annotation. */
    private final String defaultValue;

    /** The name of the parameter, as declared in the method signature. */
    private final String name;

    /**
     * Constructs a new TestParameter.
     *
     * @param name  the name of the parameter
     * @param type  the Java type of the parameter
     * @param value the default value (can be null if not specified)
     */
    public TestParameter(String name, Class<?> type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.defaultValue = value;
    }

    /**
     * Returns the Java type of the parameter.
     *
     * @return the class object representing the parameter's type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Returns the current value assigned to the parameter.
     *
     * @return the value as a String (can be null)
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the name of the parameter.
     *
     * @return the name used in the method signature
     */
    public String getName() {
        return this.name;
    }

    /**
     * Indicates whether this parameter has a default value.
     *
     * @return true if a default value is defined, false otherwise
     */
    public boolean isDefault() {
        return defaultValue != null;
    }

    /**
     * Updates the current value of the parameter.
     *
     * @param value the new value to assign
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the fully qualified name of the parameter's type.
     *
     * @return the canonical class name of the parameter's type
     */
    public String getFullyQualifiedName() {
        return this.type.getName();
    }

    /**
     * Resets the value of the parameter to its default, unless its name is "gameName".
     * If we reset the gameName parameter, we would lose the current game context and the methods can no longer be executed.
     * This does not effect the changing of the game in the application.
     */
    public void reset() {
        if (this.name.equals("gameName")) return;
        this.value = this.defaultValue;
    }
}
