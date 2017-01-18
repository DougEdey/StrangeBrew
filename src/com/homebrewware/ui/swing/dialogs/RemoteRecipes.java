/**
 *    Filename: RemoteRecipes.java
 *     Version: 0.9.0
 * Description: Remote Recipes
 *     License: GPLv2
 *        Date: 2017-01-14
 *
 * Copyright (c) 2005 Drew Avis
 * @author Drew Avis
 *
 * Copyright (c) 2017 Dallas Fletchall
 * @author Dallas Fletchall
 */

/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package com.homebrewware.ui.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.homebrewware.Debug;
import com.homebrewware.OpenImport;
import com.homebrewware.Options;
import com.homebrewware.Product;
import com.homebrewware.Recipe;
import com.homebrewware.RecipeURLPopup;
import com.homebrewware.StringUtils;
import com.homebrewware.ui.swing.ComboModel;
import com.homebrewware.ui.swing.StrangeSwing;

public class RemoteRecipes extends javax.swing.JDialog implements ActionListener {
    private JPanel findPanel;
    private JButton browseButton;
    private JTextField dirLocationText;
    private JPanel browsePanel;
    private JButton cancelButton;
    private JButton openButton;
    private JPanel buttonPanel;
    private JTable recipeTable;
    private JScrollPane recipeScrollPane;
    private FindTableModel recipeTableModel;
    private Options opt;
    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private JComboBox styles = new JComboBox();
    private ComboModel styleModel = new ComboModel();
    private List<BasicRecipe> recipes = new ArrayList<BasicRecipe>();
    private List<File> files = new ArrayList<File>();
    private File currentDir;
    private boolean close = false;

    public RemoteRecipes(JFrame frame) {
        super(frame);


        String recipeDir = "";
        opt = Options.getInstance();
        if(opt.getProperty("optRecipe") != null)
            recipeDir = opt.getProperty("optRecipe");

        if(recipeDir.equalsIgnoreCase("") ) {
            recipeDir = Product.getInstance().getAppPath(Product.Path.RECIPE);
        }
        currentDir = new File(recipeDir);

        initGUI();
        dirLocationText.setText(currentDir.getAbsolutePath());
        this.pack();
        setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);

    }

    private void initGUI() {
        try {
            BoxLayout thisLayout = new BoxLayout(this.getContentPane(), javax.swing.BoxLayout.Y_AXIS);
            /*thisLayout.columnWeights = new double[]{0.1};
            thisLayout.columnWidths = new int[]{7};
            thisLayout.rowWeights = new double[]{0.7, 0.1, 0.1};
            thisLayout.rowHeights = new int[]{7, 7, 7};*/
            this.getContentPane().setLayout(thisLayout);

            this.getContentPane().add(jTabbedPane1);
            {

                findPanel = new JPanel(new GridBagLayout());


                jTabbedPane1.addTab("Browse", null, findPanel, "Browse Recipes");
/*
                        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH,
                                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));*/
                BoxLayout findPanelLayout = new BoxLayout(findPanel, javax.swing.BoxLayout.Y_AXIS);
                //findPanelLayout.layoutContainer(thisLayout);
                findPanel.setLayout(findPanelLayout);
                //findPanel.setPreferredSize(new java.awt.Dimension(392, 269));
                {

                    styleModel.setList(getListOfStyles());
                    styles.setModel(styleModel);
                    styles.addActionListener(this);

                    findPanel.add(styles);

                    recipeScrollPane = new JScrollPane();
                    findPanel.add(recipeScrollPane);
                    //recipeScrollPane.setPreferredSize(new java.awt.Dimension(152, 75));
                    {
                        recipeTableModel = new FindTableModel();
                        recipeTable = new JTable();
                        recipeScrollPane.setViewportView(recipeTable);
                        recipeTable.setModel(recipeTableModel);
                        recipeTable.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseReleased(MouseEvent e) {
                                int r = recipeTable.rowAtPoint(e.getPoint());
                                if (r >= 0 && r < recipeTable.getRowCount()) {
                                    recipeTable.setRowSelectionInterval(r, r);
                                } else {
                                    recipeTable.clearSelection();
                                }

                                int rowindex = recipeTable.getSelectedRow();
                                if (rowindex < 0)
                                    return;
                                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                                    createRecipeUrl(recipes.get(recipeTable.getSelectedRow()));
                                }
                            }
                            @Override
                            public void mousePressed(MouseEvent e) {
                                int r = recipeTable.rowAtPoint(e.getPoint());
                                if (r >= 0 && r < recipeTable.getRowCount()) {
                                    recipeTable.setRowSelectionInterval(r, r);
                                } else {
                                    recipeTable.clearSelection();
                                }

                                int rowindex = recipeTable.getSelectedRow();
                                if (rowindex < 0)
                                    return;
                                if (e.isPopupTrigger() && e.getComponent() instanceof JTable ) {
                                    createRecipeUrl(recipes.get(recipeTable.getSelectedRow()));
                                }
                            }
                        });
                        // recipeTable.setPreferredSize(new
                        // java.awt.Dimension(148, 32));
                    }
                }
            }
            {
                browsePanel = new JPanel();
                this.getContentPane().add(
                        browsePanel,
                        new GridBagConstraints(0, 1, 1, 1, 0.0, 0., GridBagConstraints.CENTER,
                                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                BorderLayout browsePanelLayout = new BorderLayout();

                browsePanel.setLayout(browsePanelLayout);
                browsePanel.setBorder(BorderFactory.createTitledBorder("Directory"));
                browsePanel.setPreferredSize(new java.awt.Dimension(392, this.getFontMetrics(this.getFont()).getHeight()*5));
                {
                    dirLocationText = new JTextField();

                    browsePanel.add(dirLocationText, BorderLayout.CENTER);
                    dirLocationText.setText("jTextField1");
                }
                {
                    browseButton = new JButton();
                    browsePanel.add(browseButton, BorderLayout.EAST);
                    browseButton.setText("Browse...");
                    browseButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            Component owner = browseButton;
                            JFileChooser chooser = new JFileChooser();
                            chooser.setCurrentDirectory(new java.io.File("."));
                            chooser.setDialogTitle("Select directory");
                            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                            if (chooser.showOpenDialog(owner) == JFileChooser.APPROVE_OPTION) {
                                Debug.print("getCurrentDirectory(): "
                                        + chooser.getCurrentDirectory());
                                Debug.print("getSelectedFile() : " + chooser.getSelectedFile());
                                currentDir = chooser.getSelectedFile();
                                dirLocationText.setText(currentDir.getAbsolutePath());
                            } else {
                                Debug.print("No Selection ");
                            }
                        }
                    });
                }
            }
            {
                buttonPanel = new JPanel();
                this.getContentPane().add(
                        buttonPanel,
                        new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                                GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                FlowLayout buttonPanelLayout = new FlowLayout();
                buttonPanelLayout.setAlignment(FlowLayout.RIGHT);
                buttonPanel.setLayout(buttonPanelLayout);
                {
                    openButton = new JButton();
                    buttonPanel.add(openButton);
                    openButton.setText("Open");
                    openButton.addActionListener(this);
                }
                {
                    cancelButton = new JButton();
                    buttonPanel.add(cancelButton);
                    cancelButton.setText("Cancel");
                    cancelButton.addActionListener(this);
                }
            }
            this.setSize(400, 428);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRecipeUrl(BasicRecipe r) {
        this.setModalityType(null);
        RecipeURLPopup rr = new RecipeURLPopup(r.id, r.brewer, r.title);
        rr.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        rr.setVisible(false);
        rr.setVisible(true);
    }

    private void loadRecipesByStyle(String style) {

        try
        {

            String baseURL = Options.getInstance().getProperty("cloudURL");

            //URL url = new URL(baseURL+"/style/" + style);
            URI rURI = new URI("http", null, baseURL, 80, "/styles/"+style, null, null);
            URL url = rURI.toURL();

            InputStream response = url.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setValidating(false);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            // dbf.setCoalescing(true);
            // dbf.setExpandEntityReferences(true);

            DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();
            //db.setEntityResolver(new NullResolver());

            // db.setErrorHandler( new MyErrorHandler());

            Document readXML = db.parse(response);


            NodeList childNodes = readXML.getElementsByTagName("recipe");


            Debug.print("Loading recipes from online: "+ childNodes.getLength());
            for(int x = 0; x < childNodes.getLength(); x++ ) {


                Node child = childNodes.item(x);

                NamedNodeMap childAttr = child.getAttributes();


                // generate the recipe list

                long ID = Long.parseLong(childAttr.getNamedItem("id").getNodeValue() );
                String Brewer = childAttr.getNamedItem("brewer").getNodeValue().toString();
                String Title = childAttr.getNamedItem("name").getNodeValue().toString();
                String Style = childAttr.getNamedItem("style").getNodeValue().toString();
                int iteration = 0;//Integer.parseInt(childAttr.getNamedItem("iteration").getNodeValue());
                Debug.print("Loading: " + Title);
                BasicRecipe rRecipe = new BasicRecipe(ID, Brewer, Style, Title, iteration);
                recipes.add(rRecipe);
            }

            recipeTableModel.setData(recipes);
            recipeTable.updateUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }



    }

    private void loadRecipesByBrewer(String brewer) {

        try
        {

            String baseURL = Options.getInstance().getProperty("cloudURL");
            URI rURI = new URI("http", null, baseURL, 80, "/brewer/"+brewer, null, null);
            URL url = rURI.toURL();
            InputStream response = url.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setValidating(false);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            // dbf.setCoalescing(true);
            // dbf.setExpandEntityReferences(true);

            DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();
            //db.setEntityResolver(new NullResolver());

            // db.setErrorHandler( new MyErrorHandler());

            Document readXML = db.parse(response);


            NodeList childNodes = readXML.getElementsByTagName("recipe");


            Debug.print("Loading recipes from online: "+ childNodes.getLength());
            for(int x = 0; x < childNodes.getLength(); x++ ) {


                Node child = childNodes.item(x);

                NamedNodeMap childAttr = child.getAttributes();


                // generate the recipe list

                long ID = Long.parseLong( childAttr.getNamedItem("id").getNodeValue() );
                String Brewer = childAttr.getNamedItem("brewer").getNodeValue().toString();
                String Title = childAttr.getNamedItem("name").getNodeValue().toString();
                String Style = childAttr.getNamedItem("style").getNodeValue().toString();
                int iteration = 0;//Integer.parseInt(childAttr.getNamedItem("iteration").getNodeValue());
                Debug.print("Loading: " + Title);
                BasicRecipe rRecipe = new BasicRecipe(ID, Brewer, Style, Title, iteration);
                recipes.add(rRecipe);


            }


            recipeTableModel.setData(recipes);
            recipeTable.updateUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }



    }

    private List<String> getListOfStyles() {

        List<String> styles = new ArrayList<String>();

        try
        {

            String baseURL = Options.getInstance().getProperty("cloudURL");
            URI rURI = new URI("http", null, baseURL, 80, "/styles/", null, null);
            URL url = rURI.toURL();
            //URL url = new URL(baseURL+"/styles/");
            InputStream response = url.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setValidating(false);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            // dbf.setCoalescing(true);
            // dbf.setExpandEntityReferences(true);

            DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();

            Document readXML = db.parse(response);
            NodeList childNodes = readXML.getElementsByTagName("style");


            Debug.print("Loading Styles from online: "+ childNodes.getLength());

            for(int x = 0; x < childNodes.getLength(); x++ ) {


                Node child = childNodes.item(x);

                // generate the style list
                Debug.print("Style: " + child.getTextContent());
                if(child.getTextContent() != null) {
                    styles.add(child.getTextContent());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return styles;
    }


    public void focusGained() {

    }

    public void actionPerformed(ActionEvent e) {
        if(close) {
            Debug.print("Closing window");
            RemoteRecipes.this.setVisible(true);
            //System.out.println(RemoteRecipes.this.toString());
            RemoteRecipes.this.setVisible(false);
            RemoteRecipes.this.dispose();
            return;
        }

        Object o = e.getSource();
        if (o == cancelButton) {
            //System.out.println(this.toString());
            setVisible(false);
            dispose();

            return;
        } else if (o == openButton) {


            int i = recipeTable.getSelectedRow();
            Debug.print("Opening row " + i + " - " + recipes.size());
            if (i > -1 && i < recipes.size()) {
                // grab the Blob and name
                long ID = recipes.get(i).id;
                StrangeSwing.getInstance().recipeFile = getRemoteRecipe(ID, recipes.get(i).brewer, recipes.get(i).title, recipes.get(i).iteration);
                // open the file to write to

            }

            setVisible(false);
            dispose();
            return;
        } else if (o == styles) {
            // someone selected a style, update the list based on the recipes
            loadRecipesByStyle(styles.getSelectedItem().toString());
        }
    }

    private String getRemoteRecipe(long ID, String Title, String Brewer, int iteration) {
        try
        {
            String baseURL = Options.getInstance().getProperty("cloudURL");
            if (!baseURL.startsWith("http://")) {
                baseURL = "http://" + baseURL;
            }
            URL url = new URL(baseURL+"/recipes/"+ID);
            InputStream response = url.openStream();

            HttpURLConnection huc =  ( HttpURLConnection )  url.openConnection ();
           huc.setRequestMethod ("GET");
           huc.connect () ;

           int code = huc.getResponseCode (  ) ;

            if(code != 200) {
                JOptionPane.showMessageDialog(null, "Couldn't find the selected recipe (ID "+ ID +")");
                huc.disconnect();
                this.close = true;
                return ""   ;

            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setValidating(false);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            // dbf.setCoalescing(true);
            // dbf.setExpandEntityReferences(true);

            DocumentBuilder db = null;
            db = dbf.newDocumentBuilder();
            //db.setEntityResolver(new NullResolver());

            // db.setErrorHandler( new MyErrorHandler());

            Document readXML = db.parse(response);

            // check if we already have this file
            String file = Title + " - " + Brewer + " ("+ iteration + ").xml";

            File recipeFile = new File(currentDir, file);

            if(recipeFile.exists()) {

                JOptionPane.showMessageDialog(null, "This file already exists!");
                return recipeFile.getAbsolutePath();

            }
            Debug.print("Writing recipe file: " + recipeFile.getAbsolutePath());
            // file doesn't exist, lets dump the file
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(readXML);
            transformer.transform(source, result);

            OutputStream oStream = new FileOutputStream(recipeFile);
            String outXML = result.getWriter().toString();
            oStream.write(outXML.getBytes(Charset.forName("UTF-8")));
            oStream.close();
            return recipeFile.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    class BasicRecipe {

        public long id;
        public String brewer;
        public String style;
        public String title;
        public int iteration;

        BasicRecipe(long ID, String Brewer, String Type, String Title, int Iteration) {
            id = ID;
            brewer = Brewer;
            style = Type;
            title = Title;
            iteration = Iteration;
        }
    }

    class FindTableModel extends AbstractTableModel {

        private String[] columnNames = {"Recipe", "Style", "Brewer", "Iteration"};

        private List<BasicRecipe> data;

        public FindTableModel() {
            // data = new ArrayList();
        }

        public FindTableModel(String filter) {
            // data = new ArrayList();
            // check to see if the filter value is a style or brewer
        }

        public void setData(List<BasicRecipe> l) {
            data = l;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            if (data != null)
                return data.size();
            else
                return 0;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {

            try {
                BasicRecipe r = data.get(row);
                switch (col) {
                    case 0 :
                        return r.title;
                    case 1 :
                        return r.style;
                    case 2 :
                        return r.brewer;
                    case 3 :
                        return r.iteration;

                    default :
                        return "";

                }
            } catch (Exception e) {
            };
            return "";
        }

        /*
         * JTable uses this method to determine the default renderer/ editor for
         * each cell. If we didn't implement this method, then the last column
         * would contain text ("true"/"false"), rather than a check box.
         */

        // public Class getColumnClass(int c) {
        // return getValueAt(0, c).getClass();
        // }


    }

}
