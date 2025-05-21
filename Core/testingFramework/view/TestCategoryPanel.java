package view;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.TestsController;

public class TestCategoryPanel extends JPanel {
	
    
    public TestCategoryPanel(String categoryName) {
        
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