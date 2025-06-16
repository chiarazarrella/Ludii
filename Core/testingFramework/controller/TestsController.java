package controller;



import model.TestClass;
import model.TestMethod;
import model.TestsModel;
import other.context.Context;
import util.TestResultLogger;
import util.TestResultLogger.TestResult;
import view.TestsView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import controller.execution.TestLauncher;
/**
 * Controller class responsible for managing test execution in the MVC architecture.
 * 
 * Coordinates interactions between the {@link TestsModel}, the {@link TestsView},
 * and the {@link TestLauncher} for running tests.
 * Provides functionalities to update, execute, reset tests, manage test parameters,
 * and save test results.
 * 
 * @author Chiara E. Zarrella
 */
public class TestsController {
	
    private TestsModel model;
    private TestsView view;
    private TestLauncher launcher;
    private String gameName = null;
    
    /**
     * Constructs the TestsController with the specified test model.
     * 
     * @param model the model containing the tests to control
     */
    public TestsController(TestsModel model) {
        this.model = model;
        this.launcher = new TestLauncher();
    }
    
    /**
     * Sets the view associated with this controller.
     * 
     * @param view the view to associate
     */
    public void setView(TestsView view) {
        this.view = view;
    }
    
    /**
     * Updates the game name in the model based on the provided context.
     * 
     * @param context the context containing information about the current game
     */
    public void updateGameName(Context context) {
        gameName = context.game().name() + ".lud";
        model.setGameName(gameName);
    }
    
    /**
     * Returns the list of test classes containing static tests.
     * 
     * @return list of test classes with static tests
     */
    public List<TestClass> getTestClassesWithStaticTests() {
        return model.getTestClassesWithStaticTests();
    }
    
    /**
     * Returns the list of test classes containing dynamic tests.
     * 
     * @return list of test classes with dynamic tests
     */
    public List<TestClass> getTestClassesWithDynamicTests() {
        return model.getTestClassesWithDynamicTests();
    }
    
    /**
     * Executes the selected tests via the test launcher and updates the view with results.
     */
    public void executeTests() {
    	launcher.run(model.getSelectedTestClasses());
        view.updateTestResults();
    }
    
    /**
     * Resets the state of all tests in the model and updates the view to reflect the reset.
     */
    public void resetTests() {
        model.resetAllTests();
        view.resetTestDisplay();
    }
    
    /**
     * Returns the first selected test method that has parameters without assigned values.
     * 
     * @return a test method missing parameter values, or {@code null} if none are found
     */
    public TestMethod methodWithMissingParamValue() {
    	for (TestClass tc : model.getSelectedTestClasses()) {
    		for (TestMethod method : tc.getSelectedMethods()) {
    			if (method.parameterWithoutValue() != null) {
    				return method;
    			}
    		}
    	}
    	return null;
    }
    
    /**
     * Indicates whether at least one test method is selected.
     * 
     * @return {@code true} if there are selected test methods; {@code false} otherwise
     */
    public boolean hasSelectedMethods() {
    	return model.getSelectedTestClasses().size() > 0;
    }
    
    /**
     * Updates the value of a specific parameter of a test method.
     * 
     * @param method the test method to update
     * @param paramName the name of the parameter to update
     * @param value the new value to assign to the parameter
     */
    public void updateTestMethodParameter(TestMethod method, String paramName, String value) {
        method.setValue(paramName, value);
    }
    
    /**
     * Selects or deselects a test method.
     * 
     * @param method the test method to select or deselect
     * @param selected {@code true} to select the method; {@code false} to deselect
     */
	public void selectMethod(TestMethod method, boolean selected) {
		method.setSelected(selected);
	}
	
	/**
	 * Saves the results of the selected tests to a text file.
	 * The file is created or updated using the dedicated logger utility.
	 */
	public void saveResultsToFileTxt() {
	    List<TestResult> allResults = new ArrayList<>();
	    for (TestClass testClass : model.getSelectedTestClasses()) {
	        for (TestMethod method : testClass.getSelectedMethods()) {
	            TestResult result = new TestResult(
	                method.getName(),
	                method.getDuration(),
	                method.isPassed(),
	                method.getFailureMessage(),
	                method.isStatic()
	            );
	            allResults.add(result);
	        }
	    }
	    
	    File file = TestResultLogger.createFile(gameName);
	    TestResultLogger.writeSummary(file, allResults);
	    TestResultLogger.appendResults(file, gameName, allResults);
	}
}
