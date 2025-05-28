package collector;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.TestTemplate;

import model.TestClass;
import model.TestMethod;
import util.DefaultParameter;

public class TestCollector {
	

    public static List<TestClass> collectTestClasses() {
        List<TestClass> testClasses = discoverTestClasses();
        addTestMethods(testClasses);
        return testClasses;
    }

    
    private static List<TestClass> discoverTestClasses() {
        try {
            File testsDir = findTestsDirectory();
            return findTestClassesInDirectory(testsDir);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for tests directory", e);
        }
    }

    private static File findTestsDirectory() throws URISyntaxException {
        File currentDir = new File(TestCollector.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI());

        File projectRoot = currentDir;
        while (!new File(projectRoot, "tests").exists() && projectRoot.getParentFile() != null) {
            projectRoot = projectRoot.getParentFile();
        }

        File testsDir = new File(projectRoot, "tests");
        if (!testsDir.exists()) {
            throw new RuntimeException("Tests directory not found at: " + testsDir.getAbsolutePath());
        }
        return testsDir;
    }

    private static List<TestClass> findTestClassesInDirectory(File directory) {
        List<TestClass> testClasses = new ArrayList<>();
        File[] subDirs = directory.listFiles(File::isDirectory);

        if (subDirs == null) return testClasses;

        for (File subDir : subDirs) {
            File[] javaFiles = subDir.listFiles(f -> f.getName().endsWith("Test.java"));
            if (javaFiles != null) {
                for (File file : javaFiles) {
                    String className = file.getName().replaceFirst("\\.java$", "");
                    testClasses.add(new TestClass(subDir.getName(), className));
                }
            }
        }

        return testClasses;
    }

    private static void addTestMethods(List<TestClass> testClasses) {
        for (TestClass testClass : testClasses) {
            try {
                Class<?> clazz = Class.forName(testClass.getFullyQualifiedName());
                for (Method method : clazz.getDeclaredMethods()) {
                	
                    if (method.isAnnotationPresent(TestTemplate.class)) {
                        testClass.addMethod(new TestMethod(method));
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Test class not found: " + testClass.getFullyQualifiedName(), e);
            }
        }
    }
}