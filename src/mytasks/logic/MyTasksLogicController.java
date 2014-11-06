package mytasks.logic;

//@author A0114302A
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import mytasks.file.FeedbackObject;
import mytasks.parser.IParser;
import mytasks.parser.MyTasksParser;
import mytasks.storage.MyTasksStorage;

/**
 * MyTasksLogic handles all logic related operations such as program flow and
 * execution of commands
 */

@SuppressWarnings("serial")
public class MyTasksLogicController implements ILogic, Serializable {

	// Private variables
	private IParser mParser;
	private LocalMemory mLocalMem;
	private MemorySnapshotHandler mViewHandler;
	private boolean isDeveloper;
	private final String MESSAGE_UNSUP = "Unsupported command function";
	private static MyTasksLogicController INSTANCE = null;
	private HideCommand mHideCommand;
	private static final Logger LOGGER = Logger.getLogger(MyTasksStorage.class
			.getName());
	private Handler fh = null;

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
	
	private void runLogger() {
		try {
			fh = new FileHandler(mytasks.file.MyTasksController.default_log, 0,
					1, true);
			LOGGER.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			LOGGER.setUseParentHandlers(false);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void closeHandler() {
		fh.flush();
		fh.close();
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
		FeedbackObject feedback = null;
		try {
			feedback = commandObject.execute();
		} catch (UnsupportedOperationException e) {
			runLogger();
			LOGGER.log(Level.SEVERE, MESSAGE_UNSUP, e);
			closeHandler();
		}
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
	
	//@author A0108543J
	public List<String> obtainPrintableOutputAfterHide() {
		return mHideCommand.getList();
	}
	
}
