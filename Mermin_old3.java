import java.util.Random;
import java.util.Date;

/**
*  Demonstrate local behavior with Mermin-EPR characteristics
*   by simulation of D. Mermin Gedanken
*/
public class Mermin {
	Random r;
	int n = 100000;  // number of tests to perform
		
	public final static void main(String[] args) { 
		Mermin m = new Mermin();
	}

	/**
	*  Construct a simulation
	*/
	public Mermin() {

		boolean answers1[] = new boolean[n]; // for case where settings agree
		boolean answers2[] = new boolean[n]; // for case where settings are random

		r=new Random(); // init quasirandom generator to current time for creating "particles" from source
		for (int i=0; i<n; i++) {
	
			// source: choose a random angle (number) from 0 to 2pi for the spin of emitted particles			
			double angle = r.nextDouble()*Math.PI*2.0;

			// set the first detector to one of three positions randomly  
			double pos1 = r.nextDouble()*3.0;
			double pos1angle = 0.0;			
			if (pos1>0.0 & pos1<1.0) pos1angle = 0.0;			
			if (pos1>1.0 & pos1<2.0) pos1angle = Math.PI*2.0/3.0;			
			if (pos1>2.0 & pos1<3.0) pos1angle = Math.PI*4.0/3.0;

			// for first test run lets take the second detector to be the same as 1st always
			double pos2angle = pos1angle;

                        // for second test run we take second detector to be also random
			double pos3 = r.nextDouble()*3.0;			
			double pos3angle = 0.0;			
			if (pos3>0.0 & pos3<1.0) pos3angle = 0.0;			
			if (pos3>1.0 & pos3<2.0) pos3angle = Math.PI*2.0/3.0;			
			if (pos3>2.0 & pos3<3.0) pos3angle = Math.PI*4.0/3.0;
			
			// ok lets see what the results of this run are
			answers1[i] = getResult(pos1angle, pos2angle, angle);
			answers2[i] = getResult(pos1angle, pos3angle, angle);			
		}
		
		// Output our result of the simulation to stdout
		int numTrue1 = 0;
		int numTrue2 = 0;	
		for (int i=0; i<n; i++) {
			if (answers1[i]) numTrue1++;
			if (answers2[i]) numTrue2++;
		}
		System.out.println("1st test: " + numTrue1 + "2nd test: " + numTrue2);
	}

	/**
	*  This returns true if the measurements wind up agreeing.. false if they don't
	*  
	*   SettingA is detector 1 orientation, SettingB is detecter 2 orientation, spin is particle spin (angle) 	
	*   settings are 0, 2*pi/3, and 4*pi/3
	*/
	public boolean getResult(double settingA, double settingB, double spin) {
		
		// set up our random number generator at each detector
		Date d = new Date();
		long seedA = Double.doubleToLongBits(settingA-spin)-d.getTime();
		long seedB = Double.doubleToLongBits(settingB-spin)-d.getTime();
		Random ra = new Random(seedA/123450000000l);  
 		Random rb = new Random(seedB/123450000000l);  
		double ranA = ra.nextDouble();
		double ranB = rb.nextDouble();

		// current status of light at each detector (red=false, green=true)
		boolean boolA = false; 
		boolean boolB = false;

		// we calculate the difference between the detector angle and particle angle					
		double diffA = Math.abs(settingA-spin);
		double diffB = Math.abs(settingB-spin);

		// maximum angle between the two is 180 degrees ..  if it's more use the equivalent between 0 and PI
		if (diffA > Math.PI) diffA = 2.0*Math.PI - diffA;
		if (diffB > Math.PI) diffB = 2.0*Math.PI - diffB;
		
		if (diffA > Math.PI/2.0) diffA = Math.PI - diffA;
		if (diffB > Math.PI/2.0) diffB = Math.PI - diffB;
		// if we are greater than 90 degrees leave the measurement false otherwise:
		//if (diffA < 2.0*Math.PI/3.0) {		
			double dotA = diffA/4.0*Math.PI*Math.cos(diffA);//*Math.cos(diffA);
			dotA *= dotA;
			if (ra.nextDouble() < dotA ) boolA =true;
			//if (ra.nextDouble() < (2.0-Math.sqrt(2.0)) ) boolA =true;
		//}

		//if (diffB < 2.0*Math.PI/3.0) {
			double dotB = diffB/4.0*Math.PI*Math.cos(diffB);//*Math.cos(diffB);
			dotB *= dotB;
			if (rb.nextDouble() < dotB ) boolB =true;
			//if (rb.nextDouble() < (2.0-Math.sqrt(2.0)) ) boolB =true;
		//}

		// if the lights are the same return true.  else return false.  
		return boolA==boolB;
	}
		
	
}
