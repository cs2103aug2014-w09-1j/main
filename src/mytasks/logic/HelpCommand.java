package mytasks.logic;

// @author A0108543J
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import mytasks.file.FeedbackObject;
import mytasks.ui.HelpUI;

/**
 * HelpCommand extends Command object to follow OOP standards.
 * 
 * @author Shuan Siang
 *
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
