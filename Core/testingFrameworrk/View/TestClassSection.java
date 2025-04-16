package View;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controller.TestsController;
import model.TestClass;
import model.TestMethod;

public class TestClassSection extends JPanel {
	
    private TestClass testClass;
    private boolean isStatic;
    private TestsController controller;
    private TestsView parentView;
    private JPanel testsContainer;
    
    public TestClassSection(TestClass testClass, boolean isStatic, 
    		TestsController controller, TestsView parentView) {
        this.testClass = testClass;
        this.isStatic = isStatic;
        this.controller = controller;
        this.parentView = parentView;
        
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        createUI();
    }
    
    private void createUI() {
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout(0, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
        
        JLabel titleLabel = new JLabel(testClass.getPackageName());
        JButton toggleButton = new JButton("▼");
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        toggleButton.setMargin(new Insets(0, 0, 0, 0));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(toggleButton, BorderLayout.EAST);
        
        // Tests container
        testsContainer = new JPanel();
        testsContainer.setLayout(new BoxLayout(testsContainer, BoxLayout.Y_AXIS));
        testsContainer.setVisible(false); // Initially collapsed
        
        // Add test methods
        for (TestMethod method : testClass.getMethods().values()) {
            if (method.isStatic() == isStatic) {
                TestMethodRow row = new TestMethodRow(method, controller, parentView);
                testsContainer.add(row);
            }
        }
        
        // Toggle action
        toggleButton.addActionListener(e -> {
            boolean isVisible = testsContainer.isVisible();
            testsContainer.setVisible(!isVisible);
            toggleButton.setText(!isVisible ? "▲" : "▼");
            
            if (!isVisible) {
                // When expanded, remove size constraints
                int contentHeight = testsContainer.getPreferredSize().height;
                int headerHeight = headerPanel.getPreferredSize().height;
                setMaximumSize(new Dimension(Integer.MAX_VALUE, headerHeight + contentHeight));
            } else {
                // When collapsed, constrain height to just the header
                setMaximumSize(new Dimension(Integer.MAX_VALUE, headerPanel.getPreferredSize().height));
            }
            
            revalidate();
            repaint();
        });
        
        // Add components to panel
        add(headerPanel, BorderLayout.NORTH);
        add(testsContainer, BorderLayout.CENTER);
        
        // Set initial max size (collapsed)
        setMaximumSize(new Dimension(Integer.MAX_VALUE, headerPanel.getPreferredSize().height));
    }
}

