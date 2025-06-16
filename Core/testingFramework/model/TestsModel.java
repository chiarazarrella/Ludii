package model;

import java.util.List;

import java.util.stream.Collectors;

import collector.TestCollector;

/**
 * The {@code TestsModel} class serves as the central data holder (model) for the 
 * available test classes and their methods. It is responsible for collecting 
 * test classes, providing filtered views (e.g., static, dynamic, selected), 
 * and applying updates across test methods (e.g., setting the game name).
 * 
 * @author Chiara E. Zarrella
 */
public class TestsModel {
	
	/** The list of all test classes discovered in the environment. */
    private List<TestClass> testClasses;
    
    /**
     * Constructs a new {@code TestsModel} by collecting all test classes
     * using the {@link TestCollector}.
     */
    public TestsModel() {
        this.testClasses = TestCollector.collectTestClasses();
    }
    
    /**
     * Sets the given game name as a parameter for all test methods that expect it.
     * This is typically used for tests requiring a specific game context.
     *
     * @param gameName the name of the game to inject as a parameter
     */
    public void setGameName(String gameName) {
        // Update all test methods with the game name
        for(TestClass testClass: testClasses) {
            for(TestMethod method: testClass.getMethods()) {
                method.setValue("gameName", gameName);
            }
        }
    }
    
	/**
	 * Returns the list of all test classes.
	 *
	 * @return List of {@link TestClass} instances.
	 */
    public List<TestClass> getTestClasses() {
        return testClasses;
    }
    
	/**
	 * Returns the list of test classes that contain static tests.
	 *
	 * @return List of {@link TestClass} instances with static tests.
	 */
    public List<TestClass> getTestClassesWithStaticTests() {
        return testClasses.stream()
            .filter(tc -> tc.hasStaticTests())
            .collect(Collectors.toList());
    }
    
    
    /**
	 * Returns the list of test classes that contain dynamic tests.
	 *
	 * @return List of {@link TestClass} instances with dynamic tests.
	 */
    public List<TestClass> getTestClassesWithDynamicTests() {
        return testClasses.stream()
            .filter(tc -> tc.hasDynamicTests())
            .collect(Collectors.toList());
    }
    
	/**
	 * Returns the list of test classes that have at least one selected method.
	 *
	 * @return List of {@link TestClass} instances with selected methods.
	 */
    public List<TestClass> getSelectedTestClasses() {
        return testClasses.stream()
            .filter(tc -> tc.getSelectedMethods().size() > 0)
            .collect(Collectors.toList());
    }
    
	/**
	 * Resets all test methods in all test classes. This clears any execution
	 * results and resets parameters to their defaults.
	 */
    public void resetAllTests() {
        for(TestClass testClass: testClasses) {
            testClass.reset();
        }
    }
    
   
}
