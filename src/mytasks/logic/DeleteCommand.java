package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

//@author A0108543J
/**
 * DeleteCommand extends Command to follow OOP standards
 */

public class DeleteCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private static String MESSAGE_DELETE_FAIL = "Task '%1$s' does not exist. Unable to delete. Auto search for similar tasks.";
	private static String MESSAGE_DELETE_SUCCESS = "'%1$s' deleted";
	private static String MESSAGE_UPDATE_DUPLICATE = "There are multiple tasks '%1$s'. Auto search to delete the specific one";

	public DeleteCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	FeedbackObject execute() {
		if (canDeleteFromSearchResults()) {
			FeedbackObject result = deleteFromSearchResults();
			return result;
		}

		int timesAppear = countTimesAppear();
		if (timesAppear == 0){
			String resultString = String.format(MESSAGE_DELETE_FAIL,
					super.getTaskDetails()) + "\n";
			resultString += autoSearch().getFeedback();
			FeedbackObject result = new FeedbackObject(resultString, false);
			return result;
		}
		else if (timesAppear > 1) {	
			String resultString = String.format(MESSAGE_UPDATE_DUPLICATE,
					super.getTaskDetails()) + "\n";
			resultString += autoSearch().getFeedback();
			FeedbackObject result = new FeedbackObject(resultString, true);
			return result;
		} else {
			deleteSingleTask();
		} 

		haveSearched = false;
		mLocalMem.saveLocalMemory();
		String resultString = String
				.format(MESSAGE_DELETE_SUCCESS, super.getTaskDetails());
		FeedbackObject result = new FeedbackObject(resultString,true);
		return result;
	}

	public void deleteSingleTask(){
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(super.getTask().getDescription())) {
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
	}

	public int countTimesAppear() {
		int count = 0;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (super.getTaskDetails().equals(
					mLocalMem.getLocalMem().get(i).getDescription())) {
				count++;
			}
		}
		return count;
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

	//@author A0112139R
	private boolean canDeleteFromSearchResults(){
		if (haveSearched == true && isNumeric(super.getTaskDetails())
				&& Integer.parseInt(super.getTaskDetails()) - 1 < (mLocalMem.getSearchList().size())) {
			return true;
		}
		return false;
	}

	private FeedbackObject deleteFromSearchResults(){
		Task taskToDeleted = mLocalMem.getLocalMem()
				.get(mLocalMem.getSearchList().get(Integer.parseInt(super.getTaskDetails())-1));
		Command commandToUndo = new DeleteCommand(taskToDeleted.getDescription(),
				taskToDeleted.getFromDateTime(),taskToDeleted.getToDateTime(), 
				taskToDeleted.getLabels(), null);
		mLocalMem.undoPush(commandToUndo);
		mLocalMem.remove(mLocalMem.getSearchList().get(Integer.parseInt(super.getTaskDetails())-1));
		haveSearched = false;
		mLocalMem.saveLocalMemory();
		String resultString = String.format(MESSAGE_DELETE_SUCCESS, taskToDeleted.getDescription());
		FeedbackObject feedback = new FeedbackObject(resultString, true);
		return feedback;
	}

	private boolean isNumeric(String str) {
		try {
			int i = Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private FeedbackObject autoSearch(){
		Task task = super.getTask();
		FeedbackObject result = new SearchCommand(task.getDescription(), task.getFromDateTime(), task.getToDateTime(), 
				task.getLabels(), null).execute();
		return result;
	}
}
