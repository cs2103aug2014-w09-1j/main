package mytasks.logic;

import java.util.ArrayList;
import java.util.Date;

import mytasks.file.FeedbackObject;
import mytasks.ui.HelpUI;

//@author A0108543J
/**
 * HelpCommand extends Command object to follow OOP standards.
 */
public class HelpCommand extends Command {

	// private variables
	private HelpUI UIComponent;
	
	public HelpCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		UIComponent = HelpUI.getInstance();
	}

	public HelpCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc,
					boolean canDo) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		UIComponent = HelpUI.getInstance();

	}

	@Override
	FeedbackObject execute() {
		UIComponent.run();
		
		return null;
	}

	@Override
	FeedbackObject undo() {
		throw new UnsupportedOperationException(
						"Help does not have an undo function");
	}

}
