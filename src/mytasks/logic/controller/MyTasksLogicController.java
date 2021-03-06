package mytasks.logic.controller;

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
import mytasks.logic.command.Command;
import mytasks.logic.parser.IParser;
import mytasks.logic.parser.MyTasksParser;
import mytasks.storage.MyTasksStorage;

//@author A0114302A
/**
 * MyTasksLogic handles all logic related operations such as program flow and
 * execution of commands
 */

@SuppressWarnings("serial")
public class MyTasksLogicController implements ILogic, Serializable {
	
	private IParser mParser;
	private LocalMemory mLocalMem;
	private MemorySnapshotHandler mViewHandler;
	private boolean isDeveloper;
	private static MyTasksLogicController INSTANCE = null;
	private static final Logger LOGGER = Logger.getLogger(MyTasksLogicController.class
			.getName());
	private Handler fh = null;
	protected boolean labelsHidden = false;
	protected boolean showHelp = false;
	protected ArrayList<String> toHide;
	private final String MESSAGE_UNSUP = "Unsupported command function";
	
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
	
	/**
	 * closeHandler prevents overflow of information and multiple logger files
	 * from appearing
	 */
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
		toHide = new ArrayList<String>();
	}

	/**
	 * {@inheritDoc}
	 */
	public FeedbackObject executeCommand(String input) {
		Command commandObject = parseInput(input);

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
	
	//@author A0115034X
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
	public void toggleHide(boolean hideOrNot) {
		labelsHidden = hideOrNot;
	}
	
	public void hideLabels(ArrayList<String> labels){
		toHide = labels;
	}
	
	public void clearHideLabels() {
		toHide.clear();
	}
	
	public ArrayList<String> getHideLabels() {
		return toHide;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkIfToHide() {
		return labelsHidden;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> labelsToHide() {
		return toHide;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean checkIfToHelpUI() {
		return showHelp;
	}

	/**
	 * {@inheritDoc}
	 */
	public void toggleHelpUI(boolean onOff) {
		showHelp = onOff;
	}

}
