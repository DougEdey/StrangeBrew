/*
 * Created on Oct 15, 2004
 */
package strangebrew.ui.core;

/**
 * @author mike
 *
 */
public abstract class MenuView extends View {

	public abstract Menu getFileMenu();
	public abstract MenuItem getQuitItem();
	public abstract void quit();
	
}
