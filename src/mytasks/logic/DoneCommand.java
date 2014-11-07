package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

/**
 * DoneCommand extends Command object to follow OOP standards
 * 
 * @author Michael
 *
 */

//@author A0112139R
public class DoneCommand extends Command {
	
	private LocalMemory mLocalMem;
	private static String MESSAGE_DONE_SUCCESS = "'%1$s' mark as done";
	private static String MESSAGE_DONE_FAIL = "Task '%1$s' does not exist. Unable to mark as done. Auto search for similar tasks.";
	private static String MESSAGE_DONE_ALREADY = "Task '%1$s' had already been marked as done.";

	public DoneCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	FeedbackObject execute() {
		if (canDoneFromSearchResults()){
			FeedbackObject result = doneFromSearchResults();
			return result;
		}
		System.out.println("Task:" + super.getTask() + "@@@@@@@@@@@@@@@@@");
		boolean hasTask = false;
		boolean isDone = true;
		for (int i=0; i < mLocalMem.getLocalMem().size(); i++){
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(super.getTaskDetails())){
				hasTask = true;
				Task currentTask = mLocalMem.getLocalMem().get(i);
				System.out.println("currentTask:" + currentTask + "@@@@@@@@@@@@@@@@@@");
				Command commandToUndo = new DoneCommand(currentTask.getDescription(),
						currentTask.getFromDateTime(), currentTask.getToDateTime(), 
						currentTask.getLabels(), null);
				mLocalMem.undoPush(commandToUndo);

				ArrayList<String> labels = new ArrayList<String>();
				if (currentTask.getLabels() != null){
					labels = currentTask.getLabels();
				}
				System.out.println("currentTask:" + currentTask + "@@@@@@@@@@@@@@@@@@");
				if (!isDone(currentTask)){
					isDone = false;
					labels.add("done");
				}
				currentTask.setLabels(labels);
				System.out.println("currentTask:" + currentTask + "@@@@@@@@@@@@@@@@@@");
				break;
			}
		}

		haveSearched = false;
		mLocalMem.saveLocalMemory();
		if (hasTask && isDone){
			String resultString =  String.format(MESSAGE_DONE_ALREADY, super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString,false);
			return result;
		}
		else if (hasTask){
			String resultString = String.format(MESSAGE_DONE_SUCCESS, super.getTaskDetails());
			FeedbackObject result = new FeedbackObject(resultString,true);
			return result;
		}
		else{
			String resultString = String.format(MESSAGE_DONE_FAIL, super.getTaskDetails()) + "\n";
			resultString += autoSearch().getFeedback();
			FeedbackObject result = new FeedbackObject(resultString,false);
			return result;
		}
	}

	@Override
	FeedbackObject undo() {
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(this.getTaskDetails())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				ArrayList<String> labels = new ArrayList<String>();
				for (int j=0; j < prevState.getLabels().size(); j++){
					if (prevState.getLabels().get(j).equals("done")){
						continue;
					}
					labels.add(prevState.getLabels().get(j));
				}	
				prevState.setLabels(labels);
				mLocalMem.getLocalMem().remove(i);
				mLocalMem.getLocalMem().add(prevState);
				break;
			}
		}
		Command toRedo = new DoneCommand(prevState.getDescription(),
				prevState.getFromDateTime(), prevState.getToDateTime(),
				prevState.getLabels(), null);
	
		mLocalMem.redoPush(toRedo);
		mLocalMem.saveLocalMemory();
		String resultString = this.getTask().getDescription() + " undone"; 
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
		FeedbackObject feedback = new DoneCommand(mLocalMem.getSearchList().get
				(Integer.parseInt(super.getTaskDetails())-1).getDescription(), null, null, null, null).execute();
		haveSearched = false;
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
