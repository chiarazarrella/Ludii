package Controller;



import model.TestClass;
import model.TestMethod;
import model.TestsModel;
import other.context.Context;
import testCollector.TestCollector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import View.TestsView;
import launcher.TestLauncher;

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
    
    public List<TestClass> getStaticTestClasses() {
        return model.getStaticTestClasses();
    }
    
    public List<TestClass> getDynamicTestClasses() {
        return model.getDynamicTestClasses();
    }
    
    public void executeTests() {
    	launcher.run(model.getCheckedTestClasses());
        view.updateTestResults();
    }
    
    public void resetTests() {
        model.resetAllTests();
        view.resetTestDisplay();
    }
    
    public void updateTestMethodParameter(TestMethod method, String paramName, String value) {
        method.setValue(paramName, value);
    }
}
