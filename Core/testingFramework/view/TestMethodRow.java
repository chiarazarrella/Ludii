package view;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import controller.TestsController;
import model.TestMethod;

/**
 * Represents a single row in the UI corresponding to a test method.
 * Displays the method name with a checkbox to select it, and optionally
 * a settings button for default parameters.
 *
 * This component also handles the display of execution results via color
 * feedback and tooltips.
 * 
 * @author Chiara E. Zarrella
 */
public class TestMethodRow extends JPanel {
    
    /** The test method associated with this row. */
    private TestMethod testMethod;

    /** Controller to handle test selection and parameter actions. */
    private TestsController controller;

    /** Checkbox allowing the user to select/deselect the test. */
    private JCheckBox checkBox;

    /**
     * Constructs a TestMethodRow for a given test method.
     *
     * @param testMethod the test method represented by this row
     * @param controller the controller managing interactions
     * @param parentView the view that holds and registers this row
     */
    public TestMethodRow(TestMethod testMethod, TestsController controller, TestsView parentView) {
        this.testMethod = testMethod;
        this.controller = controller;

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.WHITE);

        createUI();

        // Register this row in the parent view for future result updates
        int methodId = testMethod.getId();
        parentView.registerTestRow(methodId, this);
    }

    /**
     * Builds the UI components for this row:
     * - A checkbox to select the test
     * - A parameter button if the method supports default parameters
     */
    private void createUI() {
        checkBox = new JCheckBox(testMethod.getName());
        checkBox.setBackground(Color.WHITE);
        checkBox.setSelected(testMethod.isSelected());

        checkBox.addActionListener(e -> {
            controller.selectMethod(testMethod, checkBox.isSelected());
        });

        add(checkBox);

        // Add parameter configuration button if applicable
        if (testMethod.hasDefaultParameters()) {
            JButton paramButton = new JButton(" ⚙️");
            paramButton.addActionListener(e -> {
                ParameterDialog dialog = new ParameterDialog(testMethod, controller);
                dialog.displayDialog();
            });
            add(paramButton);
        }
    }

    /**
     * Updates the background color and tooltip of the checkbox
     * based on the result of the test execution.
     */
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

    /**
     * Resets the visual state of this row:
     * - Restores white background
     * - Clears tooltips
     * - Unchecks the selection box
     */
    public void resetDisplay() {
        checkBox.setBackground(Color.WHITE);
        checkBox.setToolTipText(null);
        checkBox.setSelected(false);
        checkBox.repaint();
    }
}
