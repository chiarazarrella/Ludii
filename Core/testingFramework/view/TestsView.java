package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.TestsController;
import model.TestClass;
import model.TestMethod;

/**
 * Main view component for displaying and managing test cases.
 * 
 * It provides panels for static and dynamic test categories, buttons
 * to run, reset, and save test results, and manages the mapping
 * between test methods and their UI representations.
 * 
 * The view delegates user actions to the {@link TestsController}.
 * 
 * @author Chiara E. Zarrella
 */
public class TestsView {
    
    /** The main container panel holding all UI components. */
    private JPanel mainPanel;

    /** Scroll pane that hosts the mainPanel for scrollable display. */
    private JScrollPane scrollPane;

    /** Panel displaying static test classes and their test methods. */
    private TestCategoryPanel staticTestsPanel;

    /** Panel displaying dynamic test classes and their test methods. */
    private TestCategoryPanel dynamicTestsPanel;

    /** Button to trigger execution of selected tests. */
    private JButton runButton;

    /** Button to reset test selection and UI states. */
    private JButton resetButton;

    /** Button to save the current test results to a file. */
    private JButton saveButton;

    /** Controller responsible for test logic and coordination. */
    private TestsController controller;

    /** Maps test method IDs to their corresponding UI rows for updates. */
    private Map<Integer, TestMethodRow> testRowsMap = new HashMap<>();

    /**
     * Constructs the TestsView with the provided scroll pane and controller.
     * Initializes the UI and sets up interaction wiring.
     *
     * @param scrollPane the JScrollPane used to display the view contents
     * @param controller the TestsController managing the test execution logic
     */
    public TestsView(JScrollPane scrollPane, TestsController controller) {
        this.scrollPane = scrollPane;
        this.controller = controller;
        controller.setView(this);
        initializeUI();
    }

    /**
     * Initializes and lays out the UI components:
     * - Main panels for static and dynamic tests side by side
     * - Control buttons (Run, Reset, Save)
     * - Scroll pane setup
     */
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel buttonPanel = createButtonPanel();

        JPanel testPanelContainer = new JPanel(new GridLayout(0, 2, 20, 5));
        testPanelContainer.setBackground(Color.WHITE);

        staticTestsPanel = new TestCategoryPanel("Static");
        dynamicTestsPanel = new TestCategoryPanel("Dynamic");

        populateTestPanels();

        testPanelContainer.add(staticTestsPanel);
        testPanelContainer.add(dynamicTestsPanel);

        mainPanel.add(testPanelContainer, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        scrollPane.setViewportView(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    /**
     * Creates the panel containing the Run, Reset, and Save buttons,
     * and wires their event listeners to corresponding controller actions.
     *
     * @return the configured JPanel containing control buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        runButton = new JButton("Run Tests");
        resetButton = new JButton("Reset");
        saveButton = new JButton("Save results");

        runButton.setMargin(new Insets(8, 16, 8, 16));
        resetButton.setMargin(new Insets(8, 16, 8, 16));
        saveButton.setMargin(new Insets(8, 16, 8, 16));

        runButton.addActionListener(e -> {
            if (!controller.hasSelectedMethods()) {
                JOptionPane.showMessageDialog(
                    null,
                    "You need to select at least one test.",
                    "No Test Selected",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            TestMethod methodWithMissingParams = controller.methodWithMissingParamValue();
            if (methodWithMissingParams != null) {
                JOptionPane.showMessageDialog(
                    null,
                    String.format("Method '%s' has missing parameter values.", methodWithMissingParams.getName()),
                    "Missing Parameter Value",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            controller.executeTests();
            runButton.setEnabled(false);
            saveButton.setVisible(true);
        });

        resetButton.addActionListener(e -> {
            controller.resetTests();
            runButton.setEnabled(true);
            saveButton.setVisible(false);
        });

        saveButton.addActionListener(e -> {
            controller.saveResultsToFileTxt();
            JOptionPane.showMessageDialog(
                null,
                "The file has been saved in the folder PlayerDesktop",
                "File successfully generated",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(runButton);
        saveButton.setVisible(false);

        return buttonPanel;
    }

    /**
     * Populates the static and dynamic test category panels with test sections
     * retrieved from the controller.
     *
     * Note: There seems to be a possible mistake in the original code where both
     * static and dynamic panels are populated from static tests only.
     */
    private void populateTestPanels() {
        for (TestClass testClass : controller.getTestClassesWithStaticTests()) {
            TestClassSection section = new TestClassSection(testClass, true, controller, this);
            staticTestsPanel.addTestSection(section);
        }

        // FIXME: Possibly should use getTestClassesWithDynamicTests() for dynamicTestsPanel
        for (TestClass testClass : controller.getTestClassesWithStaticTests()) {
            TestClassSection section = new TestClassSection(testClass, false, controller, this);
            dynamicTestsPanel.addTestSection(section);
        }
    }

    /**
     * Registers a {@link TestMethodRow} to allow UI updates keyed by test method ID.
     *
     * @param methodId the unique ID of the test method
     * @param row the UI component representing the test method row
     */
    public void registerTestRow(int methodId, TestMethodRow row) {
        testRowsMap.put(methodId, row);
    }

    /**
     * Updates the visual representation of all registered test method rows
     * to reflect their latest execution results.
     */
    public void updateTestResults() {
        for (Map.Entry<Integer, TestMethodRow> entry : testRowsMap.entrySet()) {
            entry.getValue().updateTestResult();
        }
    }

    /**
     * Resets the UI state of all test method rows to their initial appearance.
     */
    public void resetTestDisplay() {
        for (TestMethodRow row : testRowsMap.values()) {
            row.resetDisplay();
        }
    }

    /**
     * Clears existing test rows and reinitializes the entire UI.
     * Useful for refreshing the view after data or state changes.
     */
    public void refreshView() {
        testRowsMap.clear();
        initializeUI();
        scrollPane.setViewportView(mainPanel);
    }
}
