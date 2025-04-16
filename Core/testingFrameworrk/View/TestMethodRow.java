package View;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import Controller.TestsController;
import model.TestMethod;

public class TestMethodRow extends JPanel {
    private TestMethod testMethod;
    private TestsController controller;
    private JCheckBox checkBox;
    
    public TestMethodRow(TestMethod testMethod, TestsController controller, TestsView parentView) {
        this.testMethod = testMethod;
        this.controller = controller;
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.WHITE);
        
        createUI();
        
        // Register with parent view for result updates
        int methodId = TestMethod.getId(testMethod.getName());
        parentView.registerTestRow(methodId, this);
    }
    
    private void createUI() {
        checkBox = new JCheckBox(testMethod.getName());
        checkBox.setBackground(Color.WHITE);
        checkBox.setSelected(testMethod.isChecked());
        
        checkBox.addActionListener(e -> {
            testMethod.setChecked(checkBox.isSelected());
        });
        
        add(checkBox);
        
        // If parameters exist, add a button to open the dialog
        if (testMethod.hasDefaultParameters()) {
            JButton paramButton = new JButton(" ⚙️");
            paramButton.addActionListener(e -> {
            	ParameterDialog dialog = new ParameterDialog(testMethod, controller);
                dialog.displayDialog();
            });
            add(paramButton);
        }
    }
    
    public void updateTestResult() {
        if (!checkBox.isSelected()) return;
        
        String duration = testMethod.getDuration();
        
        if (!testMethod.isPassed()) {
            checkBox.setBackground(Color.RED);
            String reason = testMethod.getFailureMessage();
            checkBox.setToolTipText("Duration: " + duration + "ms \nReason: " + reason);
        } else {
            checkBox.setBackground(Color.GREEN);
            checkBox.setToolTipText("Duration: " + duration + "ms");
        }
        
        checkBox.setOpaque(true);
        checkBox.repaint();
    }
    
    public void resetDisplay() {
        checkBox.setBackground(Color.WHITE);
        checkBox.setToolTipText(null);
        checkBox.setSelected(false);
        checkBox.repaint();
    }
}

