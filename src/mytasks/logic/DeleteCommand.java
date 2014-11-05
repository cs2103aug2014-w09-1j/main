package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

/**
 * DeleteCommand extends Command to follow OOP standards
 * 
 * @author Huiwen, Shuan Siang
 *
 */

//@author A0108543J
public class DeleteCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private static String MESSAGE_DELETE_FAIL = "Task '%1$s' does not exist. Unable to delete";
	private static String MESSAGE_DELETE_SUCCESS = "'%1$s' deleted";

	public DeleteCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	FeedbackObject execute() {
		if (haveSearched == true
				&& isNumeric(super.getTaskDetails())
				&& Integer.parseInt(super.getTaskDetails()) - 1 < (mLocalMem
						.getSearchList().size())) {
			FeedbackObject feedback = new DeleteCommand(mLocalMem.getSearchList()
					.get(Integer.parseInt(super.getTaskDetails()) - 1)
					.getDescription(), null, null, null, null).execute();
			haveSearched = false;
			return feedback;
		}
		boolean hasTask = false;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(super.getTask().getDescription())) {
				hasTask = true;
				Task currentTask = mLocalMem.getLocalMem().get(i);
				Command commandToUndo = new DeleteCommand(
						currentTask.getDescription(),
						currentTask.getFromDateTime(),
						currentTask.getToDateTime(), currentTask.getLabels(),
						null);
				mLocalMem.undoPush(commandToUndo);
				mLocalMem.remove(super.getTask());
				break;
			}
		}

		haveSearched = false;
		mLocalMem.saveLocalMemory();
		if (hasTask) {
			String resultString = String
					.format(MESSAGE_DELETE_SUCCESS, super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString,true);
		} else {
			String resultString = String.format(MESSAGE_DELETE_FAIL, super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString,false);
		}
		return null;
	}

	@Override
	FeedbackObject undo() {
		Task prevState = super.getTask();
		Command toRedo = new DeleteCommand(prevState.getDescription(), null,
				null, null, null);
		mLocalMem.add(prevState);
		mLocalMem.redoPush(toRedo);
		mLocalMem.saveLocalMemory();
		String resultString = this.getTask().getDescription() + " added"; 
		FeedbackObject result = new FeedbackObject(resultString, true); 
		return result;
	}

	public static boolean isNumeric(String str) {
		try {
			int i = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
