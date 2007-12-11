package ca.strangebrew.ui.swing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ca.strangebrew.Recipe;
import ca.strangebrew.SBStringUtils;
import ca.strangebrew.Style;
import ca.strangebrew.Yeast;

public class DetailsPanel extends javax.swing.JPanel implements ActionListener, FocusListener {

	private Recipe myRecipe;
	private StrangeSwing.SBNotifier sbn;
	private JLabel lblBrewer;
	private JTextField brewerNameText;
	private JLabel lblDate;
	private JLabel lblStyle;
	private JLabel lblYeast;
	private JLabel lblPreBoil;
	private JLabel lblPostBoil;
	private JLabel lblEffic;
	private JLabel lblAtten;
	private JLabel lblOG;
	private JLabel lblFG;
	private JLabel lblIBU;
	private JLabel lblAlc;
	private JLabel lblColour;
	private JFormattedTextField txtDate;
	private JComboBox cmbStyle;
	private ComboModel cmbStyleModel;
	private JFormattedTextField txtPreBoil;
	private JTextField postBoilText;
	private JSpinner spnEffic;
	private JSpinner spnAtten;
	private JSpinner spnOG;
	private JSpinner spnFG;
	private JLabel lblIBUvalue;
	private JLabel lblAlcValue;
	private JLabel lblColourValue;
	private JScrollPane scpComments;
	private JTextArea txtComments;
	private ComboModel cmbYeastModel;
	private JComboBox cmbYeast;
	private JComboBox cmbSizeUnits;
	private JLabel lblSizeUnits;
	private JLabel boilTimeLable;
	private DefaultComboBoxModel colourMethodComboModel;
	private JComboBox colourMethodCombo;
	private DefaultComboBoxModel ibuMethodComboModel;
	private JComboBox ibuMethodCombo;
	private JComboBox alcMethodCombo;
	private DefaultComboBoxModel alcMethodComboModel;
	private JTextField evapText;
	private JComboBox evapMethodCombo;
	private JLabel evapLabel;
	private JTextField boilMinText;
	private ComboBoxModel cmbSizeUnitsModel;
	private JLabel lblComments;

	public DetailsPanel() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));

			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1,
					0.1};
			thisLayout.columnWidths = new int[]{7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
			thisLayout.rowWeights = new double[]{0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[]{7, 7, 7, 7, 7, 7, 7};
			this.setLayout(thisLayout);
			{
				lblBrewer = new JLabel();
				this.add(lblBrewer, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblBrewer.setText("Brewer:");
			}
			{
				brewerNameText = new JTextField();
				this.add(brewerNameText, new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				brewerNameText.addFocusListener(this);
				brewerNameText.addActionListener(this);
				brewerNameText.setText("Brewer");

			}
			{
				lblDate = new JLabel();
				this.add(lblDate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblDate.setText("Date:");
			}
			{
				lblStyle = new JLabel();
				this.add(lblStyle, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblStyle.setText("Style:");
			}
			{
				lblYeast = new JLabel();
				this.add(lblYeast, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblYeast.setText("Yeast:");
			}
			{
				lblPreBoil = new JLabel();
				this.add(lblPreBoil, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblPreBoil.setText("Pre boil:");
			}
			{
				lblPostBoil = new JLabel();
				this.add(lblPostBoil, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblPostBoil.setText("Post boil:");
			}
			{
				lblEffic = new JLabel();
				this.add(lblEffic, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblEffic.setText("Effic:");
			}
			{
				lblAtten = new JLabel();
				this.add(lblAtten, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblAtten.setText("Atten:");
			}
			{
				lblOG = new JLabel();
				this.add(lblOG, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblOG.setText("OG:");
			}
			{
				lblFG = new JLabel();
				this.add(lblFG, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblFG.setText("FG:");
			}
			{
				lblIBU = new JLabel();
				this.add(lblIBU, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblIBU.setText("IBU:");
			}
			{
				lblAlc = new JLabel();
				this.add(lblAlc, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblAlc.setText("%Alc:");
			}
			{
				lblColour = new JLabel();
				this.add(lblColour, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblColour.setText("Colour:");
			}
			{
				txtDate = new JFormattedTextField();
				this.add(txtDate, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				txtDate.setText("Date");
				txtDate.addFocusListener(this);
				txtDate.addActionListener(this);
			}
			{
				cmbStyleModel = new ComboModel();
				cmbStyle = new JComboBox();
				SmartComboBox.enable(cmbStyle);
				this.add(cmbStyle, new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				cmbStyle.setModel(cmbStyleModel);
				cmbStyle.setMaximumSize(new java.awt.Dimension(100, 32767));

				cmbStyle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						Style s = (Style) cmbStyleModel.getSelectedItem();
						if (myRecipe != null && s != myRecipe.getStyleObj()) {
							myRecipe.setStyle(s);
							sbn.setStyle(s);
						}

						cmbStyle.setToolTipText(SBStringUtils.multiLineToolTip(50, s
								.getDescription()));

					}
				});
			}
			{
				txtPreBoil = new JFormattedTextField();
				this.add(txtPreBoil, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
								0), 0, 0));
				txtPreBoil.setText("Pre Boil");
				txtPreBoil.addFocusListener(this);
				txtPreBoil.addActionListener(this);
			}
			{
				postBoilText = new JFormattedTextField();
				this.add(postBoilText, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0,
								0), 0, 0));
				postBoilText.setText("Post Boil");
				postBoilText.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent evt) {
						myRecipe.setPostBoil(Double.parseDouble(postBoilText.getText().toString()));
						sbn.displRecipe();
					}
				});
				postBoilText.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						myRecipe.setPostBoil(Double.parseDouble(postBoilText.getText().toString()));
						sbn.displRecipe();
					}
				});
			}
			{
				lblComments = new JLabel();
				this.add(lblComments, new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblComments.setText("Comments:");
			}

			{
				SpinnerNumberModel spnEfficModel = new SpinnerNumberModel(75.0, 0.0, 100.0, 1.0);
				spnEffic = new JSpinner();
				this.add(spnEffic, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				spnEffic.setModel(spnEfficModel);
				spnEffic.setMaximumSize(new java.awt.Dimension(70, 32767));
				spnEffic.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						myRecipe.setEfficiency(Double.parseDouble(spnEffic.getValue().toString()));
						sbn.displRecipe();
					}
				});
				spnEffic.setEditor(new JSpinner.NumberEditor(spnEffic, "00.#"));
				spnEffic.getEditor().setPreferredSize(new java.awt.Dimension(28, 16));
			}
			{
				SpinnerNumberModel spnAttenModel = new SpinnerNumberModel(75.0, 0.0, 100.0, 1.0);
				spnAtten = new JSpinner();
				this.add(spnAtten, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				spnAtten.setModel(spnAttenModel);
				spnAtten.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						myRecipe.setAttenuation(Double.parseDouble(spnAtten.getValue().toString()));
						sbn.displRecipe();
					}
				});
				spnAtten.setEditor(new JSpinner.NumberEditor(spnAtten, "00.#"));
			}
			{
				SpinnerNumberModel spnOgModel = new SpinnerNumberModel(1.000, 0.900, 2.000, 0.001);
				spnOG = new JSpinner();
				this.add(spnOG, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				spnOG.setModel(spnOgModel);
				spnOG.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						myRecipe.setEstOg(Double.parseDouble(spnOG.getValue().toString()));
						sbn.displRecipe();
					}
				});
				spnOG.setEditor(new JSpinner.NumberEditor(spnOG, "0.000"));
				spnOG.getEditor().setPreferredSize(new java.awt.Dimension(20, 16));
			}
			{
				SpinnerNumberModel spnFgModel = new SpinnerNumberModel(1.000, 0.900, 2.000, 0.001);
				spnFG = new JSpinner();
				this.add(spnFG, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				spnFG.setModel(spnFgModel);
				spnFG.setEditor(new JSpinner.NumberEditor(spnFG, "0.000"));
				spnFG.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent evt) {
						// set the new FG, and update alc:
						myRecipe.setEstFg(Double.parseDouble(spnFG.getValue().toString()));
						sbn.displRecipe();
					}
				});
			}
			{
				lblIBUvalue = new JLabel();
				this.add(lblIBUvalue, new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblIBUvalue.setText("IBUs");
			}
			{
				lblColourValue = new JLabel();
				this.add(lblColourValue, new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblColourValue.setText("Colour");
			}
			{
				lblAlcValue = new JLabel();
				this.add(lblAlcValue, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				lblAlcValue.setText("Alc");
			}
			{
				scpComments = new JScrollPane();
				this.add(scpComments, new GridBagConstraints(7, 4, 3, 2, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0),
						0, 0));
				{
					txtComments = new JTextArea();
					scpComments.setViewportView(txtComments);
					txtComments.setText("Comments");
					txtComments.setWrapStyleWord(true);
					// txtComments.setPreferredSize(new
					// java.awt.Dimension(117, 42));
					txtComments.setLineWrap(true);
					txtComments.addFocusListener(new FocusAdapter() {
						public void focusLost(FocusEvent evt) {
							if (!txtComments.getText().equals(myRecipe.getComments())) {
								myRecipe.setComments(txtComments.getText());
							}
						}
					});
				}
			}
			{
				cmbYeastModel = new ComboModel();
				cmbYeast = new JComboBox();
				SmartComboBox.enable(cmbYeast);
				this.add(cmbYeast, new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				cmbYeast.setModel(cmbYeastModel);
				cmbYeast.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						Yeast y = (Yeast) cmbYeastModel.getSelectedItem();
						if (myRecipe != null && y != myRecipe.getYeastObj()) {
							myRecipe.setYeast(y);
						}
						String st = SBStringUtils.multiLineToolTip(40, y.getDescription());

						cmbYeast.setToolTipText(st);
					}
				});
			}
			{
				cmbSizeUnitsModel = new ComboModel();
				cmbSizeUnits = new JComboBox();
				SmartComboBox.enable(cmbSizeUnits);
				this.add(cmbSizeUnits, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				cmbSizeUnits.setModel(cmbSizeUnitsModel);
				cmbSizeUnits.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						String q = (String) cmbSizeUnits.getSelectedItem();
						if (myRecipe != null && q != myRecipe.getVolUnits()) {
							// also sets postboil units:
							myRecipe.setVolUnits(q);
							sbn.displRecipe();
						}
					}
				});
			}
			{
				lblSizeUnits = new JLabel();
				this.add(lblSizeUnits, new GridBagConstraints(2, 5, 2, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				lblSizeUnits.setText("Size Units");
			}
			{
				boilTimeLable = new JLabel();
				this.add(boilTimeLable, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				boilTimeLable.setText("Boil Min:");
			}
			{
				evapLabel = new JLabel();
				this.add(evapLabel, new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				evapLabel.setText("Evap/hr:");
			}
			{
				boilMinText = new JTextField();
				this.add(boilMinText, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				boilMinText.setText("60");
				boilMinText.addFocusListener(this);
				boilMinText.addActionListener(this);
			}
			{
				evapText = new JTextField();
				this.add(evapText, new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
								0, 0), 0, 0));
				evapText.setText("4");
				evapText.addFocusListener(this);
				evapText.addActionListener(this);
			}
			{
				alcMethodComboModel = new DefaultComboBoxModel(new String[]{"Volume", "Weight"});
				alcMethodCombo = new JComboBox(alcMethodComboModel);
				SmartComboBox.enable(alcMethodCombo);
				this.add(alcMethodCombo, new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				alcMethodCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						recipeSettingsActionPerformed(evt);
					}
				});
			}
			{
				ibuMethodComboModel = new DefaultComboBoxModel(new String[]{"Tinseth", "Garetz",
				"Rager"});
				ibuMethodCombo = new JComboBox(ibuMethodComboModel);
				SmartComboBox.enable(ibuMethodCombo);

				this.add(ibuMethodCombo, new GridBagConstraints(9, 1, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				ibuMethodCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						recipeSettingsActionPerformed(evt);
					}
				});
			}
			{
				colourMethodComboModel = new DefaultComboBoxModel(new String[]{"SRM", "EBC"});
				colourMethodCombo = new JComboBox(colourMethodComboModel);
				SmartComboBox.enable(colourMethodCombo);
				this.add(colourMethodCombo, new GridBagConstraints(9, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0),
						0, 0));
				colourMethodCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						recipeSettingsActionPerformed(evt);
					}
				});
			}

			ComboBoxModel evapMethodComboModel = new DefaultComboBoxModel(new String[]{"Constant",
			"Percent"});

			evapMethodCombo = new JComboBox();
			SmartComboBox.enable(evapMethodCombo);
			this.add(evapMethodCombo, new GridBagConstraints(6, 5, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0,
					0));
			evapMethodCombo.setModel(evapMethodComboModel);
			evapMethodCombo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					recipeSettingsActionPerformed(evt);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		String s = "";
        // String t = "";
        
                
		s = ((JTextField) o).getText();
        // t = s.replace(',','.'); // accept also european decimal komma

		if (o == brewerNameText)
			myRecipe.setBrewer(s);
		else if (o == txtPreBoil) {
			myRecipe.setPreBoil(Double.parseDouble(s));
			sbn.displRecipe();
		} else if (o == postBoilText) {
			myRecipe.setPostBoil(Double.parseDouble(s));
			sbn.displRecipe();
		} else if (o == evapText) {
			myRecipe.setEvap(Double.parseDouble(s));
			sbn.displRecipe();
		} else if (o == boilMinText) {
                        if( s.indexOf('.') > 0)
                        {   // parseInt doesn't like '.' or ',', so trim the string
                            s = s.substring(0,s.indexOf('.'));
                        }
			myRecipe.setBoilMinutes(Integer.parseInt(s));
			sbn.displRecipe();
		}

	}
	
	private void recipeSettingsActionPerformed(ActionEvent evt) {
		Object o = evt.getSource();
		String s = (String) ((JComboBox) o).getSelectedItem();

		if (o == alcMethodCombo)
			myRecipe.setAlcMethod(s);					
		else if (o == ibuMethodCombo)
			myRecipe.setIBUMethod(s);
		else if (o == colourMethodCombo)
			myRecipe.setColourMethod(s);
		else if (o == evapMethodCombo)
			myRecipe.setEvapMethod(s);

		sbn.displRecipe();
	}

	public void focusLost(FocusEvent e) {
		Object o = e.getSource();
		ActionEvent evt = new ActionEvent(o, 1, "");
		actionPerformed(evt);
	}

	public void focusGained(FocusEvent e) {
		// do nothing, we don't need this event
	}
}
