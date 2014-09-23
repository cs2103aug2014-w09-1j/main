package mytasks.parser;

import mytasks.file.CommandInfo;

public interface IParser {
	
	/**
	 * parseInput reads and converts String input into its equivalent CommandType object with the respective
	 * fields
	 * @param input from user
	 * @return CommandInfo object that is used to execute input
	 */
	public CommandInfo parseInput (String input);
}
