package mytasks.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

//@author A0108543J
/**
 * HelpUI is the GUI for HelpCommand.
 */
public class HelpUI extends JPanel {
	private JLabel textAreaLabel, footerAreaLabel;
	private JPanel textAreaPanel;
	private JTextArea textArea;
	private Border paneEdge;
	private JScrollPane scrollPane;
	private Border titled;
	private static HelpUI INSTANCE = null;

	public static HelpUI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HelpUI();
		}
		return INSTANCE;
	}

	public HelpUI() {
		super(new GridBagLayout());

		// for header
		textAreaLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Available Commands</font>");
		textAreaLabel.setOpaque(true);
		textAreaLabel.setFocusable(false);

		// a border that puts 10 extra pixels at the sides and bottom of each pane
		paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		textAreaPanel = new JPanel();
		textAreaPanel.setBorder(paneEdge);
		textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.Y_AXIS));
		textAreaPanel.removeAll();
		textAreaPanel.revalidate();
		textAreaPanel.repaint();
		textAreaPanel.setFocusable(false);

		// new text body
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFocusable(false);

		// coloured border
		Border colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 200), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
		titled = BorderFactory.createTitledBorder(colourLine, "These are all you will ever need (:");

		// main context
		String content = "testing1\ntesting2\ntesting3\n";
		textArea.setText(content);
		textArea.setBorder(titled);

		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;

		textAreaPanel.add(textArea, c);
		textArea.setCaretPosition(0);

		scrollPane = new JScrollPane(textAreaPanel);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane.setPreferredSize(new Dimension(400, 400));
		scrollPane.setFocusable(true);  

		// footer
		footerAreaLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Press 'Esc' key to exit!</font>");
		footerAreaLabel.setOpaque(true);
		footerAreaLabel.setFocusable(false);

		// Add Components to this panel.
		c.gridwidth = GridBagConstraints.REMAINDER;

		// for header (available commands)
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(textAreaLabel, c);

		// for instructions box
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		// for footer
		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(footerAreaLabel, c);
	}

	/**
	 * run opens up the help window
	 */
	public void run() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	private static void createAndShowGUI() {
		// create and set up the window
		JFrame frame = new JFrame("MyTasks Help");
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		// add contents to the window
		frame.add(new HelpUI());

		// display the window
		frame.pack();
		frame.setVisible(true);

		// pressing the esc key will close the window
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
		.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EXIT");
		frame.getRootPane().getActionMap().put("EXIT", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}
}