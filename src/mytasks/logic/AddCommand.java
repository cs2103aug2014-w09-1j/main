package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

/**
 * AddCommand extends Command object to follow OOP standards
 * 
 * @author Huiwen, Shuan Siang 
 *
 */
public class AddCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;

	public AddCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	//@author A0108543J
	@Override
	FeedbackObject execute() {
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
	FeedbackObject undo() {
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(this.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				mLocalMem.getLocalMem().remove(i);
				break;
			}
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
