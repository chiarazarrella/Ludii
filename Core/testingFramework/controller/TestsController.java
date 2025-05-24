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

public class TestsController {
	
    private TestsModel model;
    private TestsView view;
    private TestLauncher launcher;
    private String gameName = null;
    
    public TestsController(TestsModel model) {
        this.model = model;
        this.launcher = new TestLauncher();
    }
    
    public void setView(TestsView view) {
        this.view = view;
    }
    
    public void updateGameName(Context context) {
        gameName = context.game().name() + ".lud";
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
    
    public TestMethod methodWithMissingParamValue() {
    	
    	for(TestClass tc: model.getSelectedTestClasses()) {
    		for(TestMethod method: tc.getSelectedMethods()) {
    			if(method.parameterWithoutValue() != null) {
    				return method;
    			}
    		}
    	}
    	
    	return null;
    }
    
    
    public boolean hasSelectedMethods() {
    	return model.getSelectedTestClasses().size() > 0;
    }
    
    public void updateTestMethodParameter(TestMethod method, String paramName, String value) {
        method.setValue(paramName, value);
    }
    

	public void selectMethod(TestMethod method, boolean selected) {
		method.setSelected(selected);
	}
	
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
