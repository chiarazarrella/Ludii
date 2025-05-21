package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.*;

import controller.TestsController;
import model.TestClass;
import model.TestMethod;

public class TestClassSection extends JPanel {
    private static final String COLLAPSE_ICON = "▼";
    private static final String EXPAND_ICON = "▲";
    
    private final TestClass testClass;
    private final boolean isStatic;
    private final TestsController controller;
    private final TestsView parentView;
    private final JPanel testsContainer;
    private final JButton toggleButton;

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

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    private void createUI() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createTestsContainer(), BorderLayout.CENTER);
        setupToggleBehavior();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
        
        JLabel titleLabel = new JLabel(testClass.getPackageName());
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(toggleButton, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JButton createToggleButton() {
        JButton button = new JButton(COLLAPSE_ICON);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(0, 0, 0, 0));
        return button;
    }

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

    private void setupToggleBehavior() {
        toggleButton.addActionListener(e -> toggleTestsVisibility());
        setCollapsedState(true);
    }

    private void toggleTestsVisibility() {
        boolean isVisible = testsContainer.isVisible();
        testsContainer.setVisible(!isVisible);
        toggleButton.setText(!isVisible ? EXPAND_ICON : COLLAPSE_ICON);
        updatePanelSize(!isVisible);
    }

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

    private void setCollapsedState(boolean collapsed) {
        testsContainer.setVisible(!collapsed);
        toggleButton.setText(collapsed ? COLLAPSE_ICON : EXPAND_ICON);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 
            getComponent(0).getPreferredSize().height + (collapsed ? 0 : testsContainer.getPreferredSize().height)));
    }
}