package collector;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.TestTemplate;

import model.TestClass;
import model.TestMethod;

/**
 * The {@code TestCollector} class is responsible for discovering and collecting
 * all test classes and their template methods located in the {@code tests} directory.
 * <p>
 * It scans the filesystem for classes ending with {@code *Test.java}, constructs
 * their corresponding {@link TestClass} instances, and populates them with methods
 * annotated with {@link TestTemplate}.
 * </p>
 * 
 * @author Chiara E. Zarrella
 */
public class TestCollector {
	
	 /**
     * Collects all test classes and their test template methods.
     *
     * @return a list of {@link TestClass} instances, each populated with {@link TestMethod}s
     */
    public static List<TestClass> collectTestClasses() {
        List<TestClass> testClasses = discoverTestClasses();
        addTestMethods(testClasses);
        return testClasses;
    }

    /**
     * Discovers test classes by locating Java files ending with {@code *Test.java}
     * inside the {@code tests} directory.
     *
     * @return a list of {@link TestClass} instances with only class-level metadata
     */
    private static List<TestClass> discoverTestClasses() {
        try {
            File testsDir = findTestsDirectory();
            return findTestClassesInDirectory(testsDir);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for tests directory", e);
        }
    }

    /**
     * Locates the {@code tests} directory by walking up from the current code location
     * until the directory is found.
     *
     * @return a {@link File} reference to the {@code tests} directory
     * @throws URISyntaxException if the class location URI is malformed
     */
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

    /**
     * Recursively finds all classes within the {@code tests} directory that match the naming convention {@code *Test.java}.
     *
     * @param directory the root directory to search
     * @return a list of {@link TestClass} instances created from matching files
     */
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

    /**
     * Adds test methods to each {@link TestClass} in the provided list. Only methods
     * annotated with {@link TestTemplate} are considered.
     *
     * @param testClasses the list of {@link TestClass} instances to populate
     */
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