package view;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel that visually represents a category (static or dynamic) of tests within the test dashboard UI.
 * It displays a title for the category and can contain multiple {@link TestClassSection} components,
 * each representing a group of test methods belonging to a specific test class.
 *
 * <p>This panel uses a vertical box layout to stack components from top to bottom.
 * It is intended to be used as part of a hierarchical view for organizing test classes by category.
 *
 * @author Chiara E. Zarrella
 */
public class TestCategoryPanel extends JPanel {
    
    /**
     * Constructs a {@code TestCategoryPanel} with the given category name.
     * The panel is initialized with a vertical layout and white background,
     * and the category name is displayed at the top as a label.
     *
     * @param categoryName the name of the test category to display
     */
    public TestCategoryPanel(String categoryName) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(categoryName);
        add(titleLabel);
    }

    /**
     * Adds a {@link TestClassSection} to this category panel.
     * This allows the panel to visually group test classes under the category.
     *
     * @param section the test class section to add
     */
    public void addTestSection(TestClassSection section) {
        add(section);
    }
}
