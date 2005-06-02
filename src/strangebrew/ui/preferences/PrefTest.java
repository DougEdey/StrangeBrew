package strangebrew.ui.preferences;

import javax.swing.JFrame;

import strangebrew.Options;

/**
 * @author zymurgist
 *
 * A simple class that can be used to test the preferences dialog
 * box.
 */
public class PrefTest
{
    public static void main(String[] args)
    {
    	JFrame frame = new JFrame();
    	Options preferences = new Options();
    	PreferencesDialog d = new PreferencesDialog(frame, preferences);
    	d.show();
    }
}
