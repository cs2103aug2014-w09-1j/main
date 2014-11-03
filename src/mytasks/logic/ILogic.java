package mytasks.logic;

import java.util.ArrayList;

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
	 * obtainPrintableOutput reads and converts local memory into a form that is directly printable by UI.
	 * @return printable format that is not neccessarily a string. Will use string temporarily
	 */
	public ArrayList<String> obtainPrintableOutput();
	
	/**
	 * obtainAllTaskDescription 
	 * @return 
	 */
	public ArrayList<String> obtainAllTaskDesciption();
}
