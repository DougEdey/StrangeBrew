package ca.strangebrew;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class RecipeUploadProgress extends JPanel implements ActionListener,
        PropertyChangeListener {

    private JProgressBar progressBar;
    private Task task;
    private JButton closeButton;
    private JTextArea taskOutput;
    private JPanel jpanel;
    
    class Task extends SwingWorker<Void, Void> {
        
        private URL url;
        private String xmlString;
        
        public Task(URL inUrl, String inXml) {
            url = inUrl;
            xmlString = inXml;
        }
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            String response;
            try {
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
            
                try {
                    urlConn.connect();
                } catch (ProtocolException e) {
                    taskOutput.append("Couldn't open connection: " + e.getMessage());
                    closeButton.setEnabled(true);
                    return null;
                }

                OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream());
                writer.write(xmlString);
                writer.flush();
               
                response = urlConn.getResponseMessage();
                if (response.equals("Bad Request")) {
                    // Couldn't upload. Duplicated.
                    String err = "Couldn't upload. The Recipe name is already uploaded.";
                    taskOutput.append(err);
                    JOptionPane.showMessageDialog(null, err);
                    urlConn.disconnect();
                    closeButton.setEnabled(true);
                    return null;
                }   
                // Read back to make sure it uploaded OK.
                InputStream inputStream;
                int responseCode = urlConn.getResponseCode();
                
                if ((responseCode >= 200) && (responseCode<=202)) {
                    inputStream = urlConn.getInputStream();
                    int j;
                    Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
                    String recipeString = s.next();
                    String recipeID = recipeString.replace("recipe id: ", url.toString());
                    
                    taskOutput.append("Upload Completed! \n URL is " + recipeID + "\n");    
                } else {
                    inputStream = urlConn.getErrorStream();
                }
                
                progressBar.setVisible(false);
                urlConn.disconnect();
        
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            closeButton.setEnabled(true);
            return null;
        }
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            setCursor(null); //turn off the wait cursor
        }
    }
 
    public RecipeUploadProgress() {
        super(new BorderLayout());
        
        //Create the demo's UI.
        closeButton = new JButton("Close");
        closeButton.setActionCommand("close");
        closeButton.addActionListener(this);
        closeButton.setEnabled(false);
 
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);
 
        taskOutput = new JTextArea(5, 100);
        taskOutput.setMargin(new Insets(5,5,5,5));
        taskOutput.setEditable(false);
 
        JPanel panel = new JPanel();
        panel.add(closeButton);
        panel.add(progressBar);
 
        add(panel, BorderLayout.PAGE_START);
        add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("close")) {
            Container parent = this.getParent();
            
            while (!(parent instanceof JFrame )) {
                parent = parent.getParent();
            }
            ((JFrame) parent).dispose();
            return;
        }

    }
    
    private void setURLConnection(URL inUrl, String inXml) {
        task = new Task(inUrl, inXml);
        task.addPropertyChangeListener(this);
        task.execute();
    }
    
    /**
     * Create the GUI and show it. As with all GUI code, this must run
     * on the event-dispatching thread.
     */
    public static void createAndShowGUI(URL inUrl, String inXml) {
        //Create and set up the window.
        JFrame frame = new JFrame("Recipe Upload");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new RecipeUploadProgress();
        newContentPane.setOpaque(true); //content panes must be opaque
        
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        ((RecipeUploadProgress) newContentPane).setURLConnection(inUrl, inXml);
        //frame.dispose();
        
    }
}
