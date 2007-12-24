package ca.strangebrew;

public class WaterProfile {
	private String name;
	private String description;
	private double ca;
	private double mg;
	private double na;
	private double so4;
	private double hco3;
	private double cl;
	private double hardness;
	private double tds;
	private double ph;
	private double alkalinity;
	
	public WaterProfile() {
	}

	public double getAlkalinity() {
		return alkalinity;
	}

	public double getCa() {
		return ca;
	}

	public double getCl() {
		return cl;
	}

	public String getDescription() {
		return description;
	}

	public double getHardness() {
		return hardness;
	}

	public double getHco3() {
		return hco3;
	}

	public double getMg() {
		return mg;
	}

	public double getNa() {
		return na;
	}

	public String getName() {
		return name;
	}

	public double getPh() {
		return ph;
	}

	public double getSo4() {
		return so4;
	}

	public double getTds() {
		return tds;
	}

	public void setAlkalinity(double alkalinity) {
		this.alkalinity = alkalinity;
	}

	public void setCa(double ca) {
		this.ca = ca;
	}

	public void setCl(double cl) {
		this.cl = cl;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHardness(double hardness) {
		this.hardness = hardness;
	}

	public void setHco3(double hco3) {
		this.hco3 = hco3;
	}

	public void setMg(double mg) {
		this.mg = mg;
	}

	public void setNa(double na) {
		this.na = na;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPh(double ph) {
		this.ph = ph;
	}

	public void setSo4(double so4) {
		this.so4 = so4;
	}

	public void setTds(double tds) {
		this.tds = tds;
	}
}
