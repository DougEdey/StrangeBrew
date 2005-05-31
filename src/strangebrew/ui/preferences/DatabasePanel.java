/*
 * Created on May 12, 2005
 */
package strangebrew.ui.preferences;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * @author zymurgist
 * 
 * The Database tab allows the user to select where to store the
 * database file and also sets the recipe sort order.
 */
public class DatabasePanel extends JPanel
{
	private JTextField m_databasePath = null;
	private JRadioButton[] m_options = null;
	private static final String[] optionText = {"Date", 
                                                "Alphabetical", 
                                                "As Entered", 
                                                "Reverse Entered"};
	
	public DatabasePanel()
	{
		initializeOptions();		
		add(BorderLayout.NORTH, layoutDatabasePanel());
		add(BorderLayout.CENTER, layoutSortOrderGroupBox());
	}

	private JPanel layoutDatabasePanel()
	{
		JPanel defaultDatabasePanel = new JPanel();
		setLayout(new BorderLayout());
		defaultDatabasePanel.setBorder(new TitledBorder("Default Database"));
		defaultDatabasePanel.setLayout(new BorderLayout());
		defaultDatabasePanel.add(BorderLayout.CENTER, 
				                 initializeDatabasePathField());
		defaultDatabasePanel.add(BorderLayout.EAST, 
				                 initializeBrowseButton());
		
		return defaultDatabasePanel;
	}
	
	private JPanel layoutSortOrderGroupBox()
	{
		JPanel helperPanel = new JPanel();
		helperPanel.setLayout(new BorderLayout());	
		JPanel sortOrderPanel = new JPanel();
		sortOrderPanel.setBorder(new TitledBorder("Recipe Sort Order"));
		sortOrderPanel.setLayout(new GridLayout(2, 2));
		
		for (int i = 0; i < m_options.length; i++)
		{
			sortOrderPanel.add(m_options[i]);
		}
		
		helperPanel.add(BorderLayout.NORTH, sortOrderPanel);
		return helperPanel;		
	}
	
	private JTextField initializeDatabasePathField()
	{
		m_databasePath = new JTextField();
		m_databasePath.setText("");
		return m_databasePath;
	}
	
	private JButton initializeBrowseButton()
	{
		final JButton browse = new JButton("Browse");
		
		browse.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser files = new JFileChooser();
				if (JFileChooser.APPROVE_OPTION == files.showOpenDialog(browse))
				{					
					System.out.println("User selected: " + files.getSelectedFile());
					m_databasePath.setText(files.getSelectedFile().toString());
				}
				else
				{
					System.out.println("User canceled file open.");
				}
			}
		});
		
		return browse;
	}	
		
	private void initializeOptions()
	{
		m_options = new JRadioButton[optionText.length];
		ButtonGroup group = new ButtonGroup();
		
		for(int i = 0; i < optionText.length; i++)
		{
			m_options[i] = new JRadioButton(optionText[i]);
			group.add(m_options[i]);
		}
	}	
}