package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * UndoCommand extends Command object to follow OOP standards.
 * 
 * @author Wilson
 *
 */
public class UndoCommand extends Command {

	// private variables
	private LocalMemory mLocalMem;

	public UndoCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	String execute() {
		String feedback;

		if (mLocalMem.getUndoStack().isEmpty()) {
			feedback = "No changes yet";
		} else {
			Command commandToRevert = mLocalMem.getUndoStack().pop();
			feedback = commandToRevert.undo();
		}
		haveSearched = false;
		return feedback;
	}

	@Override
	String undo() {
		throw new UnsupportedOperationException("UndoCommand does not have an undo function");
	}

}
