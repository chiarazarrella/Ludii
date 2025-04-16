package View;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Controller.TestsController;

public class TestCategoryPanel extends JPanel {
	
    private String categoryName;
    private TestsController controller;
    
    public TestCategoryPanel(String categoryName, TestsController controller) {
        this.categoryName = categoryName;
        this.controller = controller;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        
        // Add title
        JLabel titleLabel = new JLabel(categoryName);
        add(titleLabel);
    }
    
    public void addTestSection(TestClassSection section) {
        add(section);
    }
}