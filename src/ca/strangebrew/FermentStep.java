package ca.strangebrew;

public class FermentStep {
	private String type;
	private String tempU;
	private int time;
	private double temp;
	static final private String[] types = {"Primary", "Secondary", "Clearing", "Ageing"};  
	static final Options opts = Options.getInstance();
	
	public FermentStep() {		
		type = opts.getProperty("optFermentType");
		tempU = opts.getProperty("optFermentTempU");
		time = opts.getIProperty("optFermentTime");
		temp = opts.getDProperty("optFermentTemp");
	}
	
	// Misc Utility
	static public int getTypeIndex(String s) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].equalsIgnoreCase(s)) {
				return i;
			}
		}

		return 0; 
	}
	
	static public String[] getTypes() {
		return types;
	}
	
	public String toXML() {
		String out = "      <ITEM>\n";
		out += "          <TYPE>" + type + "</TYPE>\n";
		out += "          <TIME>" + Integer.toString(time) + "</TIME>\n";
		out += "          <TEMP>" + Double.toString(temp) + "</TEMP>\n";
		out += "          <TEMPU>" + tempU + "</TEMPU>\n";
   		out += "      </ITEM>\n";
		return out;
	}

	
	// Getters and Setters
	public double getTemp() {
		return temp;
	}
	public String getTempU() {
		return tempU;
	}
	public int getTime() {
		return time;
	}
	public String getType() {
		return type;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public void setTempU(String tempU) {
		this.tempU = tempU;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
