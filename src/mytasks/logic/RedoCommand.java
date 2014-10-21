package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

/**
 * RedoCommand extends Command to follow OOP standard
 * 
 * @author Wilson
 *
 */
public class RedoCommand extends Command {
	
	LocalMemory mLocalMem = null;
	
	public RedoCommand(String comdDes, Date fromDateTime, Date toDateTime,
			ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mLocalMem = LocalMemory.getInstance();
	}

	@Override
	String execute() {
		String feedback;

		if (mLocalMem.getRedoStack().isEmpty()) {
			feedback = "No commands to undo";
		} else {
			Command commandToRevert = mLocalMem.getRedoStack().pop();
			feedback = commandToRevert.execute();
		}
		return feedback;
	}

	@Override
	String undo() {
		// TODO Auto-generated method stub
		return null;
	}
}
