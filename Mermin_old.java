import java.util.Random;
import java.util.Date;

/**
*  Demonstrate local behavior with Mermin-EPR characteristics
*  
*/
public class Mermin {
	Random r;
	int n = 100000;  // number of tests to perform
	boolean debug = true;
	
	/**
	*  Construct a simulation
	*/
	public Mermin() {

		boolean answers[] = new boolean[n];
		System.out.println("Starting simulation cos pi = " + Math.cos(Math.PI));
		r=new Random(); // init quasirandom to current time

		for (int i=0; i<n; i++) {

			if (i>5) debug = false;			
			// choose a random angle from 0 to 2pi for the spin of emitted particles			
			double angle = r.nextDouble()*Math.PI*2.0;
			if (debug) System.out.println("angle = " + angle);

			// set the first detector to one of three positions randomly
			double pos1 = r.nextDouble()*3.0;
			double pos1angle = 0.0;			
			if (pos1>0.0 & pos1<1.0) pos1angle = 0.0;			
			if (pos1>1.0 & pos1<2.0) pos1angle = Math.PI*2.0/3.0;			
			if (pos1>2.0 & pos1<3.0) pos1angle = Math.PI*4.0/3.0;
			if (debug) System.out.println("pos1angle: " + pos1angle);

			// for first test lets take the second detector to be the same
			//double pos2angle = pos1angle;

                        // for second test we take second detector to be also random
			// but for now not equal to the first detector
			double pos2 = r.nextDouble()*3.0;			
			double pos2angle = 0.0;			
			if (pos2>0 & pos2<1) pos2angle = 0.0;			
			if (pos2>1 & pos2<2) pos2angle = Math.PI*2.0/3.0;			
			if (pos2>2 & pos2<3) pos2angle = Math.PI*4.0/3.0;
			while (pos2angle == pos1angle) {
				pos2 = r.nextDouble()*3.0;			
				pos2angle = 0.0;			
				if (pos2>0 & pos2<1) pos2angle = 0.0;			
				if (pos2>1 & pos2<2) pos2angle = Math.PI*2.0/3.0;			
				if (pos2>2 & pos2<3) pos2angle = Math.PI*4.0/3.0;
			}

			if (debug) System.out.println("pos2angle: " + pos2angle);
			
			// ok lets see what the results of this run are
			boolean res = getResult(pos1angle, pos2angle, angle);
			answers[i] = res;			
			if (debug) System.out.println("result: " + res);
		}
		
		int numTrue = 0;	
		for (int i=0; i<n; i++) {
			if (answers[i]) numTrue++;
		}
	
		System.out.println("num true: " + numTrue);
	}

	/**
	*  This returns true if the measurements wind up agreeing.. false if they don't
	*  
	*   SettingA is detector 1 orientation, SettingB is detecter 2 orientation, spin is particle spin (angle) 	
	*   settings are 0, 2*pi/3, and 4*pi/3
	*/
	public boolean getResult(double settingA, double settingB, double spin) {
		
		// RESET FOR TESTING
		//settingA = 0.0;
		//settingB = 2.0*Math.PI/3.0;

		double diffA = Math.abs(settingA - spin);
		double diffB = Math.abs(settingB - spin);
		while (diffA>Math.PI) diffA=(Math.PI-(diffA-Math.PI));
		while (diffB>Math.PI) diffB=(Math.PI-(diffB-Math.PI));
		
		double ranA = r.nextDouble();
		double ranB = r.nextDouble();

		boolean boolA = false; 
		boolean boolB = false;
		//if (ranA > (2.0-Math.sqrt(2.0))/2.0) boolA = true;
		//if (ranB > (Math.sqrt(2.0))/2.0) boolB = true;


		if (ranA > (2.0-Math.sqrt(2.0))/4.0 ) boolA =true;
		if (ranB < (2.0-Math.sqrt(2.0))/4.0 ) boolB =true;	
		//if (Math.cos(diffA/2.0) < ranA) boolA = true;
		//if (Math.cos(diffB/2.0) < ranB) boolB = true;
		
		// for test lets just look at A result, not agreement of A & B
		return boolA==boolB;

	//		return (boolA==boolB);
		// keep detectors separate in code.. they don't communicate during measurement
/*
		// first lets get the dot products at each detector
		double dotA = Math.cos(Math.abs(settingA-spin));
		double dotB = Math.cos(Math.abs(settingB-spin));
		dotA *= dotA;
		dotB *= dotB;
		if (debug) System.out.println("angle differences: "+ (settingA-spin)*180.0/Math.PI + " .. " + (settingB-spin)*180/Math.PI + " dotA: " + dotA + " dotB: " + dotB);
		if (debug) System.out.println(settingA + " " + Double.doubleToLongBits(settingA));

		// test the angles are correct
		
		
		// initialize our randoms at each detector based on seed from particle spin and current epoch (synchronized clocks!)
		try { Thread.sleep(0);} catch (Exception e) {e.printStackTrace();}		
		Date d = new Date();
		long seedA = Double.doubleToLongBits(settingA)-d.getTime();
		long seedB = Double.doubleToLongBits(settingB)-d.getTime();

		if (debug) System.out.println(seedA + " is seed a and seed b is " + seedB);
		Random ra = new Random(seedA/10000000000l);  int q = ra.nextInt(2);
 		Random rb = new Random(seedB/10000000000l);  q = ra.nextInt(2);

		// roll the dice at each detector
		double resA = 1.0-ra.nextDouble()*2.0;
		double resB = 1.0-rb.nextDouble()*2.0;
		if (debug) System.out.println(" in gr: resA: " + resA + " resB: " + resB);

		// new test for sanity
		boolean boolA = false; 
		boolean boolB = false;

		if (resA > dotA) boolA = true;
		if (resB > dotB) boolB = true;
		
		return (boolA==boolB); 
*/
	}
		
		
	public final static void main(String[] args) {
		Mermin m = new Mermin();
	}
}
