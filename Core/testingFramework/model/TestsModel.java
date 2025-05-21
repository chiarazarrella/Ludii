package model;

import java.util.List;
import java.util.stream.Collectors;

import collector.TestCollector;

public class TestsModel {
    private List<TestClass> testClasses;
    
    // Constructor and methods
    public TestsModel() {
        this.testClasses = TestCollector.collectTestClasses();
    }
    
    public void setGameName(String gameName) {
        // Update all test methods with the game name
        for(TestClass testClass: testClasses) {
            for(TestMethod method: testClass.getMethods()) {
                method.setValue("gameName", gameName);
            }
        }
    }
    
    public List<TestClass> getTestClasses() {
        return testClasses;
    }
    
    public List<TestClass> getStaticTestClasses() {
        return testClasses.stream()
            .filter(tc -> tc.hasStaticTests(tc.getPackageName()))
            .collect(Collectors.toList());
    }
    
    public List<TestClass> getDynamicTestClasses() {
        return testClasses.stream()
            .filter(tc -> tc.hasDynamicTests(tc.getPackageName()))
            .collect(Collectors.toList());
    }
    
    public List<TestClass> getCheckedTestClasses() {
        return testClasses.stream()
            .filter(TestClass::hasAtLeastOneMethodChecked)
            .collect(Collectors.toList());
    }
    
    public void resetAllTests() {
        for(TestClass testClass: testClasses) {
            testClass.reset();
        }
    }
    
   
}
