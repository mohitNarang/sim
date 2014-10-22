package sim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class reads through the parameter file and sets the values accordingly.
 *
 */
public class ParseInput {

	private static final String loglevel = "loglevel";
	private static final String mechanism = "mechanism";
	private static final String numrounds = "numrounds";
	private static final String reserve = "reserve";
	private static final String totalbudget = "totalbudget";
	private static final String dropoff = "dropoff";
	private static final String minval = "minval";
	private static final String maxperms = "maxperms";
	private static final String maxval = "maxval";
	private static final String numiters = "numiters";
	private static final String seed = "seed";
	private static final String agents = "agents";

	
	public static void setParameters(String fileName){
		
		try {
			BufferedReader b = new BufferedReader(new FileReader(fileName));
			String line;
			while( (line = b.readLine()) != null){
				String[] s;
				//do nothing, line is a comments
				if (line.contains("#")){
				}

				else if (line.contains(mechanism)){
					s = line.split(" ");
					if (s[1].toLowerCase().equals("gsp")){
						AuctionSimulator.setMechanism("gsp");
					}
					else if (s[1].toLowerCase().equals("vcg")){
						AuctionSimulator.setMechanism("vcg");
					}
					else if (s[1].toLowerCase().equals("switch")){
						AuctionSimulator.setMechanism("switch");
					}
				}
				else if (line.contains(numrounds)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setNumRounds(i);
					}
					catch (NumberFormatException e){
						System.err.println("numrounds must be an integer.");
					}
				}
				else if (line.contains(reserve)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setReserve(i);
					}
					catch (NumberFormatException e){
						System.err.println("reserve must be an integer.");
					}
				}
				else if (line.contains(totalbudget)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setTotalBudget(i);
					}
					catch (NumberFormatException e){
						System.err.println("totalbudget must be an integer.");
					}
				}
				else if (line.contains(dropoff)){
					s = line.split(" ");
					try {
						Double d = new Double(s[1]);
						AuctionSimulator.setDropOff(d);
					}
					catch (NumberFormatException e){
						System.err.println("dropoff must be a double.");
					}
				}
				else if (line.contains(minval)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setMinVal(i);
					}
					catch (NumberFormatException e){
						System.err.println("minval must be an integer.");
					}
				}
				else if (line.contains(maxval)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setMaxVal(i);
					}
					catch (NumberFormatException e){
						System.err.println("maxval must be an integer.");
					}
				}
				else if (line.contains(maxperms)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setMaxPerms(i);
					}
					catch (NumberFormatException e){
						System.err.println("maxperms must be an integer.");
					}
				}
				else if (line.contains(numiters)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setNumIters(i);
					}
					catch (NumberFormatException e){
						System.err.println("numrounds must be an integer.");
					}
				}
				else if (line.contains(seed)){
					s = line.split(" ");
					try {
						Integer i = new Integer(s[1]);
						AuctionSimulator.setSeed(i);
					}
					catch (NumberFormatException e){
						System.err.println("numrounds must be an integer.");
					}
				}
				else if (line.contains(loglevel)){
					s = line.split(" ");
					AuctionSimulator.setLoglevel(s[1]);
				}
				else if (line.contains(agents)){
					s = line.split(" ");
					ArrayList<String> agents = new ArrayList<String>();
					for (int i = 1; i< s.length-1; i=i+2){
						String name = s[i];
						Integer k = null;
						try{
							k = new Integer(s[i+1]);
						}catch(NumberFormatException e){
							System.err.println("Agent numbers must be integers.");
						}
						for (int n=0; n<k; n++){
							agents.add(name);
						}		
					}
					AuctionSimulator.setAgentNames(agents);
				}
			}
			b.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("Couldn't read the parameter file. Check the file name.");
			e.printStackTrace();
		} 
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}


}
