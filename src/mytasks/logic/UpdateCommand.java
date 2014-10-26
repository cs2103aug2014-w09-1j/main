package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.Task;

/**
 * UpdateCommand extends Command to follow OOP standards
 * 
 * @author Wilson
 *
 */
public class UpdateCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;

	public UpdateCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	String execute() {
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription()
					.equals(super.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				break;
			}
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
		return super.getToUpdateTaskDesc() + " updated";
	}

	@Override
	String undo() {
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
		return this.getToUpdateTaskDesc() + " reverted";
	}

}
