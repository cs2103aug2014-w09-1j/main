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
		mLocalMem.remove(super.getTask());
		DeleteCommand commandToUndo = new DeleteCommand(null, null, null, null, super.getTask().getDescription());
		mLocalMem.undoPush(commandToUndo);
		return super.getTaskDetails() + " deleted";
	}

	@Override
	String undo() {
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(this.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				mLocalMem.add(prevState);
				break;
			}
		}
		Command toRedo = new AddCommand(prevState.getDescription(),
				prevState.getFromDateTime(), prevState.getToDateTime(),
				prevState.getLabels(), null);
		mLocalMem.redoPush(toRedo);
		return this.getToUpdateTaskDesc() + " added";
	}

}
