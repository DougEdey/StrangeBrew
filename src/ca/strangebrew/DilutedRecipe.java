package ca.strangebrew;

public class DilutedRecipe extends Recipe {
//
//	private double dilOG;
//	private double dilIbu;
//	private double dilAlc;
//	private double dilSrm;
//	private Quantity dilVol;
//	private Quantity addVol;
//
//	// A diluted recipe can only be created from an undiluted recipe
//	private DilutedRecipe() {
//	}
//
//	public DilutedRecipe(Recipe r) {
//		super(r);
//
//		dilVol = new Quantity(getVolUnits(), super.postBoilVol.getValue());
//		addVol = new Quantity(getVolUnits(), 0);		
//	}
//
//
//	// Overriden Funcs
//	public void setPreBoil(final double p) {
//		super.setPreBoil(p);
//		calcDilution();
//	}	
//
//	public void setPostBoil(final double p) {
//		super.setPostBoil(p);
//		calcDilution();
//	}	
//
//	public double getDilSrm() {
//		return dilSrm;
//	}
//	
//	public double getAddVol() {
//		return addVol.getValueAs(getVolUnits());
//	}
//
//	public void setAddVol(final double a) {
//		addVol.setAmount(a);
//		addVol.setUnits(getVolUnits());
//		dilVol.setAmount(super.postBoilVol.getValue() + addVol.getValue());
//		dilVol.setUnits(getVolUnits());
//		calcDilution();
//	}
//
//	public double getDilAlc() {
//		return dilAlc;
//	}
//
//	public void setDilAlc(final double dilAlc) {
//		this.dilAlc = dilAlc;
//	}
//
//	public double getDilIbu() {
//		return dilIbu;
//	}
//
//	public void setDilIbu(final double d) {
//		final double dilutionFactor = d / super.ibu;
//		setDilVol(super.postBoilVol.getValue() / dilutionFactor);
//
//	}
//
//	public double getDilOG() {
//		if ( addVol.getValue() > 0 ) {
//			return dilOG;
//		} else {
//			return estOg;
//		}
//	}
//
//	public void setDilOG(final double d) {
//		final double dilutionFactor = (d - 1) / (estOg - 1);
//		setDilVol(postBoilVol.getValue() / dilutionFactor);
//	}
//
//	public double getDilVol() {
//		if ( addVol.getValue() > 0 ) {
//			return dilVol.getValueAs(getVolUnits());
//		} else { 
//			return postBoilVol.getValueAs(getVolUnits());
//		}
//	}
//
//	public void setDilVol(final double d) {
//		this.dilVol.setAmount(d);
//		this.dilVol.setUnits(getVolUnits());
//		addVol.setAmount(dilVol.getValue() - postBoilVol.getValue());
//		addVol.setUnits(getVolUnits());
//		calcDilution();
//	}
//
//	/*
//	 * Calculates the diluted values, assuming that the diluted volume has
//	 * been set
//	 */
//	private void calcDilution() {
//		final double dilutionFactor = dilVol.getValue() / postBoilVol.getValue();
//		dilIbu = ibu / dilutionFactor;
//		dilAlc = alcohol / dilutionFactor;
//		dilOG = ((estOg - 1) / dilutionFactor) + 1;
//		dilSrm = srm / dilutionFactor;
//
//	}

}
