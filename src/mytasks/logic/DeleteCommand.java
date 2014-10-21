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
		for (int i=0; i<mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(super.getTaskDetails())) {
				DeleteCommand commandToUndo = new DeleteCommand(mLocalMem.getLocalMem().get(i).getDescription(), mLocalMem.getLocalMem().get(i).getFromDateTime(), mLocalMem.getLocalMem().get(i).getToDateTime(), mLocalMem.getLocalMem().get(i).getLabels(), null);
				mLocalMem.undoPush(commandToUndo);
				mLocalMem.remove(super.getTask());
				break;
			}
		}
		
		return super.getTaskDetails() + " deleted";
	}

	@Override
	String undo() {
		Task prevState = null;
//		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
//			if (mLocalMem.getLocalMem().get(i).getDescription().equals(this.getToUpdateTaskDesc())) {
//				prevState = mLocalMem.getLocalMem().get(i).getClone();
				mLocalMem.getLocalMem().add(this.getTask());
//				break;
//			}
//		}
				
//		Command toRedo = new AddCommand(prevState.getDescription(),
//				prevState.getFromDateTime(), prevState.getToDateTime(),
//				prevState.getLabels(), null);
//		mLocalMem.redoPush(toRedo);
		return this.getTask().getDescription() + " added";
	}

}
