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
	
	public UndoCommand(String comdType, String comdDes, Date fromDateTime,
			Date toDateTime, ArrayList<String> comdLabels, String updateDesc) {
		super(comdType, comdDes, fromDateTime, toDateTime, comdLabels,
				updateDesc);
	}

	@Override
	String execute() {
		// TODO: implement this
		Command commandToRevert = mLocalMem.getUndoStack().pop();
		String feedback = commandToRevert.undo();
		return feedback;
	}

	@Override
	String undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
