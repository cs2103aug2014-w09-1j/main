package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;

/**
 * UpdateCommand extends Command to follow OOP standards
 * 
 * @author Huiwen, Shuan Siang
 *
 */
public class UpdateCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private static String MESSAGE_UPDATE_FAIL = "Task '%1$s' does not exist. Unable to update";
	private static String MESSAGE_UPDATE_SUCCESS = "'%1$s' updated";


	public UpdateCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	FeedbackObject execute() {
		if (canUpdateFromSearchResults()){
			FeedbackObject result = updateFromSearchResults();
			return result;
		}
		
		Task prevState = null;
		boolean hasTask = false;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(super.getToUpdateTaskDesc())) {
				hasTask = true;
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				break;
			}
		}
		if (!hasTask){
			String resultString = String.format(MESSAGE_UPDATE_FAIL, super.getToUpdateTaskDesc());
			FeedbackObject result = new FeedbackObject(resultString,false);
			return result;
		}
		
		UpdateCommand commandToUndo = null;
		if (this.getTaskDetails() == null) {
			commandToUndo = new UpdateCommand(prevState.getDescription(),
					prevState.getFromDateTime(), prevState.getToDateTime(),
					prevState.getLabels(), super.getToUpdateTaskDesc());
		} else {
			commandToUndo = new UpdateCommand(prevState.getDescription(),
					prevState.getFromDateTime(), prevState.getToDateTime(),
					prevState.getLabels(), super.getTaskDetails());
		}
		mLocalMem.undoPush(commandToUndo);

		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (super.getToUpdateTaskDesc().equals(
					mLocalMem.getLocalMem().get(i).getDescription())) {
				Task currentTask = super.getTask();
				if (currentTask.getDescription() != null) {
					mLocalMem.getLocalMem().get(i)
							.setDescription(currentTask.getDescription());
				}
				if (currentTask.getFromDateTime() != null
						&& currentTask.getToDateTime() != null) {
					mLocalMem.getLocalMem().get(i)
							.setFromDateTime(currentTask.getFromDateTime());
					mLocalMem.getLocalMem().get(i)
							.setToDateTime(currentTask.getToDateTime());
				}
				if (currentTask.getFromDateTime() != null
						&& currentTask.getToDateTime() == null) {
					mLocalMem.getLocalMem().get(i)
							.setFromDateTime(currentTask.getFromDateTime());
					mLocalMem.getLocalMem().get(i).setToDateTime(null);
				}
				if (currentTask.getLabels() != null) {
					if (!super.getTask().getLabels().isEmpty()) {
						mLocalMem.getLocalMem().get(i)
								.setLabels(currentTask.getLabels());
					}
				}
			}
		}
		mLocalMem.saveLocalMemory();
		haveSearched = false;
		String resultString = String.format(MESSAGE_UPDATE_SUCCESS, super.getToUpdateTaskDesc());
		FeedbackObject result = new FeedbackObject(resultString,true);
		return result;
	}

	//@author A0108543J
	@Override
	FeedbackObject undo() {
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(this.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				mLocalMem.getLocalMem().remove(i);
				mLocalMem.getLocalMem().add(this.getTask());
				break;
			}
		}
		Command toRedo = new UpdateCommand(prevState.getDescription(),
				prevState.getFromDateTime(), prevState.getToDateTime(),
				prevState.getLabels(), this.getTask().getDescription());
		mLocalMem.redoPush(toRedo);
		mLocalMem.saveLocalMemory();
		String resultString = this.getToUpdateTaskDesc() + " reverted";
		FeedbackObject result = new FeedbackObject(resultString,true);
		return result;
	}

	//@author A0112139R
	private boolean canUpdateFromSearchResults(){
		if (haveSearched == true && isNumeric(super.getToUpdateTaskDesc()) 
				&& Integer.parseInt(super.getToUpdateTaskDesc())-1 < (mLocalMem.getSearchList().size())){
			return true;
		}
		return false;
	}

	private FeedbackObject updateFromSearchResults(){
		FeedbackObject feedback = new UpdateCommand(super.getTaskDetails(), super.getTask().getFromDateTime(), 
				super.getTask().getToDateTime(), super.getTask().getLabels(), 
				mLocalMem.getSearchList().get(Integer.parseInt(super.getToUpdateTaskDesc())-1).getDescription()).execute();
		haveSearched = false;
		return feedback;
	}

	private boolean isNumeric(String str)  
	{  
		try  {  
			int i = Integer.parseInt(str);  
		} catch(NumberFormatException nfe)  {  
			return false;  
		}  
		return true;  
	}

}
