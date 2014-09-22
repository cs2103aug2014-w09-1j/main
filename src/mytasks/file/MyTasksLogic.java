package mytasks.file;

import java.util.ArrayList;

/**
 * MyTasksLogic handles all logic related operations such as program flow and execution of commands
 * @author Wilson, Huiwen, Shuan Siang, Michael 
 *
 */

public class MyTasksLogic implements ILogic {
	
	//Private variables
	MyTasksParser mParser;
	MyTasksStorage mStorage;
	
	//Constructor
	public MyTasksLogic(){
		initLogic();
	}
	
	/**
	 * initProgram initializes all local variables to prevent and data overflow from previous sessions
	 */
	private void initLogic(){
		//TODO init local variables
		//TODO init parser object
		//TODO init Storage object 
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public String executeCommand(String input) {
		
		CommandType commandObject = parseInput(input);
		String output = "";
		
		switch(commandObject.getType()) {
			case "add":
				addCommand();
				return output + " added";
			case "delete":
				deleteCommand();
				return output + " deleted";
			case "sort":
				sortCommand();
				return output + " sorted";
			case "search":
				searchCommand();
				return output + " search";
			default:
				return invalidCommand();
		}
	}

	private void searchCommand() {
		// TODO Auto-generated method stub
		
	}

	private void sortCommand() {
		// TODO Auto-generated method stub
		
	}

	private void addCommand() {
		// TODO Auto-generated method stub
		
	}

	private void deleteCommand() {
		// TODO Auto-generated method stub
		
	}

	private void invalidCommand() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */	
	public CommandType parseInput(String userInput) {
		CommandType input = mParser.parseInput(userInput);
		return input;
	}


}
