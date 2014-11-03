package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.Task;

/**
 * DoneCommand extends Command object to follow OOP standards
 * 
 * @author Michael
 *
 */
public class DoneCommand extends Command {
	
	private LocalMemory mLocalMem;
	private static String MESSAGE_DONE_SUCCESS = "'%1$s' mark as done";
	private static String MESSAGE_DONE_FAIL = "Task '%1$s' does not exist. Unable to mark as done.";

	public DoneCommand(String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	String execute() {
		if (haveSearched == true && isNumeric(super.getTaskDetails()) && Integer.parseInt(super.getTaskDetails())-1 < (mLocalMem.getSearchList().size())){
			String feedback = new DoneCommand(mLocalMem.getSearchList().get(Integer.parseInt(super.getTaskDetails())-1).getDescription(), null, null, null, null).execute();
			haveSearched = false;
			return feedback;
		}
	    boolean hasTask = false;
		for (int i=0; i < mLocalMem.getLocalMem().size(); i++){
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(super.getTaskDetails())){
				hasTask = true;
				Task currentTask = mLocalMem.getLocalMem().get(i);
			    Command commandToUndo = new DoneCommand(currentTask.getDescription(),
						currentTask.getFromDateTime(), currentTask.getToDateTime(), 
						currentTask.getLabels(), null);
				mLocalMem.undoPush(commandToUndo);
				ArrayList<String> labels = currentTask.getLabels();
				labels.add("done");
				currentTask.setLabels(labels);
				break;
			}
		}
		
		haveSearched = false;
		mLocalMem.saveLocalMemory();
		if (hasTask){
			return String.format(MESSAGE_DONE_SUCCESS, super.getTaskDetails());
		}
		else{
			return String.format(MESSAGE_DONE_FAIL, super.getTaskDetails());
		}
	}

	@Override
	String undo() {
		return null;
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
