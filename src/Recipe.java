/*
 * Created on Oct 4, 2004
 * @author aavis
 * recipe class 
 */

import java.util.*;

public class Recipe {

	public String name;
	public String brewer;
	public double estOg;
	public double estFg;
	public double ibu;
	public double srm;
	public double preBoilVol;
	public double postBoilVol;
	public String volUnits;
	public double efficiency;
	public int boilMinutes;
	public double attenuation;

	// calculated:
	public GregorianCalendar created;
	public double alcohol;
	public double totalMaltCost;
	

	// ingredients
	// implement arrayList later
	ArrayList hops = new ArrayList();
	ArrayList malts = new ArrayList();

	// test constuctor
	public Recipe() {
		// this is just a placeholder until we can
		// figure the recipe loader
		// and get the options setup
		name = "My Recipe";
		efficiency = 75;
		preBoilVol = 6;
		postBoilVol = 5;
		attenuation = 75;
		boilMinutes = 60;

		/*
		malts.add( new Malt("Pale Ale", 1.036, 3, 8, "lbs"));
		malts.add( new Malt("Carastan", 1.035, 34, 0.5, "lbs"));
		malts.add( new Malt("Wheat Malt", 1.039, 1.7, 0.5, "lbs"));

		hops.add ( new Hop("Saaz", 3.5, 2, 60) );
		hops.add ( new Hop("Saaz", 3.5, 2, 45) );
		hops.add ( new Hop("Saaz", 3.5, 2, 15) );
		*/

	}
	
	public void setName(String n){name = n;}
	public void setBrewer(String b){brewer = b;}
	public void setPreBoil(double p){preBoilVol = p;}
	public void setPostBoil(double p){postBoilVol = p;}
	public void setVolUnits(String v){volUnits = v;}
	public void setEfficiency(double e){efficiency = e;}
	public void setAttenuation(double a){attenuation = a;}
	
	public void addMalt(Malt m){
		malts.add(m);		
	}
	public void addHop(Hop h){
		hops.add(h);
	}

	/**
	 * Calculate all the malt totals from the array of malt objects
	 * TODO: support multiple units (this is why we use maltTotalLbs)
	 * I still have to figure out a "conversion" interface to do this
	 * Other things to implement:
	 *  - cost tracking
	 *  - hopped malt extracts (IBUs)
	 *  - the % that this malt represents
	 *  - error checking
	 * @return
	 */

	public void calcMaltTotals() {

		double og = 0;
		double fg = 0;
		double lov = 0;
		double maltTotalLbs = 0;
		double maltPoints = 0;
		double maltTotalCost = 0;
		double mcu = 0;

		// first figure out the total we're dealing with
		for (int i = 0; i < malts.size(); i++) {
			Malt m = ((Malt)malts.get(i));
			maltTotalLbs += m.amount; 
			if (m.mashed) // apply efficiencey
				maltPoints += (m.pppg - 1)
					* m.amount
					* efficiency
					/ postBoilVol;
			else
				maltPoints += (m.pppg - 1)
					* m.amount
					* 100
					/ postBoilVol;

			mcu += m.lov * m.amount / postBoilVol;
			maltTotalCost += m.costPerU * m.amount;
		}

		// set the fields in the object
		estOg = (maltPoints / 100) + 1;
		estFg = 1 + ((estOg - 1) * ((100 - attenuation) / 100));
		srm = calcColour(mcu);

		// Calculate alcohol
		calcAlcohol("Volume");


	}

	public void calcHopsTotals() {

		double ibuTotal = 0;
		double hopsOzTotal = 0;
		double hopsCostTotal = 0;

		for (int i = 0; i < hops.size(); i++) {
			// calculate the average OG of the boil
			// first, the OG at the time of addition:
			double adjPreSize, aveOg = 0;
			Hop h = ((Hop)hops.get(i));
			if (h.minutes > 0)
				adjPreSize =
					postBoilVol
						+ (preBoilVol - postBoilVol)
							/ (boilMinutes / h.minutes);
			else
				adjPreSize = postBoilVol;
			aveOg =
				1
					+ (((estOg - 1) + ((estOg - 1) / (adjPreSize / postBoilVol)))
						/ 2);
			ibuTotal
				+= calcTinseth(
					h.amount,
					postBoilVol,
					aveOg,
					h.minutes,
					h.alpha,
					4.15);
			hopsCostTotal += h.costPerU * h.amount;
			hopsOzTotal += h.amount;
		}

		// add malt IBUs:
		ibu = ibuTotal;

	}

	double calcColour(double lov) {
		// calculates SRM based on MCU (degrees LOV)
		double colour = 0;
		if (lov > 0)
			colour = 1.4922 * Math.pow(lov, 0.6859);
		else
			colour = 0;
		return colour;

	}

	void calcAlcohol(String method) {
		double oPlato = sGToPlato(estOg);
		double fPlato = sGToPlato(estFg);
		double q = 0.22 + 0.001 * oPlato;
		double re = (q * oPlato + fPlato) / (1.0 + q);
		alcohol = (oPlato - re) / (2.0665 - 0.010665 * oPlato);
		if (method == "Volume") // by volume
			alcohol = alcohol * estFg / 0.794;
		
	}

	double sGToPlato(double sg) { // function to convert a value in specific gravity as plato
		// equation based on HBD#3204 post by AJ DeLange
		double Plato;
		Plato =
			-616.989
				+ 1111.488 * sg
				- 630.606 * sg * sg
				+ 136.10305 * sg * sg * sg;
		return Plato;
	}

	double calcTinseth(
		double amount,
		double size,
		double sg,
		double time,
		double aa,
		double HopsUtil) {
		double daautil; // decimal alpha acid utilization
		double bigness; // bigness factor
		double boil_fact; // boil time factor
		double mgl_aaa; // mg/l of added alpha units
		double ibu;

		bigness = 1.65 * (Math.pow(0.000125, (sg - 1))); //0.000125   original
		boil_fact = (1 - (Math.exp(-0.04 * time))) / HopsUtil;
		daautil = bigness * boil_fact;
		mgl_aaa = (aa / 100) * amount * 7490 / size;
		ibu = daautil * mgl_aaa;
		return ibu;
	}
	
	public String toXML(){
	    StringBuffer sb = new StringBuffer();
	    sb.append( "<?xml version=\"1.0\"?>\n" );
	    sb.append( "<RECIPE>\n\n" );
	    sb.append( "<NAME>"+name+"</NAME>\n");
	    sb.append( "<SIZE>"+postBoilVol+"</SIZE>\n");
	    sb.append( "<SIZE_UNITS>"+volUnits+"</SIZE_UNITS>\n");
	    
	    for(int i=0; i<malts.size(); i++) {
	    	Malt m = (Malt)malts.get(i);
	        sb.append( m.toXML() );
	        sb.append( "\n" );
	    }
	    for(int i=0; i<hops.size(); i++) {
	    	Hop h = (Hop)hops.get(i);
	        sb.append( h.toXML() );
	        sb.append( "\n" );
	    }
	    sb.append( "</RECIPE>" );

	    return sb.toString();
	}
	
	public void testRecipe(){
		calcMaltTotals();
		calcHopsTotals();
		System.out.print("Recipe totals for " + name + ": \n");
		System.out.print("Vol: " + postBoilVol + "\n");
		System.out.print("Effic: " + efficiency + "\n");
		System.out.print("estOG: " + estOg + "\n");
		System.out.print("estFG: " + estFg + "\n");
		System.out.print("srm: " + srm + "\n");
		System.out.print("%alc: " + alcohol + "\n");
		System.out.print("IBUs: " + ibu + "\n");
	}

}
