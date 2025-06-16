package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Represents a test class containing a list of test methods and metadata such as its name and package.
 * 
 * <p>This class is used to organize test methods related to a Concept under a common Java class and supports
 * operations like retrieving selected tests, filtering static or dynamic tests, and
 * generating fully qualified names.</p>
 * 
 * @author Chiara E. Zarrella
 */
public class TestClass {
	
	/** Name of the package to which the test class belongs */
    private final String packageName;

    /** List of test methods defined in this class */
    private List<TestMethod> methods;

    /** Simple name of the class (without package) */
    private final String name;
	
	
    /**
     * Constructs a new {@code TestClass} with the specified package and class name.
     *
     * @param packageName the name of the package
     * @param name        the name of the class
     */
	public TestClass(String packageName, String name) {
		this.packageName = packageName;
		this.methods = new ArrayList<>();
		this.name = name;
	}

	/**
     * @return the package name of the test class
     */
	public String getPackageName() {
		return this.packageName;
	}
	
	/**
     * @return the simple name of the test class
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the test method with the given ID.
     *
     * @param id the unique identifier of the method
     * @return the {@code TestMethod} with the given ID, or {@code null} if not found
     */
    public TestMethod getMethod(int id) {
        for (TestMethod method : methods) {
            if (method.getId() == id) {
                return method;
            }
        }
        return null;
    }

    /**
     * Adds a new test method to this test class.
     *
     * @param method the method to add
     */
    public void addMethod(TestMethod method) {
        methods.add(method);
    }

    /**
     * @return the list of all test methods in this class
     */
    public List<TestMethod> getMethods() {
        return this.methods;
    }

    /**
     * @return the list of test methods that are currently selected in the UI.
     */
    public List<TestMethod> getSelectedMethods() {
        return methods.stream()
                .filter(TestMethod::isSelected)
                .collect(Collectors.toList());
    }

    /**
     * @return the fully qualified name of the class (package + class name)
     */
    public String getFullyQualifiedName() {
        return packageName.toLowerCase() + "." + name;
    }

    /**
     * Returns the fully qualified name for a specific test method.
     * Format: {@code package.class#methodName(parameters)}
     *
     * @param id the identifier of the test method
     * @return the fully qualified name for the method
     */
    public String getFullyQualifiedNameForMethod(int id) {
        TestMethod method = this.getMethod(id);
        return this.getFullyQualifiedName() + "#" + method.getFullyQualifiedName();
    }

    /**
     * Resets all test methods in this class (e.g., deselects them or resets their state).
     */
    public void reset() {
        for (TestMethod m : this.methods) {
            m.reset();
        }
    }

    /**
     * Provides a readable string representation of this test class and its methods.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TestClass: ").append(this.packageName).append("\n");
        sb.append("Methods:\n");
        for (TestMethod m : this.methods) {
            sb.append(m.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Checks whether the test class has any static tests.
     *
     * @return {@code true} if a static test exists, {@code false} otherwise
     */
    public boolean hasStaticTests() {
        for (TestMethod m : this.methods) {
            if (m.isStatic())
                return true;
        }
        return false;
    }

    /**
     * Checks whether the test class has any dynamic tests.
     *
     * @return {@code true} if a dynamic test exists, {@code false} otherwise
     */
    public boolean hasDynamicTests() {
        for (TestMethod m : this.methods) {
            if (!m.isStatic())
                return true;
        }
        return false;
    }

}
