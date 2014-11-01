package mytasks.ui;

import java.awt.*;
//waits for user to do something
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import mytasks.logic.ILogic;
import mytasks.logic.MyTasksLogic;

/**
 * MyTasksUI is the GUI for MyTasks. 
 *
 * 
 * @author Huiwen
 *
 */
public class MyTasksUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected JTextField textField;
	protected JTextArea textArea;
	protected JTextArea textAreaFeedback;
	private ILogic mLogic;
	private JLabel textAreaLabel, feedbackLabel, textfieldLabel;
	private JPanel textAreaPanel;
	private Border paneEdge;
	private JScrollPane scrollPane;
	private Border titled;
	
	public MyTasksUI() {
		super(new GridBagLayout());

		mLogic = new MyTasksLogic(false);
		textAreaLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Tasks</font>");
		textAreaLabel.setOpaque(true);


		// A border that puts 10 extra pixels at the sides and bottom of each pane.
		paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		textAreaPanel = new JPanel();
		textAreaPanel.setBorder(paneEdge);
		textAreaPanel.setLayout(new BoxLayout(textAreaPanel,BoxLayout.Y_AXIS));
		
		System.out.println("obtainprintableoutput: ");
		for(int i = 0; i < mLogic.obtainPrintableOutput().size(); i++) {
			System.out.println(mLogic.obtainPrintableOutput().get(i));
		}
		textAreaPanel.removeAll();
		textAreaPanel.revalidate();
		textAreaPanel.repaint();
		textArea = new JTextArea();
		
		if(mLogic.obtainPrintableOutput().size() == 0) {
			textArea = new JTextArea(20, 50);
			textArea.setEditable(false);			
			titled = BorderFactory.createTitledBorder("Welcome! Add your tasks below (:");
			textArea.setBorder(titled);
			textAreaPanel.add(textArea);
		} else {	
			for (int i = 0; i < mLogic.obtainPrintableOutput().size(); i++) {				
				System.out.println("before start up i: " + i + " size: " + mLogic.obtainPrintableOutput().size());
				
				String firstWord = mLogic.obtainPrintableOutput().get(i).split("\\s+")[0];
				System.out.println("firstWord: " + firstWord);
				
				textArea = new JTextArea(1, 50);
				textArea.setEditable(false);

				titled = BorderFactory.createTitledBorder(firstWord);

				String content = mLogic.obtainPrintableOutput().get(i).replace(firstWord, "").trim();
				textArea.setText(content);
				textArea.setBorder(titled);
				System.out.println("print output: ");
				System.out.println(mLogic.obtainPrintableOutput().get(i));
			
				textAreaPanel.add(textArea);			
			}
		}
		scrollPane = new JScrollPane(textAreaPanel);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));

		feedbackLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Feedback Box</font>");
		textAreaFeedback = new JTextArea(5, 50);
		textAreaFeedback.setEditable(false);
		JScrollPane scrollPaneFeedback = new JScrollPane(textAreaFeedback);
		scrollPaneFeedback.setBorder(BorderFactory.createLineBorder(Color.black));

		textfieldLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Input Tasks</font>");
		textField = new JTextField(20);
		textField.addActionListener(this);

		// Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(textAreaLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(feedbackLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPaneFeedback, c);

		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(textfieldLabel, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(textField, c);
	}

	/**
	 * run starts the process of accepting and executing input
	 */
	public void run() {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public void actionPerformed(ActionEvent evt) {
		String text = textField.getText();
		textAreaFeedback.setText(mLogic.executeCommand(text));
		textAreaPanel.removeAll();
		textAreaPanel.revalidate();
		textAreaPanel.repaint();
		
		if(mLogic.obtainPrintableOutput().size() == 0) {
			textArea = new JTextArea(20, 50);
			textArea.setEditable(false);			
			titled = BorderFactory.createTitledBorder("Welcome! Add your tasks below (:");
			textArea.setBorder(titled);
			textAreaPanel.add(textArea);
		} else {
			for (int i = 0; i < mLogic.obtainPrintableOutput().size(); i++) {
				System.out.println("i: " + i + " size: " + mLogic.obtainPrintableOutput().size());
			
				String firstWord = mLogic.obtainPrintableOutput().get(i).split("\\s+")[0];
				System.out.println("firstWord: " + firstWord);
				textArea = new JTextArea();
				textArea.setEditable(false);

				titled = BorderFactory.createTitledBorder(firstWord);

				String content = mLogic.obtainPrintableOutput().get(i).replace(firstWord, "").trim();
				textArea.setText(content);
				textArea.setBorder(titled);
				System.out.println("print output: ");
				System.out.println(mLogic.obtainPrintableOutput().get(i));
			
				textAreaPanel.add(textArea);
			}
		}

		textArea.setCaretPosition(textArea.getDocument().getLength());
		textField.selectAll();
	}


	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("MyTasks");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add contents to the window.
		frame.add(new MyTasksUI());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
}
