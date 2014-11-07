package mytasks.logic;

//@author A0108543J
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import mytasks.file.FeedbackObject;
import mytasks.ui.HelpUI;

/**
 * HelpCommand extends Command object to follow OOP standards.
 * 
 * @author Shuan Siang
 *
 */
public class HelpCommand extends Command {

	public HelpCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
	}

	public HelpCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc,
					boolean canDo) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
	}

	@Override
	FeedbackObject execute() {
		// create and set up the window
		JFrame frame = new JFrame("MyTasks Help");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		// add contents to the window
		frame.add(new HelpUI());
		
		// display the window
		frame.pack();
		frame.setVisible(true);
		return null;
	}

	@Override
	FeedbackObject undo() {
		throw new UnsupportedOperationException("Help does not have an undo function");
	}
	
}
