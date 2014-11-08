package mytasks.logic.command;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.logic.controller.LocalMemory;

//@author A0114302A
/**
 * UndoCommand extends Command object to follow OOP standards.Undo command does not exist within the stack
 * for undo/redo but is used to manipulate these stacks 
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
	public FeedbackObject execute() {
		String feedback;
		FeedbackObject result = null;
		if (mLocalMem.getUndoStack().isEmpty()) {
			feedback = "No changes yet";
			result = new FeedbackObject(feedback, false);
		} else {
			Command commandToRevert = mLocalMem.getUndoStack().pop();
			result = commandToRevert.undo();
		}
		haveSearched = false;
		return result;
	}

	@Override
	public FeedbackObject undo() {
		throw new UnsupportedOperationException("UndoCommand does not have an undo function");
	}

}
