package mytasks.parser;

import mytasks.logic.Command;

//@author A0114302A
public interface IParser {
	
	/**
	 * parseInput reads and converts String input into its equivalent CommandType object with the respective
	 * fields.
	 * Task object is empty in all fields for "undo","redo"
	 * mToUpdateTaskDesc is empty for all commands except "update"
	 * Task object represents new details to be updated to for "update"
	 * @param input from user
	 * @return CommandInfo object that is used to execute input
	 */
	public Command parseInput (String input);
}
