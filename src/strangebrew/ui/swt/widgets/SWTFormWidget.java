/*
 * $Id: SWTFormWidget.java,v 1.2 2004/10/21 01:55:23 tangent_ Exp $
 * Created on Oct 15, 2004
 */
package strangebrew.ui.swt.widgets;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * @author mike
 *
 */
public interface SWTFormWidget {

	public FormData getFormData();
	public Control getControl();
}
