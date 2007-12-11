package ca.strangebrew.ui.swing.dialogs;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.sf.wraplog.AbstractLogger;
import net.sf.wraplog.SystemLogger;
import ca.strangebrew.Debug;
import ca.strangebrew.ui.swing.SmartComboBox;
import ca.strangebrew.ui.swing.StrangeSwing;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;


public class PrintDialog extends javax.swing.JDialog implements ActionListener {
	private JPanel jPanel1;
	private JComboBox fontCombo;
	private ComboBoxModel fontComboModel;
	private JButton prevButton;
	private JButton cancelButton;
	private JPanel jPanel2;
	private JSpinner sizeSpin;
	private JLabel jLabel3;
	private JLabel jLabel1;
	// private Recipe myRecipe;
	private StrangeSwing app;
	

	public PrintDialog(JFrame frame) {
		super(frame);
		app = (StrangeSwing) frame;
		// myRecipe = app.myRecipe;
		initGUI();
	}

	private void initGUI() {
		try {

			this.setTitle("Print Settings");

			jPanel1 = new JPanel();
			GridBagLayout jPanel1Layout = new GridBagLayout();
			jPanel1Layout.rowWeights = new double[]{0.1, 0.1};
			jPanel1Layout.rowHeights = new int[]{7, 7};
			jPanel1Layout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1};
			jPanel1Layout.columnWidths = new int[]{7, 7, 7, 7};
			jPanel1.setLayout(jPanel1Layout);
			getContentPane().add(jPanel1, BorderLayout.CENTER);

			jPanel2 = new JPanel();
			getContentPane().add(jPanel2, BorderLayout.SOUTH);

			cancelButton = new JButton();
			jPanel2.add(cancelButton);
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(this);

			prevButton = new JButton();
			jPanel2.add(prevButton);
			prevButton.setText("Preview");
			prevButton.addActionListener(this);

			jLabel1 = new JLabel();
			jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			jLabel1.setText("Font:");

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			String[] fontNames = ge.getAvailableFontFamilyNames();

			fontComboModel = new DefaultComboBoxModel(fontNames);
			fontComboModel.setSelectedItem("Dialog");

			fontCombo = new JComboBox();
			SmartComboBox.enable(fontCombo);
			jPanel1.add(fontCombo, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			fontCombo.setModel(fontComboModel);
			fontCombo.setPreferredSize(new java.awt.Dimension(136, 20));

			ComboBoxModel bodyFontComboModel = new DefaultComboBoxModel(fontNames);
			bodyFontComboModel.setSelectedItem("Dialog");

			jLabel3 = new JLabel();
			jPanel1.add(jLabel3, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			jLabel3.setText("Size:");

			SpinnerNumberModel headSizeSpinModel = new SpinnerNumberModel(12, 8, 72, 1);

			sizeSpin = new JSpinner();
			jPanel1.add(sizeSpin, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			sizeSpin.setModel(headSizeSpinModel);
			sizeSpin.setPreferredSize(new java.awt.Dimension(56, 20));

			this.setSize(400, 172);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent ev) {

		Object o = ev.getSource();
		String font = fontComboModel.getSelectedItem().toString();
		String fontSize = sizeSpin.getValue().toString();
		String printOptions = "  <PRINT FONTSIZE=\"" + fontSize + "pt\" FONTFACE=\"" + font + "\"/>\n";

		if (o == prevButton) {
			File file = new File("print.html");
			try {
				app.saveAsHTML(file, "recipeToHtml.xslt", printOptions);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			String urlString;
			try {
				urlString = "file://" + file.getCanonicalPath();

				Debug.print(urlString);
				AbstractLogger logger = new SystemLogger();
				BrowserLauncher launcher;
				launcher = new BrowserLauncher(logger);
				BrowserLauncherRunner runner = new BrowserLauncherRunner(launcher, urlString, null);
				Thread launcherThread = new Thread(runner);
				launcherThread.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			} catch (BrowserLaunchingInitializingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedOperatingSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		setVisible(false);
		dispose();
	}

}
