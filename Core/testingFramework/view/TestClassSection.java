package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.*;

import controller.TestsController;
import model.TestClass;

/**
 * A UI panel that displays a section of test methods belonging to a single {@link TestClass}.
 * It includes a collapsible header with the test class name and a list of {@link TestMethodRow}
 * components for each relevant method.
 *
 * <p>This section distinguishes between static and dynamic methods, based on the
 * {@code isStatic} flag. It allows users to expand or collapse the view to show or hide
 * the individual test methods.
 *
 * <p>This component is designed to be used within a {@link TestCategoryPanel}.
 * 
 * @author Chiara E. Zarrella
 */
public class TestClassSection extends JPanel {
    
    private static final String COLLAPSE_ICON = "▼";
    private static final String EXPAND_ICON = "▲";

    private final TestClass testClass;
    private final boolean isStatic;
    private final TestsController controller;
    private final TestsView parentView;
    private final JPanel testsContainer;
    private final JButton toggleButton;

    /**
     * Constructs a {@code TestClassSection} to display test methods of a given class.
     *
     * @param testClass   the class containing test methods
     * @param isStatic    whether to show only static or only dynamic methods
     * @param controller  the controller used to handle user actions
     * @param parentView  the parent view containing this section
     */
    public TestClassSection(TestClass testClass, boolean isStatic, 
                            TestsController controller, TestsView parentView) {
        this.testClass = testClass;
        this.isStatic = isStatic;
        this.controller = controller;
        this.parentView = parentView;
        this.testsContainer = new JPanel();
        this.toggleButton = createToggleButton();

        initializePanel();
        createUI();
    }

    /**
     * Initializes the layout and borders of the panel.
     */
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    /**
     * Builds the UI by adding the header and test method list.
     */
    private void createUI() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTestsContainer(), BorderLayout.CENTER);
        setupToggleBehavior();
    }

    /**
     * Creates the header panel containing the class name and toggle button.
     *
     * @return the constructed header panel
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));

        JLabel titleLabel = new JLabel(testClass.getPackageName());
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(toggleButton, BorderLayout.EAST);

        return headerPanel;
    }

    /**
     * Creates the toggle button used to expand or collapse the test list.
     *
     * @return a configured JButton instance
     */
    private JButton createToggleButton() {
        JButton button = new JButton(COLLAPSE_ICON);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        return button;
    }

    /**
     * Creates the container that holds the test method rows.
     *
     * @return the test methods container panel
     */
    private JPanel createTestsContainer() {
        testsContainer.setLayout(new BoxLayout(testsContainer, BoxLayout.Y_AXIS));
        testsContainer.setVisible(false); // Initially collapsed

        testClass.getMethods().stream()
            .filter(method -> method.isStatic() == isStatic)
            .forEach(method -> {
                TestMethodRow row = new TestMethodRow(method, controller, parentView);
                testsContainer.add(row);
            });

        return testsContainer;
    }

    /**
     * Sets up the expand/collapse behavior of the toggle button.
     */
    private void setupToggleBehavior() {
        toggleButton.addActionListener(e -> toggleTestsVisibility());
        setCollapsedState(true);
    }

    /**
     * Toggles the visibility of the test method list.
     */
    private void toggleTestsVisibility() {
        boolean isVisible = testsContainer.isVisible();
        testsContainer.setVisible(!isVisible);
        toggleButton.setText(!isVisible ? EXPAND_ICON : COLLAPSE_ICON);
        updatePanelSize(!isVisible);
    }

    /**
     * Adjusts the panel size depending on whether the test methods are shown.
     *
     * @param expanded true if the list is now expanded, false otherwise
     */
    private void updatePanelSize(boolean expanded) {
        if (expanded) {
            int contentHeight = testsContainer.getPreferredSize().height;
            int headerHeight = getComponent(0).getPreferredSize().height;
            setMaximumSize(new Dimension(Integer.MAX_VALUE, headerHeight + contentHeight));
        } else {
            setMaximumSize(new Dimension(Integer.MAX_VALUE, getComponent(0).getPreferredSize().height));
        }
        revalidate();
        repaint();
    }

    /**
     * Sets the collapsed state of the section.
     *
     * @param collapsed true to collapse the section, false to expand it
     */
    private void setCollapsedState(boolean collapsed) {
        testsContainer.setVisible(!collapsed);
        toggleButton.setText(collapsed ? COLLAPSE_ICON : EXPAND_ICON);
        setMaximumSize(new Dimension(Integer.MAX_VALUE,
            getComponent(0).getPreferredSize().height + 
            (collapsed ? 0 : testsContainer.getPreferredSize().height)));
    }
}
