package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.Task;

/**
 * DeleteCommand extends Command to follow OOP standards
 * 
 * @author Huiwen
 *
 */

public class DeleteCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;

	public DeleteCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	String execute() {
		if (haveSearched == true && isNumeric(super.getTaskDetails()) && Integer.parseInt(super.getTaskDetails())-1 < (mLocalMem.getSearchList().size())){
			String feedback = new DeleteCommand(mLocalMem.getSearchList().get(Integer.parseInt(super.getTaskDetails())-1).getDescription(), null, null, null, null).execute();
			haveSearched = false;
			return feedback;
		}
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

		haveSearched = false;
		mLocalMem.saveLocalMemory();
		System.out.println(super.getTaskDetails());
		return super.getTaskDetails() + " deleted";
	}

	@Override
	String undo() {
		Task prevState = super.getTask();
		Command toRedo = new DeleteCommand(prevState.getDescription(), null,
				null, null, null);
		mLocalMem.add(prevState);
		mLocalMem.redoPush(toRedo);
		mLocalMem.saveLocalMemory();
		return this.getTask().getDescription() + " added";
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	     int i = Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
}
