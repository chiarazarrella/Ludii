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

/**
 * A dialog window used to set parameter values for a specific {@link TestMethod}.
 * Allows users to edit values of parameters (except "gameName") via a simple UI.
 * Updates are applied to the model using the {@link TestsController}.
 * 
 * @author Chiara E. Zarrella
 */
@SuppressWarnings("serial")
public class ParameterDialog extends JDialog {

	private final TestMethod testMethod;
    private final TestsController controller;
    private final Map<String, JTextField> paramFields = new HashMap<>();

    /**
     * Constructs a new {@code ParameterDialog} for the given test method.
     *
     * @param testMethod the test method whose parameters are to be edited
     * @param controller the controller responsible for updating parameter values
     */
    public ParameterDialog(TestMethod testMethod, TestsController controller) {
        this.testMethod = testMethod;
        this.controller = controller;

        setTitle("Set Parameters for " + testMethod.getName());
        setSize(300, 200);
        setModal(true);

        createUI();
    }

    /**
     * Initializes and lays out the user interface components.
     * For each parameter (excluding "gameName"), a label and text field are added to the dialog.
     * A "Save" button is provided to apply the changes through the controller.
     */
    private void createUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (TestParameter parameter : testMethod.getParameters()) {
            String paramName = parameter.getName();

            // Skip 'gameName' parameter which is set automatically
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

    /**
     * Displays the dialog to the user.
     */
    public void displayDialog() {
        setVisible(true);
    }
}
