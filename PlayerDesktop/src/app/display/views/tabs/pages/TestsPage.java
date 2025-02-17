package app.display.views.tabs.pages;

import board.BoardTest;
import launcher.TestLauncher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import app.PlayerApp;
import app.display.views.tabs.TabPage;
import app.display.views.tabs.TabView;
import other.context.Context;

public class TestsPage extends TabPage
{

	public TestsPage(PlayerApp app, Rectangle rect, String title, String text, int pageIndex, TabView parent)
	{
		super(app, rect, title, text, pageIndex, parent);
	}

	@Override
	public void updatePage(Context context)
	{
		// TODO Auto-generated method stub
		clear();

		JPanel testsPanel = new JPanel();
		testsPanel.setLayout(new BorderLayout());
		testsPanel.setBackground(Color.WHITE);

		JButton runButton = new JButton("Run Tests");
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(runButton);
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setOpaque(true);

		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new GridLayout(0, 2, 20, 5));
		checkBoxPanel.setBackground(Color.WHITE);
		checkBoxPanel.setOpaque(true);

		testsPanel.add(checkBoxPanel, BorderLayout.CENTER);
		testsPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		addTestSection(checkBoxPanel, "Static", new String[]{"Static Test 1", "Static Test 2", "Static Test 3"});
        addTestSection(checkBoxPanel, "Dynamic", new String[]{"Dynamic Test 1", "Dynamic Test 2", "Dynamic Test 3"});

		super.scrollPane().setViewportView(testsPanel);
		super.scrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		super.scrollPane().validate();
		super.scrollPane().repaint();
		
		
		String game = "Tic-Tac-Toe.lud";
		TestLauncher launcher = new TestLauncher();
		launcher.run(game, 3);
		
	}

	@Override
	public void reset() {
		
		
	}
	
	// create the space for tests
	private void addTestSection(JPanel parentPanel, String sectionName, String[] testNames) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.add(new JLabel(sectionName));

        for (String testName : testNames) {
            sectionPanel.add(createTestRow(testName));
        }

        parentPanel.add(sectionPanel);
    }
	
	private JPanel createTestRow(String testName) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);

        JCheckBox checkBox = new JCheckBox(testName);
        checkBox.setBackground(Color.WHITE);

        JButton toggleButton = new JButton("▼"); 
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);

        JPanel formPanel = createFormPanel();
        formPanel.setVisible(false);

        toggleButton.addActionListener(e -> formPanel.setVisible(!formPanel.isVisible()));

        JPanel checkBoxContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxContainer.setBackground(Color.WHITE);
        checkBoxContainer.add(checkBox);
        checkBoxContainer.add(toggleButton);

        rowPanel.add(checkBoxContainer, BorderLayout.NORTH);
        rowPanel.add(formPanel, BorderLayout.CENTER);

        return rowPanel;
    }
	
	 private JPanel createFormPanel() {
	        JPanel formPanel = new JPanel();
	        formPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	        formPanel.setBackground(new Color(240, 240, 240)); 
	        formPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

	        formPanel.add(new JLabel("Parameter:"));
	        formPanel.add(new JTextField(10));

	        return formPanel;
	    }

}
