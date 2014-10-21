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
		mLocalMem.saveLocalMemory();
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
}
