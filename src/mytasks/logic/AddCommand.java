package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.Task;

/**
 * AddCommand extends Command object to follow OOP standards
 * 
 * @author Wilson
 *
 */
public class AddCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;
	private Task mTask;

	public AddCommand(String comdType, String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdType, comdDes, fromDateTime, toDateTime, comdLabels,
				updateDesc);
		Task thisTask = new Task(comdDes, fromDateTime, toDateTime, comdLabels);
		mTask = thisTask;
		
	}

	@Override
	String execute() {
		// TODO Auto-generated method stub
		mLocalMem.add(super.getTask());
		AddCommand commandToUndo = new AddCommand(null, null, null, null, null, mTask.getDescription());
		mLocalMem.push(commandToUndo);
		return "add completed";
	}

	String undo() {
		for (int i=0; i<mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(this.getToUpdateTaskDesc())) {
				mLocalMem.getLocalMem().remove(i);
				break;
			}
		}
		
		return "undo complete";
	}
}
