/*
 * Created on Oct 12, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strangebrew.ui.core;

import strangebrew.ui.core.widgets.*;

public abstract class RecipeDetailsView extends View {

	public abstract TextInput getBrewer();
	public abstract TextOutput getBrewerLabel();
	public abstract TextOutput getEfficiencyLabel();
	public abstract NumberInput getEfficiency();
	public abstract TextOutput getAlcoholLabel();
	public abstract TextOutput getAlcohol();
	public abstract TextOutput getAlcoholPostfix();
	public abstract TextOutput getDateLabel();
	public abstract TextInput getDate();
	public abstract TextOutput getMashLabel();
	public abstract CheckBox getMash();
	public abstract TextOutput getAttenuationLabel();
	public abstract NumberInput getAttenuation();
	public abstract TextOutput getIBULabel();
	public abstract TextOutput getIBU();
	public abstract TextOutput getIBUPostfix();
	public abstract TextInput getStyle();
	public abstract TextOutput getStyleLabel();
	public abstract TextOutput getOGLabel();
	public abstract NumberInput getOG();
	public abstract TextOutput getColourLabel();
	public abstract TextOutput getColour();
	public abstract TextOutput getColourPostfix();
	



}
