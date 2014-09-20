package mytasks.file;

/**
 * ILogic interface defines abstract methods to be used by logic component
 * @author Wilson
 *
 */
public interface ILogic {
	
	/**
	 * executeCommand executes the user's input.
	 * @param userInput
	 * @return message/updated UI to be printed
	 */
	public String executeCommand(String userInput);
	
	/**
	 * parseInput calls the parser to read and understand user input 
	 * @param userInput
	 * @return CommandType object that contains the relevant fields
	 */
	public CommandType parseInput (String userInput);
	
}
