package mytasks.logic.command;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.logic.controller.LocalMemory;

//@author A0112139R
/**
 * UpdateCommand extends Command to follow OOP standards
 */
public class UpdateCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private static String MESSAGE_UPDATE_FAIL = "Task '%1$s' does not exist. Unable to update. Auto search for similar tasks.";
	private static String MESSAGE_UPDATE_SUCCESS = "'%1$s' updated";
	private static String MESSAGE_UPDATE_DUPLICATE = "There are multiple tasks '%1$s'. Auto search to update the specific one.";

	public UpdateCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	public FeedbackObject execute() {
		if (isRedo) {
			return redoTask();
		} else if (canUpdateFromSearchResults()) {
			return updateFromSearchResults();
		} else {
			return updateByTaskDesc();
		}
	}

	private FeedbackObject redoTask(){
		int indexOfTaskToUpdated = 0;
		Task taskToUpdated = mLocalMem.getLocalMem().get(indexOfTaskToUpdated);
		Command commandToUndo = createUpdateUndo(taskToUpdated);
		mLocalMem.undoPush(commandToUndo);
		Task updatedTask = updateTask(super.getTask(), taskToUpdated);
		mLocalMem.getLocalMem().remove(indexOfTaskToUpdated);
		mLocalMem.getLocalMem().add(updatedTask);
		mLocalMem.saveLocalMemory();
		String resultString = String.format(MESSAGE_UPDATE_SUCCESS,
				super.getToUpdateTaskDesc());
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	private boolean canUpdateFromSearchResults() {
		if (hasSearched == true && isNumeric(super.getToUpdateTaskDesc())
				&& Integer.parseInt(super.getToUpdateTaskDesc()) - 1 < (searchList.size())) {
			return true;
		}
		return false;
	}

	private FeedbackObject updateFromSearchResults() {
		int indexOfTaskToUpdated = searchList.get(Integer.parseInt(super.getToUpdateTaskDesc()) - 1);
		Task taskToUpdated = mLocalMem.getLocalMem().get(indexOfTaskToUpdated);

		UpdateCommand commandToUndo = createUpdateUndo(taskToUpdated);
		mLocalMem.undoPush(commandToUndo);

		Task currentTask = super.getTask();
		Task updatedTask = updateTask(currentTask, taskToUpdated);
		mLocalMem.getLocalMem().remove(indexOfTaskToUpdated);
		mLocalMem.getLocalMem().add(updatedTask);

		hasSearched = false;
		mLocalMem.saveLocalMemory();
		String resultString = String.format(MESSAGE_UPDATE_SUCCESS,
				taskToUpdated.getDescription());
		FeedbackObject feedback = new FeedbackObject(resultString, true);
		return feedback;
	}

	private FeedbackObject updateByTaskDesc(){
		int timesAppear = countTimesAppear();
		if (timesAppear == 0) {
			return updateFail();
		} else if (timesAppear > 1) {
			return directToSearch();
		} else {
			return updateSingleTask();
		}
	}

	private FeedbackObject updateFail(){
		String resultString = String.format(MESSAGE_UPDATE_FAIL,
				super.getToUpdateTaskDesc()) + "\n";
		resultString += autoSearch().getFeedback();
		FeedbackObject result = new FeedbackObject(resultString, false);
		return result;
	}

	private FeedbackObject directToSearch(){
		String resultString = String.format(MESSAGE_UPDATE_DUPLICATE,
				super.getToUpdateTaskDesc()) + "\n";
		resultString += autoSearch().getFeedback();
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	// @author A0115034X
	private Task savePrevState() {
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(super.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				break;
			}
		}
		return prevState;
	}

	private FeedbackObject updateSingleTask() {
		Task prevState = savePrevState();
		UpdateCommand commandToUndo = createUpdateUndo(prevState);
		mLocalMem.undoPush(commandToUndo);

		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (super.getToUpdateTaskDesc().equals(
					mLocalMem.getLocalMem().get(i).getDescription())) {
				Task currentTask = super.getTask();
				Task updatedTask = updateTask(currentTask, mLocalMem
						.getLocalMem().get(i));
				mLocalMem.getLocalMem().remove(i);
				mLocalMem.getLocalMem().add(updatedTask);
			}
		}		

		mLocalMem.saveLocalMemory();
		hasSearched = false;
		String resultString = String.format(MESSAGE_UPDATE_SUCCESS,
				super.getToUpdateTaskDesc());
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	private Task updateTask(Task currentTask, Task taskToUpdated) {
		if (currentTask.getDescription() != null) {
			taskToUpdated.setDescription(currentTask.getDescription());
		}
		if (currentTask.getFromDateTime() != null
				&& currentTask.getToDateTime() != null) {
			taskToUpdated.setFromDateTime(currentTask.getFromDateTime());
			taskToUpdated.setToDateTime(currentTask.getToDateTime());
		}
		if (currentTask.getFromDateTime() != null
				&& currentTask.getToDateTime() == null) {
			taskToUpdated.setFromDateTime(currentTask.getFromDateTime());
			taskToUpdated.setToDateTime(null);
		}
		if (currentTask.getLabels() != null) {
			if (!super.getTask().getLabels().isEmpty()) {
				taskToUpdated.setLabels(currentTask.getLabels());
			}
		}
		Task updatedTask = taskToUpdated;
		return updatedTask;
	}

	private UpdateCommand createUpdateUndo(Task prevState) {
		UpdateCommand commandToUndo = null;
		if (this.getTaskDetails() == null) {
			commandToUndo = new UpdateCommand(prevState.getDescription(),
					prevState.getFromDateTime(), prevState.getToDateTime(),
					prevState.getLabels(), prevState.getDescription());
		} else {
			commandToUndo = new UpdateCommand(prevState.getDescription(),
					prevState.getFromDateTime(), prevState.getToDateTime(),
					prevState.getLabels(), super.getTaskDetails());
		}
		return commandToUndo;
	}

	private int countTimesAppear() {
		int count = 0;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (super.getToUpdateTaskDesc().equals(
					mLocalMem.getLocalMem().get(i).getDescription())) {
				count++;
			}
		}
		return count;
	}

	// @author A0108543J
	@Override
	public FeedbackObject undo() {
		Task prevState = null;
		int indexOfTaskToUpdate = mLocalMem.getLocalMem().size() - 1;
		prevState = mLocalMem.getLocalMem().get(indexOfTaskToUpdate).getClone();
		Command toRedo = new UpdateCommand(prevState.getDescription(),
				prevState.getFromDateTime(), prevState.getToDateTime(),
				prevState.getLabels(), this.getTask().getDescription());
		mLocalMem.getLocalMem().remove(indexOfTaskToUpdate);
		mLocalMem.getLocalMem().add(0, this.getTask());
		mLocalMem.redoPush(toRedo);
		mLocalMem.saveLocalMemory();
		String resultString = this.getToUpdateTaskDesc() + " reverted";
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	// @author A0112139R
	private boolean isNumeric(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private FeedbackObject autoSearch() {
		FeedbackObject result = new SearchCommand(super.getToUpdateTaskDesc(),
				null, null, null, null).execute();
		return result;
	}
}
