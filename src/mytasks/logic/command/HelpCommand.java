package mytasks.logic.command;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.logic.controller.MyTasksLogicController;

//@author A0108543J
/**
 * HelpCommand extends Command object to follow OOP standards.
 */
public class HelpCommand extends Command {

	// private variables
	private MyTasksLogicController mController; 
	
	public HelpCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mController = MyTasksLogicController.getInstance(false);
	}

	public HelpCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc,
					boolean canDo) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		mController = MyTasksLogicController.getInstance(false);
	}

	@Override
	public FeedbackObject execute() {
		hasSearched = false;
		mController.toggleHelpUI(true);
		FeedbackObject temp = new FeedbackObject("Help menu opened",true);
		return temp;
	}

	@Override
	public FeedbackObject undo() {
		throw new UnsupportedOperationException(
						"Help does not have an undo function");
	}

}
