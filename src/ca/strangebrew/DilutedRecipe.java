package ca.strangebrew;

public class DilutedRecipe extends Recipe {

	private Quantity addVol;

	// A diluted recipe can only be created from an undiluted recipe
	private DilutedRecipe() {
	}

	// Create a new Diluted Recipe with 0 added water
	public DilutedRecipe(Recipe r) {
		super(r);
		this.addVol = new Quantity(super.getVolUnits(), 0);
	}

	// Create a diluted recipe from a recipe and an amount of dilution volumn
	public DilutedRecipe(Recipe r, Quantity dil) {
		super(r);
		this.addVol = new Quantity(dil);		
	}

	// Specialized Functions
	public double getAddVol(final String u) {
		return this.addVol.getValueAs(u);
	}

	public void setAddVol(final Quantity a) {
		addVol = a;
	}

	private double calcDilutionFactor() {
		return (this.getAddVol(super.getVolUnits()) + super.getFinalWortVol(super.getVolUnits())) / super.getFinalWortVol(super.getVolUnits());
	}
	
	// Overrides 
	public void setVolUnits(final String v) {
		super.setVolUnits(v);
		this.addVol.setUnits(v);
	}

	public double getFinalWortVol(final String s) {
		return this.getAddVol(s) + super.getFinalWortVol(s);		
	}

	public double getColour() {
		return getColour(getColourMethod());
	}
	
	public double getColour(final String method) {		
		return BrewCalcs.calcColour(getMcu() / this.calcDilutionFactor(), method);		
	}
	
	public double getIbu() {
		return super.getIbu() / this.calcDilutionFactor();
	}
	
	public double getEstOg() {
		return ((super.getEstOg() - 1) / this.calcDilutionFactor()) + 1;
	}
	
	public double getAlcohol() {
		return super.getAlcohol() / this.calcDilutionFactor();
	}		
	
	public String toXML(final String printOptions) {
		StringBuffer unDil = new StringBuffer(super.toXML(printOptions));
		String dil = "";
		
		// This is UGGLY! :)
		dil += "      <ADDED_VOLUME>" + SBStringUtils.format(addVol.getValue(), 2) + "</ADDED_VOLUME>\n";
		int offset = unDil.indexOf("</DETAILS>\n");
		unDil.insert(offset, dil);
		
		return unDil.toString();
	}
	
	public String toText() {
		String unDil = super.toText();
		
		unDil += "Dilute With: " + SBStringUtils.format(addVol.getValue(), 2) + addVol.getUnits() + "\n";
			
		return unDil;
	}
	
//	public double getBUGU() {
//		double bugu = 0.0;
//		if ( estOg != 1.0 ) { 
//		   bugu = ibu / ((estOg - 1) * 1000);
//		}		
//		return bugu;
//	}	
	// Accessors to original numbers for DilPanel
	public double getOrigFinalWortVol(final String s) {
		return super.getFinalWortVol(s);		
	}

	public double getOrigColour() {
		return super.getColour();
	}

	public double getOrigColour(final String method) {		
		return super.getColour(method);
	}
	
	public double getOrigIbu() {
		return super.getIbu();
	}
	
	public double getOrigEstOg() {
		return super.getEstOg();
	}
	
	public double getOrigAlcohol() {
		return super.getAlcohol();
	}				
}
