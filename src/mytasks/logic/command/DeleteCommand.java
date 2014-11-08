package mytasks.logic.command;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.logic.controller.LocalMemory;

//@author A0112139R
/**
 * DeleteCommand extends Command to follow OOP standards
 */

public class DeleteCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private static String MESSAGE_DELETE_FAIL = "Task '%1$s' does not exist. Unable to delete. Auto search for similar tasks.";
	private static String MESSAGE_DELETE_SUCCESS = "'%1$s' deleted";
	private static String MESSAGE_UPDATE_DUPLICATE = "There are multiple tasks '%1$s'. Auto search to delete the specific one.";

	public DeleteCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	public FeedbackObject execute() {
		if (isRedo){
			int indexOfTaskToDeleted = mLocalMem.getLocalMem().size()-1;
			Task taskToDeleted = mLocalMem.getLocalMem().get(indexOfTaskToDeleted);
			DeleteCommand commandToUndo = createDeleteUndo(taskToDeleted);
			mLocalMem.undoPush(commandToUndo);
			mLocalMem.remove(indexOfTaskToDeleted);
			String resultString = String.format(MESSAGE_DELETE_SUCCESS, super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString, true);
			return result;
		}
		
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
			hasSearched = false;
			mLocalMem.saveLocalMemory();
			String resultString = String.format(MESSAGE_DELETE_SUCCESS, 
					super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString,true);
			return result;
		} 
	}

	//@author A0115034X
	private void deleteSingleTask(){
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(super.getTask().getDescription())) {
				Task currentTask = mLocalMem.getLocalMem().get(i);
				DeleteCommand commandToUndo = createDeleteUndo(currentTask);
				mLocalMem.undoPush(commandToUndo);
				mLocalMem.remove(super.getTask());
				break;
			}
		}
	}

	private DeleteCommand createDeleteUndo(Task currentTask){
		DeleteCommand commandToUndo = new DeleteCommand(currentTask.getDescription(),
				currentTask.getFromDateTime(), currentTask.getToDateTime(), 
				currentTask.getLabels(), null);
		return commandToUndo;
	}

	private int countTimesAppear() {
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
	public FeedbackObject undo() {
		Task prevState = super.getTask();
		Command toRedo = new DeleteCommand(prevState.getDescription(), 
				null, null, null, null);
		mLocalMem.add(prevState);
		mLocalMem.redoPush(toRedo);
		mLocalMem.saveLocalMemory();
		String resultString = this.getTask().getDescription() + " added"; 
		FeedbackObject result = new FeedbackObject(resultString, true); 
		return result;
	}

	//@author A0112139R
	private boolean canDeleteFromSearchResults(){
		if (hasSearched == true && isNumeric(super.getTaskDetails())
				&& Integer.parseInt(super.getTaskDetails()) - 1 < (mLocalMem.getSearchList().size())) {
			return true;
		}
		return false;
	}

	private FeedbackObject deleteFromSearchResults(){
		int indexOfTaskToDeleted = mLocalMem.getSearchList().get(Integer.parseInt(super.getTaskDetails())-1);
		Task taskToDeleted = mLocalMem.getLocalMem().get(indexOfTaskToDeleted);
		DeleteCommand commandToUndo = createDeleteUndo(taskToDeleted);
		mLocalMem.undoPush(commandToUndo);
		mLocalMem.remove(indexOfTaskToDeleted);
		mLocalMem.saveLocalMemory();
		hasSearched = false;
		String resultString = String.format(MESSAGE_DELETE_SUCCESS, taskToDeleted.getDescription());
		FeedbackObject feedback = new FeedbackObject(resultString, true);
		return feedback;
	}

	private boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private FeedbackObject autoSearch(){
		Task task = super.getTask();
		FeedbackObject result = new SearchCommand(task.getDescription(), task.getFromDateTime(), 
				task.getToDateTime(), task.getLabels(), null).execute();
		return result;
	}
}
