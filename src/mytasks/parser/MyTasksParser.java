package mytasks.parser;

import mytasks.file.CommandInfo;

/**
 * MyTasksParser interprets userinput to useable data structures to work with in the Logic component. Naive version for now
 * @author Wilson
 *
 */
public class MyTasksParser {
	
	//Constructor
	public MyTasksParser() {
	}
	
	/**
	 * parseInput reads and converts String input into its equivalent CommandType object with the respective
	 * fields
	 * @param input from user
	 * @return CommandInfo object that is used to execute input
	 */
	public CommandInfo parseInput (String input){
		String comdType = getFirstWord(input);
		//TODO parse input into commandtype object. (#labels)
		return null;
	}

	private String getFirstWord(String input) {
		return input.split("\\s+")[0];
	}
}
