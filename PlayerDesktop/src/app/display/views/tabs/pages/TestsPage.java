package app.display.views.tabs.pages;

import launcher.TestLauncher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import annotation.DefaultParameter;
import app.PlayerApp;
import app.display.views.tabs.TabPage;
import app.display.views.tabs.TabView;
import other.context.Context;
import util.Pair;
import util.TestClass;
import util.TestMethod;
public class TestsPage extends TabPage
{
	
	private static String gameName;
	private final Map<JCheckBox, List<JTextField>> testCheckboxes = new HashMap<>();
	private Map<String, List<String>> testsToLaunch = new HashMap<>(); 
	private List<TestClass> testClasses = new ArrayList<TestClass>(); // used for later improvement of the framework
	

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
		
		// DYN HARDCODED
		List<String> dynTestName = new ArrayList<String>();
		dynTestName.add("Dynamic Test 1");
		dynTestName.add("Dynamic Test 2");
		dynTestName.add("Dynamic Test 3");
				
		
		
		List<TestMethod> dynMethods = new ArrayList<TestMethod>();
		List<Pair<String, Class<?>>> dynParams = new ArrayList<>();
		List<Pair<String, Object>> dynParamsValues = new ArrayList<>();
		dynParams.add(new Pair<String, Class<?>>("param 1", String.class));
		dynParams.add(new Pair<String, Class<?>>("param 2", String.class));
		dynParams.add(new Pair<String, Class<?>>("param 3", String.class));
		
		for(String dyn: dynTestName) {
			TestMethod method = new TestMethod(dyn, dynParams, dynParamsValues);
			dynMethods.add(method);
		}
		
		
		// STATIC 
		TestClass board = new TestClass("Board");
		TestClass player = new TestClass("Player");
		TestClass piece = new TestClass("Piece");
		
		testClasses.add(board);
		testClasses.add(player);
		testClasses.add(piece);
		
		
		try
		{
			
			for(TestClass testClass: testClasses) {
				
				Method[] methods = Class.forName(testClass.getClassName()).getDeclaredMethods();
				
				for(Method m: methods) {
					
					if(m.getModifiers() != Modifier.PUBLIC) continue;
					
					List<Pair<String, Class<?>>> paramTypes = new ArrayList<>();
					List<Pair<String, Object>> paramValues = new ArrayList<>();

					for(Parameter p: m.getParameters()) {
						
						if(p.getName().equals("gameName")) continue;
						
						String value = null;
						
						if(p.isAnnotationPresent(DefaultParameter.class)) {
							value = p.getAnnotation(DefaultParameter.class).value();
						}
						
						paramTypes.add(new Pair<String, Class<?>>(p.getName(), p.getType()));
						paramValues.add(new Pair<String, Object>(p.getName(), value));
						
					
					}
					
					TestMethod method = new TestMethod(m.getName(), paramTypes, paramValues);
					testClass.addMethod(method);
				}
				
				
			}
			
			
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		
		// need to became a method
		clear();

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);

		JButton runButton = new JButton("Run Tests");
		
		runButton.addActionListener(e -> 
							{
									saveSelectedTests();
									launchTests();
							});
		
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
		
		
		// creation test class section - STATIC
		for(TestClass testClass: testClasses) {
			createTestSection(staticTestPanel, testClass.getName(), testClass.getMethods().values());
		}

		//dynamic tests
		for(TestMethod s : dynMethods) {
			createTestSection(dynamicTestPanel, s.getName(), dynMethods);
		}

		testPanel.add(staticTestPanel);
		testPanel.add(dynamicTestPanel);
		
		super.scrollPane().setViewportView(mainPanel);
		super.scrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		super.scrollPane().validate();
		super.scrollPane().repaint();
		
		
	}
	
	// create the panel for tests - Static / Dynamic
	private JPanel createSectionTestPanel(String sectionName) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.add(new JLabel(sectionName));
       
        return sectionPanel;
    }
	
	// create row for test (checkbox + downslide window)
	private void createTestRow(JPanel parent, TestMethod method) {
		JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    rowPanel.setBackground(Color.WHITE);

	    JCheckBox checkBox = new JCheckBox(method.getName());
	    checkBox.setBackground(Color.WHITE);
	    
	    // Store parameters for this test
	    List<JTextField> paramFields = new ArrayList<>();
	    testCheckboxes.put(checkBox, paramFields);
	    
	    // If parameters exist, show a button to open the modal
	    if (!method.getParamTypes().isEmpty()) {
	        JButton paramButton = new JButton(" ⚙️");
	        paramButton.addActionListener(e -> openParameterModal(method, paramFields));

	        rowPanel.add(checkBox);
	        rowPanel.add(paramButton);
	    } else {
	        rowPanel.add(checkBox);
	    }

	    parent.add(rowPanel);
        
    }
	
	
	private void openParameterModal(TestMethod method, List<JTextField> paramFields) {
	    JDialog dialog = new JDialog();
	    dialog.setTitle("Enter Parameters for " + method.getName());
	    dialog.setSize(300, 200);
	    dialog.setModal(true);
	    
	    JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	    
	    paramFields.clear();
	    
	    List<String> parametersName = new ArrayList<String>();
	    
	    for(Pair<String, Class<?>> pair: method.getParamTypes()) {
	    	parametersName.add(pair.getFirst());
	    }
	    
	    for (String param : parametersName) {
	        JPanel paramRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
	        JLabel label = new JLabel(param);
	        JTextField textField = new JTextField(15);
	        paramFields.add(textField);
	        
	        paramRow.add(label);
	        paramRow.add(textField);
	        panel.add(paramRow);
	    }
	    
	    JButton submitButton = new JButton("Save");
	    submitButton.addActionListener(e -> {
	        dialog.dispose();
	    });

	    panel.add(submitButton);
	    dialog.add(panel);
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
	}

	
	private void createTestSection(JPanel parent, String category, Collection<TestMethod> methods) {
	    // Create section panel with minimal vertical space when collapsed
	    JPanel sectionPanel = new JPanel(new BorderLayout(0, 0));
	    sectionPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove all border padding
	    
	    // Create header panel
	    JPanel headerPanel = new JPanel(new BorderLayout(0, 0));
	    headerPanel.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5)); // Minimal vertical padding
	    
	    JLabel titleLabel = new JLabel(category);
	    JButton toggleButton = new JButton("▼");
	    toggleButton.setBorderPainted(false);
	    toggleButton.setFocusPainted(false);
	    toggleButton.setContentAreaFilled(false);
	    toggleButton.setMargin(new Insets(0, 0, 0, 0));
	    
	    headerPanel.add(titleLabel, BorderLayout.WEST);
	    headerPanel.add(toggleButton, BorderLayout.EAST);
	    
	    // Tests container
	    JPanel testsContainer = new JPanel();
	    testsContainer.setLayout(new BoxLayout(testsContainer, BoxLayout.Y_AXIS));
	    testsContainer.setVisible(false); // Initially collapsed
	    
	    // Add tests
	    for (TestMethod method : methods) {
	        createTestRow(testsContainer, method);
	    }
	    
	    // Toggle action
	    toggleButton.addActionListener(e -> {
	        boolean isVisible = testsContainer.isVisible();
	        testsContainer.setVisible(!isVisible);
	        toggleButton.setText(!isVisible ? "▲" : "▼");
	       
	        if (!isVisible) {
	            // When expanded, remove size constraints
	        	 int contentHeight = testsContainer.getPreferredSize().height;
	             int headerHeight = headerPanel.getPreferredSize().height;
	             sectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, headerHeight + contentHeight));	        
	             } else {
	            // When collapsed, constrain height to just the header
	            sectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, headerPanel.getPreferredSize().height));
	        }
	        
	        parent.revalidate();
	        parent.repaint();
	    });
	    
	    // Add components to section panel
	    sectionPanel.add(headerPanel, BorderLayout.NORTH);
	    sectionPanel.add(testsContainer, BorderLayout.CENTER);
	    
	    // IMPORTANT - set maximum size constraint initially (when collapsed)
	    sectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, headerPanel.getPreferredSize().height));
	    
	    // Add to parent
	    parent.add(sectionPanel);
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
	            System.out.println("text and value " + checkBox.getText() + " " + paramValues);
	        }
	    }
		
		
		
	}

	
	private void launchTests() {

	    TestLauncher launcher = new TestLauncher();
	    Map<String, Pair<String, String>> results = launcher.run(gameName, testsToLaunch);

	    for (Entry<JCheckBox, List<JTextField>> entry : testCheckboxes.entrySet()) {
	        JCheckBox checkBox = entry.getKey();
	        String testName = checkBox.getText();
	        
	        if (!checkBox.isSelected()) continue;

	        // Get the result for the test
	        Pair<String, String> testResult = results.get(testName);
	        String duration = testResult != null ? testResult.getFirst() : "N/A"; // Duration as String
	        String reason = testResult != null && testResult.getSecond() != null ? testResult.getSecond() : "null"; // Reason or "null" for passed tests

	        // test failed
	        if (reason != null && !reason.equals("null")) {
	            // Test failed
	            checkBox.setBackground(Color.RED);
		        checkBox.setToolTipText("Duration: " + duration + "ms \n Reason: " + reason);

	        } else {
	            // Test passed
	            checkBox.setBackground(Color.GREEN);
		        checkBox.setToolTipText("Duration: " + duration + "ms");

	        }

	        // Ensure the color is visible
	        checkBox.setOpaque(true);
	        checkBox.repaint();
	    }
	}




}
