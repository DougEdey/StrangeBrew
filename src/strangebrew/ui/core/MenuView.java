/*
 * $Id: MenuView.java,v 1.7 2004/10/21 02:01:23 tangent_ Exp $
 * Created on Oct 15, 2004
 */
package strangebrew.ui.core;

import strangebrew.ui.core.widgets.Menu;
import strangebrew.ui.core.widgets.MenuItem;

/**
 * @author mike
 *
 */
public abstract class MenuView extends View {

	public abstract Menu getFileMenu();
	public abstract MenuItem getOpenItem();
	public abstract MenuItem getQuitItem();
	public abstract void quit();
	public abstract String getOpenFilename();
	
}
