package mytasks.logic;

//@author A0114302A
import java.util.ArrayList;
import java.util.Date;

/**
 * RedoCommand extends Command to follow OOP standard. Redo command does not exist within the stack
 * for undo/redo but is used to manipulate these stacks 
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
		haveSearched = false;
		return feedback;
	}

	@Override
	String undo(){
		// Unimplemented method
		throw new UnsupportedOperationException("RedoCommand does not have an undo function");
	}
}
