package mytasks.logic;

import mytasks.file.CommandInfo;
import mytasks.parser.MyTasksParser;
import mytasks.storage.MyTasksStorage;

/**
 * MyTasksLogic handles all logic related operations such as program flow and execution of commands
 * @author Wilson, Huiwen, Shuan Siang, Michael 
 *
 */

public class MyTasksLogic implements ILogic {
	
	//Private variables
	MyTasksParser mParser;
	MyTasksStorage mStorage;
	LocalMemory mLocalMem;
	MemorySnapshotHandler mViewHandler;
	
	//Constructor
	public MyTasksLogic(){
		initLogic();
	}
	
	/**
	 * initProgram initializes all local variables to prevent and data overflow from previous sessions
	 */
	private void initLogic(){
		mParser = new MyTasksParser();
		mStorage = new MyTasksStorage();
		mLocalMem = new LocalMemory();
		mLocalMem.loadLocalMemory();
		mViewHandler = new MemorySnapshotHandler();
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public String executeCommand(String input) {
		
		CommandInfo commandObject = parseInput(input);
		String output = "";
		
		switch(commandObject.getType()) {
			case ADD: //REMOVE COMMENT WHEN READ. Enum types are found in CommandInfo. Refer there
				addCommand();
				return output + " added";
			case DELETE:
				deleteCommand();
				return output + " deleted";
			case SORT:
				sortCommand();
				return output + " sorted";
			case SEARCH:
				searchCommand();
				return output + " search";
			default:
				invalidCommand();
				return "invalid command";
		}
	}

	/**
	 * {@inheritDoc}
	 */	
	public CommandInfo parseInput(String userInput) {
		CommandInfo input = mParser.parseInput(userInput);
		return input;
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
	public String obtainPrintableOutput() {
		return mViewHandler.getSnapshot();
	}
}
