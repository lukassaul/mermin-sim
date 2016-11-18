import java.util.Random;
import java.util.Date;

/**
*  Demonstrate local behavior with Mermin-EPR characteristics
*
*   by simulation of D. Mermin Gedanken
*
*  The secret of understanding so-called "measurement problem" is here
*   - allow measurement device to use random number generator 
*      
*/
public class Mermin3 {
	
	Random r;  // pseudorandom number generator 
	               
	int n = 10000;  // number of tests to perform in each simulation
		
	public final static void main(String[] args) { 
		Mermin3 m = new Mermin3();
	}

	/**
	*  Construct a simulation
	*
	*  We will run two simulations as per the Mermin criteria
	*    with different settings on detectors  
	*/
	public Mermin3() {

		int answers1[] = new int[n]; // for case where settings agree
		int answers2[] = new int[n]; // for case where settings are random

		r=new Random(); // init quasirandom generator to current time for creating "particles" from source
		for (int i=0; i<n; i++) {
	
			// source: choose a random angle (number) from 0 to 2pi for the spin of emitted particles			
			double angle = r.nextDouble()*Math.PI*2.0;

			// set the first detector to one of three positions randomly  
			double pos1 = r.nextDouble()*3.0;
			double pos1angle = 0.0;			
			if (pos1>0.0 & pos1<=1.0) pos1angle = 0.0;			
			if (pos1>1.0 & pos1<=2.0) pos1angle = Math.PI*2.0/3.0;			
			if (pos1>2.0 & pos1<=3.0) pos1angle = Math.PI*4.0/3.0;

			// for first test run lets take the second detector to be the same as 1st always
			double pos2angle = pos1angle;

            // for second test run we take second detector to be also randomly selected
			double pos3 = r.nextDouble()*3.0;			
			double pos3angle = 0.0;			
			if (pos3>0.0 & pos3<=1.0) pos3angle = 0.0;			
			if (pos3>1.0 & pos3<=2.0) pos3angle = Math.PI*2.0/3.0;			
			if (pos3>2.0 & pos3<=3.0) pos3angle = Math.PI*4.0/3.0;
			
			// ok lets see what the results of this run are
			// getresult needs to return 0 (one or both off), 1 (disagree), or 2(agree)			
			answers1[i] = getResult(pos1angle, pos2angle, angle);
			answers2[i] = getResult(pos1angle, pos3angle, angle);			
		}
		
		// Output our result of the simulation to stdout
		int numTrue1 = 0; int numFalse1 = 0;
		int numTrue2 = 0; int numFalse2 = 0;	
		for (int i=0; i<n; i++) {
			if (answers1[i]==2) numTrue1++;
			if (answers1[i]==1) numFalse1++;
			if (answers2[i]==2) numTrue2++;
			if (answers2[i]==1) numFalse2++;
		}
		System.out.println("numTrue 1,  numFalse1 " + numTrue1 + " " + numFalse1);
		System.out.println("numTrue 2,  numFalse2 " + numTrue2 + " " + numFalse2);
		double prob1 = (double)numTrue1/(double)(numTrue1+numFalse1); 
		double prob2 = (double)numTrue2/(double)(numTrue2+numFalse2);
		System.out.println("1st test: " + prob1 + "\n 2nd test: " + prob2);
	}

	/**
	*   DETECTOR OPERATION - LOCAL PHYSICS ONLY  
	*
	*    We pass in here the settings of each detector and the spin angle of the particles (equal as per Mermin gedanken)
	*
	*    Neither detector needs to know the other setting or other result to make it's measurement (light red or green)
	*    Hence --   local physics 
	*
 	*   This function returns 0 if one or both detectors still unlit, 1 if they disagree, and 2 if they agree..  enough in for for Mermin 
	*  
	*   SettingA is detector 1 orientation, SettingB is detecter 2 orientation, spin is particle spin (angle) 	
	*   settings are 0, 2*pi/3, and 4*pi/3
	*/
	public int getResult(double settingA, double settingB, double spin) {

		// current status of light at each detector (0=off, 1=red, 2=green)
		int lightA = 0; 
		int lightB = 0;

		// we calculate the difference between the detector angle and particle angle					
		double diffA = Math.abs(settingA-spin);
		double diffB = Math.abs(settingB-spin);

		// maximum angle between the two is 180 degrees ..  if it's more use the equivalent between 0 and PI
		if (diffA > Math.PI) diffA = 2.0*Math.PI - diffA;
		if (diffB > Math.PI) diffB = 2.0*Math.PI - diffB;
		
		// there are two options.  If we are within 90 degrees we go for green.  Outside 90 degrees, we go red.		
		// 1st detector
		if (diffA > Math.PI/2.0) {
			// A has a chance to light up green
			if (r.nextDouble() < 0.0-Math.cos(diffA)) lightA = 2;
		}
		if (diffA < Math.PI/2.0) {
			// A has a chance to light up red
			if (r.nextDouble() < Math.cos(diffA)) lightA = 1;
		}
		// 2nd detector
		if (diffB > Math.PI/2.0) {
			// B has a chance to light up green
			if (r.nextDouble() < 0.0-Math.cos(diffB)) lightB = 2;
		}
		if (diffB < Math.PI/2.0) {
			// A has a chance to light up red
			if (r.nextDouble() < Math.cos(diffB)) lightB = 1;
		}

		// if the lights are the same return true.  else return false. 
		//System.out.println("final state: " + lightA + " " + lightB); 
		if (lightA==0 | lightB==0) return 0;
		else if (lightA==lightB) return 2;
		else if (lightA!=lightB) return 1;
		return 3; // shouldn't happen
	}
}
