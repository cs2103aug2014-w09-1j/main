package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * DeleteCommand extends Command to follow OOP standards
 * 
 * @author Wilson
 *
 */

public class DeleteCommand extends Command {
	
	// private variables
	private LocalMemory mLocalMem;
		
	public DeleteCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
	}

	@Override
	String execute() {
		mLocalMem.remove(super.getTask());
		DeleteCommand commandToUndo = new DeleteCommand(null, null, null, null, super.getTask().getDescription());
		mLocalMem.undoPush(commandToUndo);
		return super.getTaskDetails() + " added";
	}

	@Override
	String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
