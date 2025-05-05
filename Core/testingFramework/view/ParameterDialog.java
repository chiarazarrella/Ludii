package view;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.TestsController;
import model.TestMethod;
import model.TestParameter;

public class ParameterDialog extends JDialog {
	
    private TestMethod testMethod;
    private TestsController controller;
    private Map<String, JTextField> paramFields = new HashMap<>();
    
    public ParameterDialog(TestMethod testMethod, TestsController controller) {
        this.testMethod = testMethod;
        this.controller = controller;
        
        setTitle("Set Parameters for " + testMethod.getName());
        setSize(300, 200);
        setModal(true);
        
        createUI();
    }
    
    private void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        for (TestParameter parameter : testMethod.getParameters()) {
            String paramName = parameter.getName();
            
            // Skip gameName parameter which is set automatically
            if (paramName.equals("gameName")) continue;
            
            JPanel paramRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            
            JLabel label = new JLabel(paramName + ": ");
            JTextField textField = new JTextField(15);
            textField.setText(parameter.getValue());
            
            paramRow.add(label);
            paramRow.add(textField);
            panel.add(paramRow);
            
            paramFields.put(paramName, textField);
        }
        
        JButton submitButton = new JButton("Save");
        submitButton.addActionListener(e -> {
            for (Map.Entry<String, JTextField> entry : paramFields.entrySet()) {
                String paramName = entry.getKey();
                String newValue = entry.getValue().getText();
                controller.updateTestMethodParameter(testMethod, paramName, newValue);
            }
            dispose();
        });
        
        panel.add(submitButton);
        add(panel);
        setLocationRelativeTo(null);
    }
    
    public void displayDialog() {
        setVisible(true);
    }
}

