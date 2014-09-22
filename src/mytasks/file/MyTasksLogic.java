package mytasks.file;
import java.util.ArrayList;

/**
 * MyTasksLogic handles all logic related operations such as program flow and execution of commands
 * @author Wilson, Huiwen, Shuan Siang, Michael 
 *
 */

public class MyTasksLogic implements ILogic {
	
	//Constructor
	public MyTasksLogic(){
		initLogic();
	}
	
	/**
	 * initProgram initializes all local variables to prevent and data overflow from previous sessions
	 */
	private void initLogic(){
		//TODO init local variables
		//TODO init Storage object 
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public String executeCommand(String input) {
		// TODO Auto-generated method stub
		CommandType command = parseInput(input); 
		switch(command.getType()) {
			case "add":
				addCommand();
				break;
			case "delete":
				deleteCommand();
				break;
			case "sort":
				sortCommand();
				break;
			case "search":
				searchCommand();
				break;
			case "invalid":
				invalidCommand();
				break;
		}
		return null;
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
		// TODO call Parser.parseinput
		CommandType input = MyTasksParser.parseInput(userInput);
		return input;
	}


}
