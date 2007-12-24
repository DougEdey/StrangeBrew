package ca.strangebrew;

import java.util.ArrayList;

public class Salt {
	private String name;
	private String commonName;
	private String chemicalName;
	private double amount = 0.0;
	private String amountU = "gr";
	private ArrayList chemicalEffects = new ArrayList();
	
	public Salt() { 
	}

	public double getAmount() {
		return amount;
	}

	public String getAmountU() {
		return amountU;
	}

	public ArrayList getChemicalEffects() {
		return chemicalEffects;
	}
	
	public ChemicalEffect getChemicalEffect(int i) {
		return (ChemicalEffect)chemicalEffects.get(i);
	}

	public String getChemicalName() {
		return chemicalName;
	}

	public String getCommonName() {
		return commonName;
	}

	public String getName() {
		return name;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	// TODO
	/*public void setAmountU(String amountU) {
		this.amountU = amountU;
	}*/

	public void addChemicalEffect(String elem, double val) {
		ChemicalEffect e = new ChemicalEffect(elem, val);
		this.chemicalEffects.add(e);
	}

	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public class ChemicalEffect {
		final private String elem;
		final private double effect;
		
		public ChemicalEffect(String elem, double effect) {
			this.elem = elem;
			this.effect = effect;
		}

		public double getEffect() {
			return effect;
		}

		public String getElem() {
			return elem;
		}
	}
}
