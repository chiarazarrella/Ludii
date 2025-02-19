package app.display.views.tabs.pages;

import board.BoardTest;
import launcher.TestLauncher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
	
	private static String gameName;
	private final Map<JCheckBox, List<JTextField>> testCheckboxes = new HashMap<>();
	private Map<String, List<String>> testsToLaunch = new HashMap<>();


	public TestsPage(PlayerApp app, Rectangle rect, String title, String text, int pageIndex, TabView parent)
	{
		super(app, rect, title, text, pageIndex, parent);
	}

	@Override
	public void updatePage(Context context)
	{
		
		gameName = context.game().name() + ".lud";
		
	}

	@Override
	public void reset() {
		
		
		List<String> dynTestName = new ArrayList<String>();
		dynTestName.add("Dynamic Test 1");
		dynTestName.add("Dynamic Test 2");
		dynTestName.add("Dynamic Test 3");
		
		//List<String> paramsName = new ArrayList<String>();
		
		List<String> dynParams = new ArrayList<String>();
		dynParams.add("param 1");
		dynParams.add("param 2");
		dynParams.add("param 3");
		
	
		Map<String, List<String>> methodSignature = new HashMap<>();	
		try
		{
			Class<?> testClass = Class.forName("board.BoardTest"); // -- at the moment only one test from class BoardTest
			Method[] testMethods = testClass.getDeclaredMethods();
			
			for(var m : testMethods) {
								
				List<String> params = new ArrayList<>();
				
				for(Parameter p: m.getParameters()) {
					
					params.add(p.getName());
				}
				
				methodSignature.put(m.getName(), params);
			
			}
			
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// need to became a method
		clear();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);

		JButton runButton = new JButton("Run Tests");
		runButton.addActionListener(e -> saveSelectedTests());
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(runButton);
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setOpaque(true);

		JPanel testPanel = new JPanel();
		testPanel.setLayout(new GridLayout(0, 2, 20, 5));
		testPanel.setBackground(Color.WHITE);
		testPanel.setOpaque(true);

		mainPanel.add(testPanel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		JPanel staticTestPanel;
		JPanel dynamicTestPanel;
		
		staticTestPanel = createSectionTestPanel("Static");
		dynamicTestPanel = createSectionTestPanel("Dynamic");
		
		// static tests
		for(String s : methodSignature.keySet()) {
			createTestRow(staticTestPanel, s, methodSignature.get(s));
		}
		
		//dynamic tests
		for(String s : dynTestName) {
			createTestRow(dynamicTestPanel, s, dynParams);
		}

		testPanel.add(staticTestPanel);
		testPanel.add(dynamicTestPanel);
		
		super.scrollPane().setViewportView(mainPanel);
		super.scrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		super.scrollPane().validate();
		super.scrollPane().repaint();
		
		
		
		//TestLauncher launcher = new TestLauncher();
		//launcher.run(gameName, 3);
		
	}
	
	// create the space for tests
	private JPanel createSectionTestPanel(String sectionName) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.add(new JLabel(sectionName));

       
        return sectionPanel;
    }
	
	// create row for test (checkbox + downslide window)
	private void createTestRow(JPanel parent, String testName, List<String> params) {
	    List<JTextField> paramFields = new ArrayList<>();
		
		JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(Color.WHITE);

        JCheckBox checkBox = new JCheckBox(testName);
        checkBox.setBackground(Color.WHITE);

        JButton toggleButton = new JButton("▼"); 
        toggleButton.setBorderPainted(false);
        toggleButton.setFocusPainted(false);
        toggleButton.setContentAreaFilled(false);
        
        JPanel formPanel = createFormPanel(params, paramFields);
        formPanel.setVisible(false);

        toggleButton.addActionListener(e -> formPanel.setVisible(!formPanel.isVisible()));

        JPanel checkBoxContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxContainer.setBackground(Color.WHITE);
        checkBoxContainer.add(checkBox);
        checkBoxContainer.add(toggleButton);

        rowPanel.add(checkBoxContainer, BorderLayout.NORTH);
        rowPanel.add(formPanel, BorderLayout.CENTER);

        parent.add(rowPanel);
        
        testCheckboxes.put(checkBox, paramFields); // save reference to checkbox and text fields
    }
	
	// create downslide window for parameter
	private JPanel createFormPanel(List<String> paramsName, List<JTextField> paramFields) {
	    JPanel formPanel = new JPanel();
	    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
	    formPanel.setBackground(new Color(240, 240, 240)); 

	    for (String p : paramsName) {
	    	
	    	if(p.equals("gameName")) continue;
	    	
	        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        JLabel label = new JLabel(p);
	        JTextField textField = new JTextField(10);
	        textField.setMaximumSize(new Dimension(200, 25));
	        row.add(label);
	        row.add(textField);
	        formPanel.add(row);
	        
	        paramFields.add(textField);

	    }
	    
	    return formPanel; 	
	}
	
	
	private void saveSelectedTests() {
	   
		for (Map.Entry<JCheckBox, List<JTextField>> entry : testCheckboxes.entrySet()) {
	        JCheckBox checkBox = entry.getKey();
	        List<JTextField> paramFields = entry.getValue();

	        if (checkBox.isSelected()) {  
	            List<String> paramValues = new ArrayList<>();
	            for (JTextField field : paramFields) {
	                paramValues.add(field.getText().trim());
	            }

	            testsToLaunch.put(checkBox.getText(), paramValues);
	        }
	    }
		
		for(String t: testsToLaunch.keySet()) {
			System.out.println("This is what the user has chosen: " + t + "\n values: " + testsToLaunch.get(t).toString());
		}
	}

	
	private void launchTests() {
		
	}



}
