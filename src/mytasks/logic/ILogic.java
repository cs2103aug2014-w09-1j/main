package mytasks.logic;

//@author A0114302A
import java.util.List;

/**
 * ILogic interface defines abstract methods to be used by logic component
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
	public List<String> obtainPrintableOutput();
	
	/**
	 * obtainAllTaskDescription generates a list of all the task descriptions
	 * @return a list containing all the task descriptions of the current memory
	 */
	public List<String> obtainAllTaskDescription();
	
	/**
	 * obtainsAllLabels generates a list of all the labels currently used by memory 
	 * @return a list of strings
	 */
	public List<String> obtainAllLabels();
}
