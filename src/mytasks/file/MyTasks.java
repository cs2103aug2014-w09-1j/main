package mytasks.file;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * myTasks is a tasks management program designed for users who like to use command line interface 
 * to manage daily schedule. API for myTasks is provided in the user guide.
 * 
 * @author Wilson, Micheal, ShaungSiang, HuiWen
 * @version 0.1
 *
 */
public class MyTasks {
	
	private static Scanner sc;
	
	//TODO Update data structure for localMem to suit program needs (may need private class)
	private static ArrayList<String> localMem;
	
	public static void main (String[] args){
		
		initProgram();
		
		//TODO retrieve external memory 
		
		//TODO Update UI
		
		while (true) {
			
			String input = sc.nextLine();
			CommandType inputDetails = parseInput(input);
			
			//TODO execute input (need to decide if we want to aid TDD and return something or do void)
			
			//TODO update UI
			
		}
		
		//TODO store updated information back to external memory
		
	}
	
	/**
	 * initProgram initializes all local variables to prevent and data overflow from previous sessions
	 */
	private static void initProgram() {
		
		sc = new Scanner(System.in);
		localMem = new ArrayList<String>();
		
	}
	
	/**
	 * parseInput reads and converts String input into its equivalent CommandType object with the respective
	 * fields
	 * @param input from user
	 * @return CommandType object that is used to execute input
	 */
	private static CommandType parseInput(String input) {
		
		//TODO parse input into commandtype object
		
		return null;
	}
	
}
