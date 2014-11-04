package mytasks.logic;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

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
		// TODO Auto-generated constructor stub
	}

	public HelpCommand(String comdDes, Date fromDateTime, Date toDateTime,
					ArrayList<String> comdLabels, String updateDesc,
					boolean canDo) {
		super(comdDes, fromDateTime, toDateTime, comdLabels, updateDesc);
		// TODO Auto-generated constructor stub
	}

	@Override
	String execute() {
		// TODO Auto-generated method stub
		HelpWindow help = new HelpWindow(null);
		help.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		help.setSize(300,300);
		help.setLocation(300, 300);
		help.setVisible(true);
		
		return "Opening help";
	}

	@Override
	String undo() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Help does not have an undo function");
	}
	
	public class HelpWindow extends JDialog {
		JLabel label;
		
		public HelpWindow(JFrame frame) {
			super(frame, "Help", true);
			setLayout(new FlowLayout());
			
			label = new JLabel("new window");
			add(label);
		}
	}
}
