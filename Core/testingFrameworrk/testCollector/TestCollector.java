package testCollector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.TestClass;

public class TestCollector {

    public static List<TestClass> discoverTestPackages() {
        List<TestClass> testPackages = new ArrayList<>();
        
        try {
            // Get the directory containing the compiled TestCollector class
            File currentDir = new File(TestCollector.class.getProtectionDomain()
                                    .getCodeSource()
                                    .getLocation()
                                    .toURI());
            // Navigate up to the project root (assuming standard Maven/Gradle structure)
            File projectRoot = currentDir;
            while (!new File(projectRoot, "tests").exists() && projectRoot.getParentFile() != null) {
                projectRoot = projectRoot.getParentFile();
            }
            
            File testsDir = new File(projectRoot, "tests");
            if (!testsDir.exists()) {
                throw new RuntimeException("Tests directory not found at: " + testsDir.getAbsolutePath());
            }

            // Discover packages recursively
            discoverPackages(testsDir, "tests", testPackages);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return testPackages;
    }

    private static void discoverPackages(File directory, String currentPackage, List<TestClass> result) {
        File[] files = directory.listFiles();
        if (files != null) {
            
      
            for (File file : files) {
                if (file.isDirectory()) {
					System.out.println("Discovering package: " + file.getName());
                	result.add(new TestClass(file.getName()));
                }
            }
            
            
        }
    }
}