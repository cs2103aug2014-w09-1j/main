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
		String output = removeFirstWord(input);
		
		switch(commandObject.getType()) {
			case ADD:
				addCommand(commandObject);
				return output + " added";
			case DELETE:
				deleteCommand(commandObject);
				return output + " deleted";
			case UPDATE:
				updateCommand(commandObject);
				return output + " updated"; 
			case SORT:
				sortCommand(commandObject);
				return output + " sorted";
			case SEARCH:
				searchCommand(commandObject);
				return output + " search";
			default:
				return "invalid command";
		}
	}
	
	private static String removeFirstWord(String input) {
		return input.replace(input.trim().split("\\s+")[0], "").trim();
	}
	
	private void addCommand(CommandInfo commandObject) {
		mLocalMem.add(commandObject.getTask());
	}

	private void deleteCommand(CommandInfo commandObject) {
		mLocalMem.remove(commandObject.getTask());
	}

	private void updateCommand(CommandInfo commandObject) {
		// either update task desc 
		// or update task desc and label (delete all labels prior to this) 
		// for update, I assume the parser will send in this format: task1, task2, labels (if any)
		// the updatedDesc will be labeled as updateDesc 
		mLocalMem.update(commandObject.getTask());
	}

	private void sortCommand(CommandInfo commandObject) {
		// TODO Auto-generated method stub
		
	}

	private void searchCommand(CommandInfo commandObject) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */	
	public CommandInfo parseInput(String userInput) {
		CommandInfo input = mParser.parseInput(userInput);
		return input;
	}

	/**
	 * {@inheritDoc}
	 */
	public String obtainPrintableOutput() {
		return mViewHandler.getSnapshot();
	}
}
