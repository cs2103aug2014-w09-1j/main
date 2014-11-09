package mytasks.logic.command;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.logic.controller.LocalMemory;

//@author A0112139R
/**
 * DoneCommand extends Command object to follow OOP standards
 */
public class DoneCommand extends Command {

	private LocalMemory mLocalMem;
	private static String MESSAGE_DONE_SUCCESS = "'%1$s' mark as done";
	private static String MESSAGE_DONE_FAIL = "Task '%1$s' does not exist. Unable to mark as done. Auto search for similar tasks.";
	private static String MESSAGE_DONE_ALREADY = "Task '%1$s' had already been marked as done.";
	private static String MESSAGE_DONE_DUPLICATE = "There are multiple tasks '%1$s'. Auto search to mark the specific one as done.";

	public DoneCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	public FeedbackObject execute() {
		if (isRedo){
			return redoTask();
		} else if (canDoneFromSearchResults()){
			return doneFromSearchResults();
		} else {
			return doneByTaskDesc();
		}
	}

	private FeedbackObject redoTask() {
		int indexOfTaskToDone = 0;
		Task taskToDone = mLocalMem.getLocalMem().get(indexOfTaskToDone);		
		Command commandToUndo = createDoneUndo(taskToDone);
		mLocalMem.undoPush(commandToUndo);	
		Task doneTask = markTaskAsDone(taskToDone);
		mLocalMem.getLocalMem().remove(indexOfTaskToDone);
		mLocalMem.getLocalMem().add(doneTask);
		mLocalMem.saveLocalMemory();			
		String resultString = String.format(MESSAGE_DONE_SUCCESS, taskToDone.getDescription());
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	private boolean canDoneFromSearchResults(){
		if (hasSearched == true && isNumeric(super.getTaskDetails()) && 
				Integer.parseInt(super.getTaskDetails())-1 < (searchList.size())){
			return true;
		}
		return false;
	}

	private FeedbackObject doneFromSearchResults(){
		int indexOfTaskToDone = searchList.get(Integer.parseInt(super.getTaskDetails())-1);
		Task taskToDone = mLocalMem.getLocalMem().get(indexOfTaskToDone);

		boolean hasDone = true;
		if (!isDone(taskToDone)){
			hasDone = false;
			DoneCommand commandToUndo = createDoneUndo(taskToDone);
			mLocalMem.undoPush(commandToUndo);
			markTaskAsDone(taskToDone);
			mLocalMem.getLocalMem().remove(indexOfTaskToDone);
			mLocalMem.getLocalMem().add(taskToDone);
			mLocalMem.saveLocalMemory();
		}
		hasSearched = false;
		if (hasDone){
			String resultString =  String.format(MESSAGE_DONE_ALREADY, taskToDone.getDescription());
			FeedbackObject result = new FeedbackObject(resultString,false);
			return result;
		} else {
			String resultString = String.format(MESSAGE_DONE_SUCCESS, taskToDone.getDescription());
			FeedbackObject result = new FeedbackObject(resultString,true);
			return result;
		}
	}

	private FeedbackObject doneByTaskDesc(){
		int timesAppear = countTimesAppear();
		if (timesAppear == 0){
			return doneFail();
		} else if (timesAppear > 1) {	
			return directToSearch();
		} else {
			return doSingleTask();
		}
	}

	private FeedbackObject doneFail(){
		String resultString = String.format(MESSAGE_DONE_FAIL,
				super.getTaskDetails()) + "\n";
		resultString += autoSearch().getFeedback();
		FeedbackObject result = new FeedbackObject(resultString, false);
		return result;
	}

	private FeedbackObject directToSearch(){
		String resultString = String.format(MESSAGE_DONE_DUPLICATE,
				super.getTaskDetails()) + "\n";
		resultString += autoSearch().getFeedback();
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
	}

	private FeedbackObject doSingleTask(){
		boolean hasDone = true;
		for (int i=0; i < mLocalMem.getLocalMem().size(); i++){
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(super.getTaskDetails())){
				Task currentTask = mLocalMem.getLocalMem().get(i);
				if (!isDone(currentTask)){
					hasDone = false;
					Command commandToUndo = createDoneUndo(currentTask);
					mLocalMem.undoPush(commandToUndo);
					markTaskAsDone(currentTask);
					mLocalMem.getLocalMem().remove(i);
					mLocalMem.getLocalMem().add(currentTask);
				}
				break;
			}
		}
		hasSearched = false;
		mLocalMem.saveLocalMemory();
		if (hasDone){
			String resultString =  String.format(MESSAGE_DONE_ALREADY, super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString,false);
			return result;
		} else {
			String resultString = String.format(MESSAGE_DONE_SUCCESS, super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString,true);
			return result;
		}
	}

	private Task markTaskAsDone(Task taskToDone) {
		ArrayList<String> labels = new ArrayList<String>();
		if (taskToDone.getLabels() != null){
			for (int i=0; i < taskToDone.getLabels().size(); i++){
				labels.add(taskToDone.getLabels().get(i));
			}
		}
		if (!isDone(taskToDone)){
			labels.add("done");
		}
		taskToDone.setLabels(labels);
		return taskToDone;
	}

	private boolean isDone(Task task){
		if (task.getLabels() == null){
			return false;
		}

		for (int i=0; i < task.getLabels().size(); i++){
			if (task.getLabels().get(i).toLowerCase().equals("done")){
				return true;
			}
		}
		return false;
	}

	private DoneCommand createDoneUndo(Task currentTask) {
		DoneCommand commandToUndo = new DoneCommand(currentTask.getDescription(),
				currentTask.getFromDateTime(), currentTask.getToDateTime(), 
				currentTask.getLabels(), null);
		return commandToUndo;
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
	public FeedbackObject undo() {
		Task prevState = null;
		int indexOfTaskToUndone = mLocalMem.getLocalMem().size()-1;
		prevState = mLocalMem.getLocalMem().get(indexOfTaskToUndone).getClone();
		Command toRedo = new DoneCommand(prevState.getDescription(),
				prevState.getFromDateTime(), prevState.getToDateTime(),
				prevState.getLabels(), null);
		mLocalMem.getLocalMem().remove(indexOfTaskToUndone);
		mLocalMem.getLocalMem().add(0, super.getTask());
		mLocalMem.redoPush(toRedo);
		mLocalMem.saveLocalMemory();
		String resultString = this.getTaskDetails() + " undone";
		FeedbackObject result = new FeedbackObject(resultString, true);
		return result;
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
		FeedbackObject result = new SearchCommand(task.getDescription(), task.getFromDateTime(), task.getToDateTime(), 
				task.getLabels(), null).execute();
		return result;
	}
}
