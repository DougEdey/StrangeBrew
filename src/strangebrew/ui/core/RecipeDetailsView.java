/*
 * Created on Oct 12, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.core;

import strangebrew.ui.core.widgets.NumberInput;
import strangebrew.ui.core.widgets.TextInput;
import strangebrew.ui.core.widgets.TextOutput;

public abstract class RecipeDetailsView extends View {

	public abstract TextInput getBrewer();
	public abstract TextOutput getBrewerLabel();
	public abstract TextOutput getEfficiencyLabel();
	public abstract NumberInput getEfficiency();
	public abstract TextOutput getAlcoholLabel();
	public abstract TextOutput getAlcohol();
	public abstract TextOutput getAlcoholPostfix();
}
