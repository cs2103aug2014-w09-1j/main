package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

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
	FeedbackObject execute() {
		if (isRedo){
			int indexOfTaskToDone = 0;
			Task taskToDone = mLocalMem.getLocalMem().get(indexOfTaskToDone);		
			Command commandToUndo = createDoneUndo(taskToDone);
			mLocalMem.undoPush(commandToUndo);	
			Task doneTask = doTask(taskToDone);
			mLocalMem.getLocalMem().remove(indexOfTaskToDone);
			mLocalMem.getLocalMem().add(doneTask);
			mLocalMem.saveLocalMemory();			
			String resultString = String.format(MESSAGE_DONE_SUCCESS, taskToDone.getDescription());
			FeedbackObject result = new FeedbackObject(resultString, true);
			return result;
		}
		
		if (canDoneFromSearchResults()){
			FeedbackObject result = doneFromSearchResults();
			return result;
		}
		
		int timesAppear = countTimesAppear();
		if (timesAppear == 0){
			String resultString = String.format(MESSAGE_DONE_FAIL,
					super.getTaskDetails()) + "\n";
			resultString += autoSearch().getFeedback();
			FeedbackObject result = new FeedbackObject(resultString, false);
			return result;
		}
		else if (timesAppear > 1) {	
			String resultString = String.format(MESSAGE_DONE_DUPLICATE,
					super.getTaskDetails()) + "\n";
			resultString += autoSearch().getFeedback();
			FeedbackObject result = new FeedbackObject(resultString, true);
			return result;
		}else{
			boolean isDone = true;
			for (int i=0; i < mLocalMem.getLocalMem().size(); i++){
				if (mLocalMem.getLocalMem().get(i).getDescription().equals(super.getTaskDetails())){
					Task currentTask = mLocalMem.getLocalMem().get(i);
					Command commandToUndo = createDoneUndo(currentTask);
					mLocalMem.undoPush(commandToUndo);
					if (!isDone(currentTask)){
						isDone = false;
						doTask(currentTask);
					}
					mLocalMem.getLocalMem().remove(i);
					mLocalMem.getLocalMem().add(currentTask);
					break;
				}
			}

			haveSearched = false;
			mLocalMem.saveLocalMemory();
			if (isDone){
				String resultString =  String.format(MESSAGE_DONE_ALREADY, super.getTaskDetails());
				FeedbackObject result = new FeedbackObject(resultString,false);
				return result;
			} else{
				String resultString = String.format(MESSAGE_DONE_SUCCESS, super.getTaskDetails());
				FeedbackObject result = new FeedbackObject(resultString,true);
				return result;
			}
		}
	}
	

	private Task doTask(Task taskToDone) {
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

	private Task undoTask(Task taskToUndone) {
		if (taskToUndone.getLabels() == null){
			return taskToUndone;
		}
		
		for (int i=0; i < taskToUndone.getLabels().size(); i++){
			if (taskToUndone.getLabels().get(i).toLowerCase().equals("done")){
				taskToUndone.getLabels().remove(i);
			}
		}
		return taskToUndone;
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
	FeedbackObject undo() {
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
	
	private boolean canDoneFromSearchResults(){
		if (haveSearched == true && isNumeric(super.getTaskDetails()) && 
				Integer.parseInt(super.getTaskDetails())-1 < (mLocalMem.getSearchList().size())){
			return true;
		}
		return false;
	}

	private FeedbackObject doneFromSearchResults(){
		int indexOfTaskToDone = mLocalMem.getSearchList().get(Integer.parseInt(super.getTaskDetails())-1);
		Task taskToDone = mLocalMem.getLocalMem().get(indexOfTaskToDone);
		DoneCommand commandToUndo = createDoneUndo(taskToDone);
		mLocalMem.undoPush(commandToUndo);
		
		boolean isDone = true;
		if (!isDone(taskToDone)){
			isDone = false;
			doTask(taskToDone);
		}
		mLocalMem.getLocalMem().remove(indexOfTaskToDone);
		mLocalMem.getLocalMem().add(taskToDone);
		haveSearched = false;
		mLocalMem.saveLocalMemory();
		if (isDone){
			String resultString =  String.format(MESSAGE_DONE_ALREADY, taskToDone.getDescription());
			FeedbackObject result = new FeedbackObject(resultString,false);
			return result;
		}else{
			String resultString = String.format(MESSAGE_DONE_SUCCESS, taskToDone.getDescription());
			FeedbackObject result = new FeedbackObject(resultString,true);
			return result;
		}
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
