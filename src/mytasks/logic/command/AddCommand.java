package mytasks.logic.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.logic.LocalMemory;

//@author A0114302A
/**
 * AddCommand extends Command object to follow OOP standards
 */
public class AddCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private static final Logger LOGGER = Logger.getLogger(AddCommand.class
			.getName());
	private Handler fh = null;

	public AddCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
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

	//@author A0108543J
	@Override
	public FeedbackObject execute() {
		mLocalMem.add(super.getTask());
		AddCommand commandToUndo = new AddCommand(null, null, null, null, super
				.getTask().getDescription());
		mLocalMem.undoPush(commandToUndo);
		mLocalMem.saveLocalMemory();
		haveSearched = false;
		String resultString = super.getTaskDetails() + " added";
		FeedbackObject result = new FeedbackObject(resultString, true); 
		return result;
	}
	
	//@author A0114302A
	@Override
	public FeedbackObject undo() {
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(this.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				mLocalMem.getLocalMem().remove(i);
				break;
			}
		}
		if (prevState == null){
			runLogger();
			LOGGER.log(Level.SEVERE, Command.MESSAGE_NOTASK);
			closeHandler();
			return null;
		} 
		Command toRedo = new AddCommand(prevState.getDescription(),
				prevState.getFromDateTime(), prevState.getToDateTime(),
				prevState.getLabels(), null);
		mLocalMem.saveLocalMemory();
		mLocalMem.redoPush(toRedo);
		String resultString =this.getToUpdateTaskDesc() + " deleted";
		FeedbackObject result = new FeedbackObject(resultString, true); 
		return result;
	}
}
