/*
 * Created on May 12, 2005
 */
package strangebrew.ui.preferences;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import strangebrew.Options;

/**
 * @author zymurgist
 * 
 * This class creates a tabbed dialog box with all the preferences
 * used by the application.
 */
public class PreferencesDialog extends JDialog
{
	private JTabbedPane tabs = null;
	private boolean m_savePreferences = false;
	
	PreferencesDialog(Dialog owner, Options settings)
	{
		super(owner, "Recipe Preferences", true);
		layoutUi();
	}

	PreferencesDialog(Frame owner, Options settings)
	{
		super(owner, "Recipe Preferences", true);
		layoutUi();
	}

	void layoutUi()
	{
		tabs = new JTabbedPane();
		tabs.addTab("New Recipe Defaults", new RecipeDefaultsPanel());
		tabs.addTab("Calculations", new CalculationsPanel());
		tabs.addTab("Appearance", new AppearancePanel());
		tabs.addTab("Time Defaults", new TimeDefaultsPanel());
		tabs.addTab("Database", new DatabasePanel());
		tabs.addTab("Cost and Carb Defaults", new CostCarbDefaultsPanel());
		tabs.addTab("Brewer", new BrewerPanel());
		
		JPanel buttons = new JPanel();
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttons.add(cancelButton);
		buttons.add(okButton);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.CENTER, tabs);
		getContentPane().add(BorderLayout.SOUTH, buttons);
		
		setSize(400, 400);
		
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.out.println("ok");
				m_savePreferences = true;
				hide();
			}
		});
		
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.out.println("cancel");
				m_savePreferences = false;
				hide();
			}
		});
	}
	
	public void show()
	{
		throw new RuntimeException("Call showPreferences() instead.");
	}
	
	public boolean showPreferences()
	{
		System.out.println("showPreferences 1");
		super.show();
		System.out.println("showPreferences 2");
		return m_savePreferences;
	}
}
