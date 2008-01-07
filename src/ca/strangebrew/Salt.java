package ca.strangebrew;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Salt {
	private String name;
	private String commonName;
	private String chemicalName;
	private double amount = 0.0;
	private String amountU = Quantity.G;
	private ArrayList<ChemicalEffect> chemicalEffects = new ArrayList<ChemicalEffect>();
	
	public static final String MAGNESIUM = "Mg";
	public static final String CHLORINE = "Cl";
	public static final String SODIUM = "Na";
	public static final String SULPHATE = "So4"; 
	public static final String CARBONATE = "Co3";
	public static final String CALCIUM = "Ca";
	public static final String HARDNESS = "Hardness";
	public static final String ALKALINITY = "Alkalinity";
	
	public static final String MAGNESIUM_SULPHATE = "Magnesium Sulfate";
	public static final String CALCIUM_CARBONATE = "Calcium Carbonate";
	public static final String SODIUM_CHLORIDE = "Sodium Chloride";
	public static final String SODIUM_BICARBONATE = "Sodium Bicarbonate";
	public static final String CALCIUM_SULPHATE = "Calcium Sulphate";
	public static final String CALCIUM_CHLORIDE = "Calcium Chloride";
	
	public Salt() { 
	}
	
	public Salt(Salt s) {
		this.name = s.getName();
		this.commonName = s.getCommonName();
		this.chemicalName = s.getChemicalName();
		this.amount = s.getAmount();
		this.amountU = s.getAmountU();
		// Shallow copy! This should be ok, since this stuff should 'never' change on the fly
		this.chemicalEffects = s.getChemicalEffects();
	}

	public double getAmount() {
		return amount;
	}

	public String getAmountU() {
		return amountU;
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

	public ArrayList<ChemicalEffect> getChemicalEffects() {
		return chemicalEffects;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String toString() {
		MessageFormat mf;
		
		mf = new MessageFormat(name + "(" + commonName + ") {0,number,0.000}" + amountU);		
		Object[] objs = {new Double(amount)};
				
		return mf.format(objs);
	}
	
	public String toXML(int indent) {
		String xml = "";
		
		xml += SBStringUtils.xmlElement("NAME", name, indent);
		xml += SBStringUtils.xmlElement("COMMONNAME", commonName, indent);
		xml += SBStringUtils.xmlElement("CHEM", chemicalName, indent);
		xml += SBStringUtils.xmlElement("AMOUNT", SBStringUtils.format(amount, 2), indent);
		xml += SBStringUtils.xmlElement("AMOUNTU", amountU, indent);		
			
		return xml;
	}

	// TODO currently only in grams
	/*public void setAmountU(String amountU) {
		this.amountU = amountU;
	}*/

	public void addChemicalEffect(String elem, double val) {
		// Remove old effect of same name first!
		for (int i = 0; i < this.chemicalEffects.size(); i++) {
			ChemicalEffect temp = this.chemicalEffects.get(i);
			if (temp.getElem().equals(elem)) {
				this.chemicalEffects.remove(i);
				break;
			}
		}

		ChemicalEffect e = new ChemicalEffect(elem, val);
		this.chemicalEffects.add(e);
	}
	
	public void setChemicalEffects(ArrayList<ChemicalEffect> effs) {
		this.chemicalEffects.clear();
		this.chemicalEffects.addAll(effs);
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


	public double getEffectByChem(String chem) {
		for (int i = 0; i < chemicalEffects.size(); i++) {
			if (((ChemicalEffect)chemicalEffects.get(i)).getElem().equals(chem)) {
				return ((ChemicalEffect)chemicalEffects.get(i)).getEffect();
			}
		}
		
		return 0;
	}
	
	static public Salt getSaltByName(ArrayList<Salt> salts, String name) {
		for (int i = 0; i < salts.size(); i++) {
			if (salts.get(i).getName().equals(name)) {
				return salts.get(i);
			}
		}
		
		return null;
	}
}
