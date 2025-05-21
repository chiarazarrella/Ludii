package controller;



import model.TestClass;
import model.TestMethod;
import model.TestsModel;
import other.context.Context;
import util.TestResultLogger;
import util.TestResultLogger.TestResult;
import view.TestsView;
import java.util.ArrayList;
import java.util.List;
import controller.execution.TestLauncher;

public class TestsController {
	
    private TestsModel model;
    private TestsView view;
    private TestLauncher launcher;
    
    public TestsController(TestsModel model) {
        this.model = model;
        this.launcher = new TestLauncher();
    }
    
    public void setView(TestsView view) {
        this.view = view;
    }
    
    public void updateGameName(Context context) {
        String gameName = context.game().name() + ".lud";
        model.setGameName(gameName);
    }
    
    public List<TestClass> getTestClassesWithStaticTests() {
        return model.getTestClassesWithStaticTests();
    }
    
    public List<TestClass> getTestClassesWithDynamicTests() {
        return model.getTestClassesWithDynamicTests();
    }
    
    public void executeTests() {
    	launcher.run(model.getSelectedTestClasses());
        view.updateTestResults();
    }
    
    public void resetTests() {
        model.resetAllTests();
        view.resetTestDisplay();
    }
    
    public void updateTestMethodParameter(TestMethod method, String paramName, String value) {
        method.setValue(paramName, value);
    }
    

	public void selectMethod(TestMethod method, boolean selected) {
		method.setSelected(selected);
	}
	
	public void saveResults() {
		
	    List<TestResult> allResults = new ArrayList<>();
	    for (TestClass testClass : model.getSelectedTestClasses()) {
	        for (TestMethod method : testClass.getMethods()) {
	        	if(!method.isSelected()) {
	        		continue;
	        	}
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

	    TestResultLogger.saveTestResultsToTimestampedFile("test_results", allResults);

	}
}
