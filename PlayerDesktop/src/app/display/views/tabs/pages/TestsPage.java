package app.display.views.tabs.pages;

import board.BoardTest;
import launcher.TestLauncher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
		
		
	}

	@Override
	public void reset() {
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

		JPanel staticPanel = new JPanel();
		staticPanel.setLayout(new BoxLayout(staticPanel, BoxLayout.Y_AXIS));
		staticPanel.setBackground(Color.WHITE);
		staticPanel.setOpaque(true); 

		staticPanel.add(new JLabel("Static"));
		JCheckBox staticCheck1 = new JCheckBox("Static Test 1");
		JCheckBox staticCheck2 = new JCheckBox("Static Test 2");
		JCheckBox staticCheck3 = new JCheckBox("Static Test 3");
		staticCheck1.setBackground(Color.WHITE);
		staticCheck2.setBackground(Color.WHITE);
		staticCheck3.setBackground(Color.WHITE);
		staticPanel.add(staticCheck1);
		staticPanel.add(staticCheck2);
		staticPanel.add(staticCheck3);

		
		JPanel dynamicPanel = new JPanel();
		dynamicPanel.setLayout(new BoxLayout(dynamicPanel, BoxLayout.Y_AXIS));
		dynamicPanel.setBackground(Color.WHITE);
		dynamicPanel.setOpaque(true);

		dynamicPanel.add(new JLabel("Dynamic"));
		JCheckBox dynamicCheck1 = new JCheckBox("Dynamic Test 1");
		JCheckBox dynamicCheck2 = new JCheckBox("Dynamic Test 2");
		JCheckBox dynamicCheck3 = new JCheckBox("Dynamic Test 3");
		dynamicCheck1.setBackground(Color.WHITE);
		dynamicCheck2.setBackground(Color.WHITE);
		dynamicCheck3.setBackground(Color.WHITE);
		dynamicPanel.add(dynamicCheck1);
		dynamicPanel.add(dynamicCheck2);
		dynamicPanel.add(dynamicCheck3);

		checkBoxPanel.add(staticPanel);
		checkBoxPanel.add(dynamicPanel);

		testsPanel.add(checkBoxPanel, BorderLayout.CENTER);
		testsPanel.add(buttonPanel, BorderLayout.SOUTH);

		super.scrollPane().setViewportView(testsPanel);
		super.scrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		super.scrollPane().validate();
		super.scrollPane().repaint();
		
		//BoardTest test = new BoardTest();
		//test.LineLessOrEqualThanBoardSide(2, "Tic-Tac-Toe.lud");
		
		//String game = "Tic-Tac-Toe.lud";
		//TestLauncher launcher = new TestLauncher();
		//launcher.run(game, 3);
		
	}


}
