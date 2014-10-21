package mytasks.logic;

import mytasks.file.Task;
import mytasks.parser.IParser;
import mytasks.parser.MyTasksParser;
import mytasks.storage.IStorage;
import mytasks.storage.MyTasksStorage;

/**
 * MyTasksLogic handles all logic related operations such as program flow and
 * execution of commands
 * 
 * @author Wilson, Huiwen, Michael
 *
 */

public class MyTasksLogic implements ILogic {

	// Private variables
	private IParser mParser;
	private IStorage mStorage;
	private LocalMemory mLocalMem;
	private MemorySnapshotHandler mViewHandler;
	private boolean isDeveloper;
	private static String MESSAGE_SEARCH_FAIL = "unable to find task with keyword '%1$s'";
	private static String MESSAGE_SEARCH_SUCCESS = "task(s) with keyword '%1$s' searched";

	// TODO: introduce get Instance to all classes. This included. Read
	// notes/google this
	// Difference between creating a public getInstance for checking yourself,
	// and getinstance for your member variables
	// Constructor
	public MyTasksLogic(boolean isDeveloper) {
		initLogic(isDeveloper);
	}

	/**
	 * initProgram initializes all local variables to prevent and data overflow
	 * from previous sessions
	 */
	private void initLogic(boolean isDev) {
		isDeveloper = isDev;
		mParser = new MyTasksParser();
		mStorage = MyTasksStorage.getInstance();
		mLocalMem = LocalMemory.getInstance();
		if (!isDeveloper) {
			mLocalMem.loadLocalMemory();
		}
		mViewHandler = new MemorySnapshotHandler();
	}

	/**
	 * {@inheritDoc}
	 */
	public String executeCommand(String input) {

		Command commandObject = parseInput(input);
		String output = removeFirstWord(input);

		switch (commandObject.getType()) {
		case ADD:
			// a boolean to make sure that commandobject will only be
			// put into stack only if it is not an undo operation
			// addCommand(commandObject);
			commandObject.execute();
			// putToUndoStack(commandObject);
			if (!isDeveloper) {
				mLocalMem.saveLocalMemory();
			}
			return output + " added";
		case DELETE:
			// putToUndoStack(commandObject);
			deleteCommand(commandObject);
			if (!isDeveloper) {
				mLocalMem.saveLocalMemory();
			}
			return output + " deleted";
		case UPDATE:
			// putToUndoStack(commandObject);
			updateCommand(commandObject);
			if (!isDeveloper) {
				mLocalMem.saveLocalMemory();
			}
			output = input.replace(input.trim().split("[-]+")[0], "").trim();
			output = output.replace(output.trim().split("\\s+")[0], "").trim();
			// mLocalMem.print();
			return output + " updated";
		case SORT:
			sortCommand(commandObject);
			return output + " sorted";
		case SEARCH:
			boolean isFound = searchCommand(commandObject);
			if (isFound) {
				return String.format(MESSAGE_SEARCH_SUCCESS, output);
			} else {
				return String.format(MESSAGE_SEARCH_FAIL, output);
			}
		case UNDO:
			undoCommand();
			if (!isDeveloper) {
				mLocalMem.saveLocalMemory();
			}
			return "";
		case REDO:
			redoCommand();
			if (!isDeveloper) {
				mLocalMem.saveLocalMemory();
			}
			return "";
		default:
			return "invalid command";
		}
	}

	// TODO confused to make stack of command type or string type
	private void putToUndoStack(Command commandObject) {
		// TODO Auto-generated method stub
		Command commandToUndo;

		switch (commandObject.getType()) {
		case ADD:
			commandToUndo = new DeleteCommand(null, null, null, null, null,
					commandObject.getTask().getDescription());
			mLocalMem.push(commandToUndo);
			return;
		case DELETE:
			for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
				if (mLocalMem.getLocalMem().get(i).getDescription()
						.equals(commandObject.getTask().getDescription())) {
					commandToUndo = new AddCommand("add", commandObject
							.getTask().getDescription(), mLocalMem
							.getLocalMem().get(i).getFromDateTime(), mLocalMem
							.getLocalMem().get(i).getToDateTime(), mLocalMem
							.getLocalMem().get(i).getLabels(), null);
					mLocalMem.push(commandToUndo);
					break;
				}
			}
			return;
		case UPDATE:
			for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
				if (mLocalMem.getLocalMem().get(i).getDescription()
						.equals(commandObject.getToUpdateTaskDesc())) {
					commandToUndo = new UpdateCommand(null, mLocalMem
							.getLocalMem().get(i).getDescription(), mLocalMem
							.getLocalMem().get(i).getFromDateTime(), mLocalMem
							.getLocalMem().get(i).getToDateTime(), mLocalMem
							.getLocalMem().get(i).getLabels(), commandObject
							.getTask().getDescription());
					mLocalMem.push(commandToUndo);
					break;
				}
			}
			return;
		default:
			return;
		}
	}

	private String removeFirstWord(String input) {
		int i = input.indexOf(' ');
		return input.substring(i).trim();
	}

	private void addCommand(Command commandObject) {
		mLocalMem.add(commandObject.getTask());
	}

	private void deleteCommand(Command commandObject) {
		mLocalMem.remove(commandObject.getTask());
	}

	private void updateCommand(Command commandObject) {
		mLocalMem.update(commandObject.getToUpdateTaskDesc(),
				commandObject.getTask());
	}

	private void sortCommand(Command commandObject) {
		mViewHandler.setView(commandObject.getTask().getLabels());
	}

	private boolean searchCommand(Command commandObject) {
		return mLocalMem.search(commandObject.getTask());
	}

	private void undoCommand() {
		// TODO Auto-generated method stub
		mLocalMem.undo();
	}

	private void redoCommand() {
		// TODO Auto-generated method stub
		mLocalMem.redo();

	}

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
	public String obtainPrintableOutput() {
		return mViewHandler.getSnapshot(mLocalMem);
	}

	protected LocalMemory getMemory() {
		return mLocalMem;
	}
}
