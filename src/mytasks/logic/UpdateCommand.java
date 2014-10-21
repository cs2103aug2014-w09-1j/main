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
		// TODO Auto-generated method stub
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(super.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				break;
			}
		}
		UpdateCommand commandToUndo = new UpdateCommand(prevState.getDescription(), prevState.getFromDateTime(), prevState.getToDateTime(), prevState.getLabels(), super.getTaskDetails());
		mLocalMem.undoPush(commandToUndo);
		
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (super.getToUpdateTaskDesc().equals(mLocalMem.getLocalMem().get(i).getDescription())) {
				if (super.getTask().getDescription() != null) {
					mLocalMem.getLocalMem().get(i).setDescription(super.getTask().getDescription());
				}
				if (super.getTask().getFromDateTime() != null) {
					mLocalMem.getLocalMem().get(i).setFromDateTime(super.getTask().getFromDateTime());
				}
				if (super.getTask().getToDateTime() != null) {
					mLocalMem.getLocalMem().get(i).setToDateTime(super.getTask().getToDateTime());
				}
				if (super.getTask().getLabels() != null) {
					if (!super.getTask().getLabels().isEmpty()) {
						mLocalMem.getLocalMem().get(i).setLabels(super.getTask().getLabels());
					}
				}
			}
		}
		
		return super.getToUpdateTaskDesc() + " updated";
	}

	@Override
	String undo() {
		// TODO Auto-generated method stub
//		mLocalMem.print();
		Task prevState = null;
		for (int i = 0; i < mLocalMem.getLocalMem().size(); i++) {
			if (mLocalMem.getLocalMem().get(i).getDescription().equals(this.getToUpdateTaskDesc())) {
				prevState = mLocalMem.getLocalMem().get(i).getClone();
				mLocalMem.getLocalMem().remove(i);
				mLocalMem.getLocalMem().add(this.getTask());
				break;
			}
		}
//		mLocalMem.print();

		Command toRedo = new UpdateCommand(prevState.getDescription(),
				prevState.getFromDateTime(), prevState.getToDateTime(),
				prevState.getLabels(), null);
		mLocalMem.redoPush(toRedo);

		return this.getToUpdateTaskDesc() + " reverted";
	}

}
