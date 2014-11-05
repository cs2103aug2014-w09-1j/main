package mytasks.logic;

//@author A0114302A
import java.util.ArrayList;
import java.util.List;

import mytasks.file.FeedbackObject;
import mytasks.parser.IParser;
import mytasks.parser.MyTasksParser;
import mytasks.storage.MyTasksStorage;

/**
 * MyTasksLogic handles all logic related operations such as program flow and
 * execution of commands
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
	public FeedbackObject executeCommand(String input) {
		mLocalMem.print();
		Command commandObject = parseInput(input);
		// String output = removeFirstWord(input);

		if (commandObject == null) {
			FeedbackObject result = new FeedbackObject("Invalid input", false);
			return result;
		}
		FeedbackObject feedback = commandObject.execute();
		//mLocalMem.print();
		return feedback;
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
	public List<String> obtainPrintableOutput() {
		return mViewHandler.getSnapshot(mLocalMem);
	}

	protected LocalMemory getMemory() {
		return mLocalMem;
	}
	
	protected MemorySnapshotHandler getView(){
		return mViewHandler;
	}

	//@author A0114302A
	/**
	 * {@inheritDoc}
	 */
	public List<String> obtainAllTaskDescription() {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i<mLocalMem.getLocalMem().size(); i++) {
			String curDesc = mLocalMem.getLocalMem().get(i).getDescription();
			result.add(curDesc);
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<String> obtainAllLabels() {
		ArrayList<String> labelsResult = new ArrayList<String>();
		for (int i = 0; i<mLocalMem.getLocalMem().size(); i++) {
			if(mLocalMem.getLocalMem().get(i).getLabels() != null) {
				if(!mLocalMem.getLocalMem().get(i).getLabels().isEmpty()) {
					for(int k = 0; k < mLocalMem.getLocalMem().get(i).getLabels().size(); k++) {
						String curLabels = mLocalMem.getLocalMem().get(i).getLabels().get(k);
						labelsResult.add(curLabels);
					}
				}
			}			
		}
		return labelsResult;
	}
}
