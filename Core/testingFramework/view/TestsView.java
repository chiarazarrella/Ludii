package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.TestsController;
import model.TestClass;

public class TestsView {
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private TestCategoryPanel staticTestsPanel;
    private TestCategoryPanel dynamicTestsPanel;
    private JButton runButton;
    private JButton resetButton;
    private TestsController controller;
    private Map<Integer, TestMethodRow> testRowsMap = new HashMap<>();
    
    public TestsView(JScrollPane scrollPane, TestsController controller) {
        this.scrollPane = scrollPane;
        this.controller = controller;
        controller.setView(this);
        initializeUI();
    }
    
    private void initializeUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        
        JPanel buttonPanel = createButtonPanel();
        
        JPanel testPanelContainer = new JPanel(new GridLayout(0, 2, 20, 5));
        testPanelContainer.setBackground(Color.WHITE);
        
        staticTestsPanel = new TestCategoryPanel("Static", controller);
        dynamicTestsPanel = new TestCategoryPanel("Dynamic", controller);
        
        populateTestPanels();
        
        testPanelContainer.add(staticTestsPanel);
        testPanelContainer.add(dynamicTestsPanel);
        
        mainPanel.add(testPanelContainer, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        scrollPane.setViewportView(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        runButton = new JButton("Run Tests");
        resetButton = new JButton("Reset");
        runButton.setMargin(new Insets(8, 16, 8, 16)); // top, left, bottom, right
        resetButton.setMargin(new Insets(8, 16, 8, 16));
        
        runButton.addActionListener(e -> {
            controller.executeTests();
            runButton.setEnabled(false);
        });
        
        resetButton.addActionListener(e -> {
            controller.resetTests();
            runButton.setEnabled(true);
        });
        
        buttonPanel.add(resetButton);
        buttonPanel.add(runButton);
        
        return buttonPanel;
    }
    
    private void populateTestPanels() {
        for (TestClass testClass : controller.getStaticTestClasses()) {
            TestClassSection section = new TestClassSection(testClass, true, controller, this);
            staticTestsPanel.addTestSection(section);
        }
        
        for (TestClass testClass : controller.getDynamicTestClasses()) {
            TestClassSection section = new TestClassSection(testClass, false, controller, this);
            dynamicTestsPanel.addTestSection(section);
        }
    }
    
    public void registerTestRow(int methodId, TestMethodRow row) {
        testRowsMap.put(methodId, row);
    }
    
    public void updateTestResults() {
        for (Map.Entry<Integer, TestMethodRow> entry : testRowsMap.entrySet()) {
            entry.getValue().updateTestResult();
        }
    }
    
    public void resetTestDisplay() {
        for (TestMethodRow row : testRowsMap.values()) {
            row.resetDisplay();
        }
    }
    
    public void refreshView() {
        testRowsMap.clear();
        
        initializeUI();
        
        scrollPane.setViewportView(mainPanel);
    }
}
