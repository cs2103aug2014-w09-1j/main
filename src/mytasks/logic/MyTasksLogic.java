package mytasks.logic;

import mytasks.file.CommandInfo;
import mytasks.parser.MyTasksParser;
import mytasks.storage.MyTasksStorage;

/**
 * MyTasksLogic handles all logic related operations such as program flow and execution of commands
 * @author Wilson, Huiwen
 *
 */

public class MyTasksLogic implements ILogic {
	
	//Private variables
	MyTasksParser mParser;
	MyTasksStorage mStorage;
	LocalMemory mLocalMem;
	MemorySnapshotHandler mViewHandler;
	boolean isDeveloper;
	
	//Constructor
	public MyTasksLogic(boolean isDeveloper){
		initLogic(isDeveloper);
	}

	/**
	 * initProgram initializes all local variables to prevent and data overflow from previous sessions
	 */
	private void initLogic(boolean isDev){
		isDeveloper = isDev;
		mParser = new MyTasksParser();
		mStorage = new MyTasksStorage();
		mLocalMem = new LocalMemory();
		if (!isDeveloper) {
			mLocalMem.loadLocalMemory();	
		}
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
				if(!isDeveloper){
					mLocalMem.saveLocalMemory(); 
				}
				return output + " added";
			case DELETE:
				deleteCommand(commandObject);
				if(!isDeveloper){
					mLocalMem.saveLocalMemory(); 
				}				
				return output + " deleted";
			case UPDATE:
				updateCommand(commandObject);
				if(!isDeveloper){
					mLocalMem.saveLocalMemory(); 
				}				
				output = input.replace(input.trim().split("[-]+")[0], "").trim();	
				output = output.replace(output.trim().split("\\s+")[0], "").trim();		
				return output + " updated";
			case SORT:
				sortCommand(commandObject);
				return output + " sorted";
			case SEARCH:
				searchCommand(commandObject);
				return output + " search";
			case UNDO:
				undoCommand();
				return "";
			case REDO:
				redoCommand();
				return "";
			default:
				return "invalid command";
		}
	}

	private static String removeFirstWord(String input) {
		int i = input.indexOf(' ');
		return input.substring(i).trim();
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
		mLocalMem.update(commandObject.getToUpdateTaskDesc(), commandObject.getTask());
	}

	private void sortCommand(CommandInfo commandObject) {
		// TODO Auto-generated method stub
		
	}

	private void searchCommand(CommandInfo commandObject) {
			
	}
	
	private void undoCommand() {
		// TODO Auto-generated method stub
		
	}
	
	private void redoCommand() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * parseInput calls the parser to read and understand user input 
	 * @param userInput
	 * @return CommandType object that contains the relevant fields
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
