/*
 * Created on May 12, 2005
 */
package strangebrew.ui.preferences;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * @author zymurgist
 */
public class CalculationsPanel extends JPanel
{
	private JPanel pnlHopsCalc;
	private ButtonGroup bgHopsCalc;
	private JRadioButton rbTinseth;
	private JRadioButton rbGaretz;
	private JPanel pnlHops;
	private JTextField txtPellet;
	private JLabel jLabel2;
	private JTextField txtTinsethUtil;
	private JLabel jLabel4;
	private JTextField txtFWHTime;
	private JTextField txtMashHopTime;
	private JLabel jLabel5;
	private JTextField txtDryHopTime;
	private JLabel jLabel3;
	private JPanel pnlHopTimes;
	private JRadioButton rbABW;
	private JRadioButton rbABV;
	private JPanel pnlAlc;
	private JLabel jLabel1;
	private JRadioButton rbHBU;
	private JRadioButton rbRager;
	
	public CalculationsPanel () {
		initGUI();
	}

	private void initGUI() {
		try {
			{
				GridBagLayout thisLayout = new GridBagLayout();
				thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1};
				thisLayout.rowHeights = new int[] {7, 7, 7};
				thisLayout.columnWeights = new double[] {0.1, 0.1};
				thisLayout.columnWidths = new int[] {7, 7};
				this.setLayout(thisLayout);
				this.setPreferredSize(new java.awt.Dimension(524, 372));
				{
					{
						bgHopsCalc = new ButtonGroup();
						{
							pnlHops = new JPanel();
							GridLayout pnlHopsLayout = new GridLayout(1, 2);
							pnlHopsLayout.setColumns(2);
							pnlHopsLayout.setHgap(5);
							pnlHopsLayout.setVgap(5);
							pnlHops.setLayout(pnlHopsLayout);
							this.add(pnlHops, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							pnlHops.setBorder(BorderFactory.createTitledBorder("Hops:"));
							{
								jLabel1 = new JLabel();
								pnlHops.add(jLabel1);
								jLabel1.setText("Pellet Hops +%");
							}
							{
								txtPellet = new JTextField();
								pnlHops.add(txtPellet);
								txtPellet.setPreferredSize(new java.awt.Dimension(20, 20));
							}
							{
								jLabel2 = new JLabel();
								pnlHops.add(jLabel2);
								jLabel2.setText("Tinseth Utilization Factor");
							}
							{
								txtTinsethUtil = new JTextField();
								pnlHops.add(txtTinsethUtil);
								txtTinsethUtil.setText("4.15");
							}
						}
						{
							pnlAlc = new JPanel();
							GridLayout pnlAlcLayout = new GridLayout(2, 1);
							pnlAlcLayout.setColumns(1);
							pnlAlcLayout.setHgap(5);
							pnlAlcLayout.setVgap(5);
							pnlAlcLayout.setRows(2);
							pnlAlc.setLayout(pnlAlcLayout);
							this.add(pnlAlc, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							pnlAlc.setBorder(BorderFactory.createTitledBorder("Alcohol By:"));
							{
								rbABV = new JRadioButton();
								pnlAlc.add(rbABV);
								rbABV.setText("Volume");
							}
							{
								rbABW = new JRadioButton();
								pnlAlc.add(rbABW);
								rbABW.setText("Weight");
							}
						}
						{
							pnlHopTimes = new JPanel();
							GridLayout pnlHopTimesLayout = new GridLayout(3, 2);
							pnlHopTimesLayout.setColumns(2);
							pnlHopTimesLayout.setHgap(5);
							pnlHopTimesLayout.setVgap(5);
							pnlHopTimesLayout.setRows(3);
							pnlHopTimes.setLayout(pnlHopTimesLayout);
							this.add(pnlHopTimes, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
							pnlHopTimes.setBorder(BorderFactory.createTitledBorder("Hop Times:"));
							{
								jLabel3 = new JLabel();
								pnlHopTimes.add(jLabel3);
								jLabel3.setText("Dry (min):");
							}
							{
								txtDryHopTime = new JTextField();
								pnlHopTimes.add(txtDryHopTime);
								txtDryHopTime.setText("0.0");
							}
							{
								jLabel4 = new JLabel();
								pnlHopTimes.add(jLabel4);
								jLabel4.setText("FWH, boil minus (min):");
							}
							{
								txtFWHTime = new JTextField();
								pnlHopTimes.add(txtFWHTime);
								txtFWHTime.setText("20.0");
							}
							{
								jLabel5 = new JLabel();
								pnlHopTimes.add(jLabel5);
								jLabel5.setText("Mash Hop (min):");
							}
							{
								txtMashHopTime = new JTextField();
								pnlHopTimes.add(txtMashHopTime);
								txtMashHopTime.setText("2.0");
							}
						}
					}
					pnlHopsCalc = new JPanel();
					FlowLayout pnlHopsCalcLayout = new FlowLayout();
					pnlHopsCalc.setLayout(pnlHopsCalcLayout);
					this.add(pnlHopsCalc, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					pnlHopsCalc.setPreferredSize(new java.awt.Dimension(117, 107));
					pnlHopsCalc.setBorder(BorderFactory.createTitledBorder("IBU Calc Method:"));
					{
						rbTinseth = new JRadioButton();
						pnlHopsCalc.add(rbTinseth);
						rbTinseth.setText("Tinseth");
					}
					{
						rbRager = new JRadioButton();
						pnlHopsCalc.add(rbRager);
						rbRager.setText("Rager");
					}
					{
						rbGaretz = new JRadioButton();
						pnlHopsCalc.add(rbGaretz);
						rbGaretz.setText("Garetz");
					}
					{
						rbHBU = new JRadioButton();
						pnlHopsCalc.add(rbHBU);
						rbHBU.setText("HBU");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
