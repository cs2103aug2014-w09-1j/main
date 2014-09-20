package mytasks.file;

/**
 * MyTasksParser interprets userinput to useable data structures to work with in the Logic component
 * @author Wilson
 *
 */
public class MyTasksParser {
	
	/**
	 * parseInput reads and converts String input into its equivalent CommandType object with the respective
	 * fields
	 * @param input from user
	 * @return CommandType object that is used to execute input
	 */
	public static CommandType parseInput (String input){
		String comdType = getFirstWord(input);
		//TODO parse input into commandtype object. (#labels)
		return null;
	}

	private static String getFirstWord(String input) {
		return input.split("\\s+")[0];
	}
}
