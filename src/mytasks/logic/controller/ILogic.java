package mytasks.logic.controller;

import java.util.List;

import mytasks.file.FeedbackObject;

//@author A0114302A
/**
 * ILogic interface defines abstract methods to be used by logic component
 */
public interface ILogic {
	
	/**
	 * executeCommand executes the user's input.
	 * @param userInput
	 * @return message/updated UI to be printed
	 */
	public FeedbackObject executeCommand(String userInput);
	
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
	
	/**
	 * checkIfToHide checks if there is a need to hide any labels. If so, call labelsToHide
	 * @return boolean
	 */
	public boolean checkIfToHide();
	
	/**
	 * labelsToHide returns the labels that should be hidden. Values are only accurate if
	 * checkIfToHide is true. 
	 * @return List of strings to hide
	 */
	public List<String> labelsToHide();
	
	/**
	 * checkIfToHelpUI checks if there is a need to display the help pop up
	 * @return boolean
	 */
	public boolean checkIfToHelpUI();
	
	/**
	 * toggleHelpUIOff changes boolean flag for HelpUI
	 * @param onOff
	 */
	public void toggleHelpUI(boolean onOff);
}
