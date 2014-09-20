package mytasks.file;

/**
 * MyTasksLogic handles all logic related operations such as program flow and execution of commands
 * @author Wilson
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
		return null;
	}

	/**
	 * {@inheritDoc}
	 */	
	public CommandType parseInput(String userInput) {
		// TODO call Parser.parseinput
		return null;
	}


}
