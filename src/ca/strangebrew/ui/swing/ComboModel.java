/*
 * Created on May 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ca.strangebrew.ui.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import ca.strangebrew.Fermentable;
import ca.strangebrew.Hop;
import ca.strangebrew.Style;
import ca.strangebrew.Yeast;


public class ComboModel extends AbstractListModel implements ComboBoxModel {
	
	private final static boolean DEBUG = false;

	List list = new ArrayList();
	Object selected = null;

	public void addOrInsert(Object o) {

		int i = 0;
		boolean found = false;
		while (i < list.size() && !found) {
			if (DEBUG) System.out.println(o.getClass().getName().toString());
			if (o.getClass().getName().toString().equals(
					"strangebrew.Yeast")) {
				Yeast y = (Yeast) list.get(i);
				Yeast y2 = (Yeast) o;
				found = y.getName().equalsIgnoreCase(y2.getName());
			} else if (o.getClass().getName().toString().equals("strangebrew.Fermentable")) {
				Fermentable f = (Fermentable) list.get(i);
				Fermentable f2 = (Fermentable) o;
				found = f.getName().equalsIgnoreCase(f2.getName());
			} else if (o.getClass().getName().toString().equals("strangebrew.Hop")) {
				Hop h = (Hop) list.get(i);
				Hop h2 = (Hop) o;
				found = h.getName().equalsIgnoreCase(h2.getName());
			} else if (o.getClass().getName().toString().equals("strangebrew.Style")) {
				Style s = (Style) list.get(i);
				Style s2 = (Style) o;
				found = s.getName().equalsIgnoreCase(s2.getName());
			} else if (o.getClass().getName().toString().equals("java.lang.String")) {
				String q = (String) list.get(i);
				String q2 = (String) o;
				found = q.equalsIgnoreCase(q2);
			}
			 
			i++;
		}
		
		// if it's not found, add it to the list & select it, 
		// otherwise, set the found index to the selected index
		
		if (!found) {
			list.add(o);
			selected = o;
		}
		
		else selected = list.get(i-1);

	}
	
	public Object getSelectedItem() {
		return selected;
	}
	
	public void setSelectedItem(Object item) {
		if ((selected != null && !selected.equals(item))
				|| (selected == null && item != null)) {
			selected = item;
			fireContentsChanged(this, -1, -1);
		}
	}
	

	public int getSize() {
		return list.size();
	}

	public Object getElementAt(int index) {
		return list.get(index);
	}

	public void setList(ArrayList l) {
		list = l;
	}
}