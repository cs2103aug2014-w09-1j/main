package mytasks.logic;

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

//@author A0108543J
/**
 * HelpCommand extends Command object to follow OOP standards.
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

		// pressing the esc key will close the window
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EXIT");
		frame.getRootPane().getActionMap().put("EXIT", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		return null;
	}

	@Override
	FeedbackObject undo() {
		throw new UnsupportedOperationException(
						"Help does not have an undo function");
	}

}
