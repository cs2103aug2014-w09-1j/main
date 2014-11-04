package mytasks.logic;

import java.util.ArrayList;

import mytasks.parser.IParser;
import mytasks.parser.MyTasksParser;
import mytasks.storage.MyTasksStorage;

/**
 * MyTasksLogic handles all logic related operations such as program flow and
 * execution of commands
 * 
 * @author Wilson, Huiwen, Michael
 *
 */

public class MyTasksLogicController implements ILogic {

	// Private variables
	private IParser mParser;
	private LocalMemory mLocalMem;
	private MemorySnapshotHandler mViewHandler;
	private boolean isDeveloper;
	private static MyTasksLogicController INSTANCE = null;

	// Constructor
	private MyTasksLogicController(boolean isDeveloper) {
		initLogic(isDeveloper);
	}
	
	public static MyTasksLogicController getInstance(boolean isDeveloper){
		if (INSTANCE == null){
			INSTANCE= new MyTasksLogicController(isDeveloper);
		}
		return INSTANCE;
	}
	
	protected Object readResolve() {
		return INSTANCE;
	}

	/**
	 * initProgram initializes all local variables to prevent and data overflow
	 * from previous sessions
	 */
	private void initLogic(boolean isDev) {
		isDeveloper = isDev;
		mParser = new MyTasksParser();
		MyTasksStorage.getInstance();
		mLocalMem = LocalMemory.getInstance();
		if (!isDeveloper) {
			mLocalMem.loadLocalMemory();
		}
		mViewHandler = MemorySnapshotHandler.getInstance();
	}

	/**
	 * {@inheritDoc}
	 */
	public String executeCommand(String input) {
		mLocalMem.print();
		Command commandObject = parseInput(input);
		// String output = removeFirstWord(input);

		if (commandObject == null) {
			return "Invalid input";
		}
		String feedback = commandObject.execute();
		//mLocalMem.print();
		return feedback;
		
		// Don't remove this chunk yet. Useful for adding into individual command sections
		// switch (commandObject.getType()) {
		// case ADD:
		// // a boolean to make sure that commandobject will only be
		// // put into stack only if it is not an undo operation
		// // addCommand(commandObject);
		// commandObject.execute();
		// // putToUndoStack(commandObject);
		// if (!isDeveloper) {
		// mLocalMem.saveLocalMemory();
		// }
		// return output + " added";
		// case DELETE:
		// // putToUndoStack(commandObject);
		// deleteCommand(commandObject);
		// if (!isDeveloper) {
		// mLocalMem.saveLocalMemory();
		// }
		// return output + " deleted";
		// case UPDATE:
		// // putToUndoStack(commandObject);
		// updateCommand(commandObject);
		// if (!isDeveloper) {
		// mLocalMem.saveLocalMemory();
		// }
		// output = input.replace(input.trim().split("[-]+")[0], "").trim();
		// output = output.replace(output.trim().split("\\s+")[0], "").trim();
		// // mLocalMem.print();
		// return output + " updated";
		// case SORT:
		// sortCommand(commandObject);
		// return output + " sorted";
		// case SEARCH:
		// boolean isFound = searchCommand(commandObject);
		// if (isFound) {
		// return String.format(MESSAGE_SEARCH_SUCCESS, output);
		// } else {
		// return String.format(MESSAGE_SEARCH_FAIL, output);
		// }
		// case UNDO:
		// commandObject.execute();
		// if (!isDeveloper) {
		// mLocalMem.saveLocalMemory();
		// }
		// return "";
		// case REDO:
		// commandObject.execute();
		// if (!isDeveloper) {
		// mLocalMem.saveLocalMemory();
		// }
		// return "";
		// default:
		// return "invalid command";
		// }
	}

	// private String removeFirstWord(String input) {
	// int i = input.indexOf(' ');
	// return input.substring(i).trim();
	// }

	// private void addCommand(Command commandObject) {
	// mLocalMem.add(commandObject.getTask());
	// }
	//
	// private void deleteCommand(Command commandObject) {
	// mLocalMem.remove(commandObject.getTask());
	// }
	//
	// private void updateCommand(Command commandObject) {
	// mLocalMem.update(commandObject.getToUpdateTaskDesc(),
	// commandObject.getTask());
	// }
	//
	// private void sortCommand(Command commandObject) {
	// mViewHandler.setView(commandObject.getTask().getLabels());
	// }
	//
	// private boolean searchCommand(Command commandObject) {
	// return mLocalMem.search(commandObject.getTask());
	// }

	/**
	 * parseInput calls the parser to read and understand user input
	 * 
	 * @param userInput
	 * @return CommandType object that contains the relevant fields
	 */
	private Command parseInput(String userInput) {
		Command input = mParser.parseInput(userInput);
		return input;
	}

	/**
	 * {@inheritDoc}
	 */
	public ArrayList<String> obtainPrintableOutput() {
		return mViewHandler.getSnapshot(mLocalMem);
	}

	protected LocalMemory getMemory() {
		return mLocalMem;
	}
	
	protected MemorySnapshotHandler getView(){
		return mViewHandler;
	}

	@Override
	public ArrayList<String> obtainAllTaskDescription() {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i<mLocalMem.getLocalMem().size(); i++) {
			String curDesc = mLocalMem.getLocalMem().get(i).getDescription();
			result.add(curDesc);
		}
		return result;
	}

}