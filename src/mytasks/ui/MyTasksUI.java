package mytasks.ui;

import java.awt.*;
//waits for user to do something
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import mytasks.file.FeedbackObject;
import mytasks.logic.controller.ILogic;
import mytasks.logic.controller.MyTasksLogicController;

//@author A0115034X
/**
 * MyTasksUI accepts input to the user and prints output to the user through a
 * GUI supported by the Swing framework
 */
@SuppressWarnings("serial")
public class MyTasksUI extends JFrame implements ActionListener,
		DocumentListener {
	protected JTextField textField;
	protected JTextArea textArea;
	protected JTextArea textAreaFeedback;
	private JLabel textAreaLabel, feedbackLabel, textfieldLabel;
	private JPanel textAreaPanel;
	private Border paneEdge;
	private JScrollPane scrollPane, scrollPaneFeedback;
	private Border titled;
	private boolean lookingFor = false;
	private int w = 0;
	private ILogic mLogic;
	private static MyTasksUI INSTANCE = null;

	private Mode mode = Mode.INSERT;
	private List<String> words, commands;
	private HelpUI helpUI;

	private static enum Mode {
		INSERT, COMPLETION
	};

	public static MyTasksUI getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MyTasksUI();
		}
		return INSTANCE;
	}

	protected Object readResolve() {
		return INSTANCE;
	}

	private MyTasksUI() {
		super("My Tasks");
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

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		MyTasksUI frame = new MyTasksUI();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set up the content pane
		frame.addComponentsToPane();

		// Display the window.
		frame.pack();
		frame.setVisible(true);

		// pressing the escape key will close the window
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EXIT");
		frame.getRootPane().getActionMap().put("EXIT", new AbstractAction() {

			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}

	private void addComponentsToPane() {
		GridBagLayout layout = new GridBagLayout();
		getContentPane().setLayout(layout);

		mLogic = MyTasksLogicController.getInstance(false);
		helpUI = HelpUI.getInstance();

		// for tasks label and box
		initTextAreaLabelPanel();
		clearTextAreaPanel();
		printToUserOnStartUp();
		initTextAreaScrollpane();

		initFeedbackBox();
		initTextfield();
		initAutocomplete();
		autocompleteStrings();
		addComponents();
	}

	private void initTextAreaLabelPanel() {
		textAreaLabel = new JLabel("<html><center>"
				+ "<font color=#7c5cff>Tasks</font>");
		textAreaLabel.setOpaque(true);
		textAreaLabel.setFocusable(false);

		paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		textAreaPanel = new JPanel();
		textAreaPanel.setBorder(paneEdge);
		textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.Y_AXIS));
		textAreaPanel.setFocusable(false);
	}

	private void clearTextAreaPanel() {
		textAreaPanel.removeAll();
		textAreaPanel.revalidate();
		textAreaPanel.repaint();
	}

	private void printToUserOnStartUp() {
		if (mLogic.obtainPrintableOutput().size() == 0) {
			newTextArea();
			Border colourLine = BorderFactory.createLineBorder(new Color(
					(int) (Math.random() * 255), (int) (Math.random() * 255),
					(int) (Math.random() * 255)), 3);
			titled = BorderFactory.createTitledBorder(colourLine,
					"Welcome! Add your tasks below (:");
			setTextareaContentBorder("", titled);
		} else {
			for (int i = 0; i < mLogic.obtainPrintableOutput().size(); i++) {
				newTextArea();

				String firstWord = mLogic.obtainPrintableOutput().get(i)
						.split("\\s+")[0];
				Border colourLine = BorderFactory.createLineBorder(new Color(
						(int) (Math.random() * 255),
						(int) (Math.random() * 255),
						(int) (Math.random() * 255)), 3);
				titled = BorderFactory
						.createTitledBorder(colourLine, firstWord);
				String content = mLogic.obtainPrintableOutput().get(i)
						.replace(firstWord, "").trim();
				setTextareaContentBorder(content, titled);
			}
		}
	}

	private void newTextArea() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFocusable(false);
	}

	private void setTextareaContentBorder(String content, Border titled2) {
		textArea.setText(content);
		textArea.setBorder(titled);
		textAreaPanel.add(textArea);
		textArea.setCaretPosition(0);
	}

	private void initTextAreaScrollpane() {
		scrollPane = new JScrollPane(textAreaPanel);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane.setPreferredSize(new Dimension(500, 400));
		scrollPane.setFocusable(true);
	}

	private JScrollPane initFeedbackBox() {
		feedbackLabel = new JLabel("<html><center>"
				+ "<font color=#7c5cff>Feedback Box</font>");
		feedbackLabel.setFocusable(false);
		textAreaFeedback = new JTextArea(10, 40);
		textAreaFeedback.setEditable(false);
		textAreaFeedback.setFocusable(false);
		scrollPaneFeedback = new JScrollPane(textAreaFeedback);
		scrollPaneFeedback.setBorder(BorderFactory
				.createLineBorder(Color.black));
		scrollPaneFeedback.setFocusable(true);
		return scrollPaneFeedback;
	}

	private void initTextfield() {
		textfieldLabel = new JLabel("<html><center>"
				+ "<font color=#7c5cff>Input Tasks</font>");
		textfieldLabel.setFocusable(false);
		textField = new JTextField(20);
		textField.addActionListener(this);
		textField.getDocument().addDocumentListener(this);
		textField.setFocusable(true);
	}

	private void initAutocomplete() {
		InputMap im = textField.getInputMap();
		ActionMap am = textField.getActionMap();
		im.put(KeyStroke.getKeyStroke("RIGHT"), "commit");
		am.put("commit", new CommitAction());
	}

	private void autocompleteStrings() {
		commands = new ArrayList<String>();
		commands.add("ad");
		commands.add("se");
		commands.add("de");
		commands.add("do");
		commands.add("un");
		commands.add("re");
		commands.add("up");
		commands.add("so");
		commands.add("hi");
		commands.add("sh");
		commands.add("?");
		commands.add("he");
		commands.add("help");

		words = new ArrayList<String>(commands);
		for (int i = 0; i < mLogic.obtainAllTaskDescription().size(); i++) {
			words.add(mLogic.obtainAllTaskDescription().get(i));
		}
		for (int i = 0; i < mLogic.obtainAllLabels().size(); i++) {
			words.add(mLogic.obtainAllLabels().get(i));
		}
		Collections.sort(words);
	}

	private void addComponents() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		initGridbagWeight(c);
		add(textAreaLabel, c);

		c.fill = GridBagConstraints.BOTH;
		initGridbagWeight(c);
		add(scrollPane, c);

		c.fill = GridBagConstraints.VERTICAL;
		initGridbagWeight(c);
		add(feedbackLabel, c);

		c.fill = GridBagConstraints.BOTH;
		initGridbagWeight(c);
		add(scrollPaneFeedback, c);

		c.fill = GridBagConstraints.VERTICAL;
		initGridbagWeight(c);
		add(textfieldLabel, c);

		c.fill = GridBagConstraints.BOTH;
		initGridbagWeight(c);
		add(textField, c);
	}

	private void initGridbagWeight(GridBagConstraints c) {
		c.weightx = 1.0;
		c.weighty = 1.0;
	}

	public void actionPerformed(ActionEvent evt) {
		lookingFor = false;
		w = 0;
		boolean isHide = false;
		List<String> labelsToHide = new ArrayList<String>();

		clearTextAreaPanel();

		String text = textField.getText();
		FeedbackObject feedback = mLogic.executeCommand(text);
		returnFeedbackToUser(feedback);
		autocompleteStrings();

		if (mLogic.checkIfToHelpUI()) {
			helpUI.run();
			mLogic.toggleHelpUI(false);
		}
		printToUser(isHide, labelsToHide);
	}

	private void returnFeedbackToUser(FeedbackObject feedback) {
		if (feedback != null) {
			if (feedback.getValidity() == false) {
				textAreaFeedback.setForeground(new Color(255, 0, 0));
				textAreaFeedback.setText(feedback.getFeedback());
				textAreaFeedback.setCaretPosition(0);
			} else {
				textAreaFeedback.setForeground(new Color(0, 150, 0));
				textAreaFeedback.setText(feedback.getFeedback());
				textAreaFeedback.setCaretPosition(0);
			}
		}
	}

	private void printToUser(boolean isHide, List<String> labelsToHide) {
		boolean isRed;
		Border colourLine;

		if (mLogic.obtainPrintableOutput().size() == 0) {
			newTextArea();
			titled = BorderFactory
					.createTitledBorder("Welcome! Add your tasks below (:");
			setTextareaContentBorder("", titled);
		} else {
			for (int i = 0; i < mLogic.obtainPrintableOutput().size(); i++) {
				newTextArea();

				String firstWord = mLogic.obtainPrintableOutput().get(i)
						.split("\\s+")[0];
				isRed = checkImportant(firstWord);
				isHide = checkLabelsToHide(isHide, labelsToHide, firstWord);

				if (isHide) {
					colourLine = detBorderColour(isRed);
					titled = BorderFactory.createTitledBorder(colourLine,
							firstWord);
					setTextareaContentBorder("", titled);
				} else {
					colourLine = detBorderColour(isRed);
					titled = BorderFactory.createTitledBorder(colourLine,
							firstWord);
					String content = mLogic.obtainPrintableOutput().get(i)
							.replace(firstWord, "").trim();
					setTextareaContentBorder(content, titled);
				}
			}
		}
		textField.selectAll();
	}

	private boolean checkImportant(String firstWord) {
		boolean isRed;
		if (firstWord.equals("#important")) {
			isRed = true;
		} else {
			isRed = false;
		}
		return isRed;
	}

	private boolean checkLabelsToHide(boolean isHide,
			List<String> labelsToHide, String firstWord) {
		isHide = false;
		if (mLogic.checkIfToHide()) {
			labelsToHide = mLogic.labelsToHide();
		}
		if (labelsToHide.size() > 0) {
			for (int k = 0; k < labelsToHide.size(); k++) {
				if (labelsToHide.get(k).equals(firstWord)) {
					isHide = true;
				}
			}
		}
		return isHide;
	}

	private Border detBorderColour(boolean isRed) {
		Border colourLine;
		if (isRed) {
			colourLine = BorderFactory
					.createLineBorder(new Color(255, 0, 0), 3);
		} else {
			colourLine = BorderFactory.createLineBorder(
					new Color((int) (Math.random() * 200),
							(int) (Math.random() * 255),
							(int) (Math.random() * 255)), 3);
		}
		return colourLine;
	}

	@Override
	public void insertUpdate(DocumentEvent ev) {
		if (ev.getLength() != 1)
			return;

		int pos = ev.getOffset(); // last letter of input
		String content = null;
		try {
			content = textField.getText(0, pos + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		// Find where the word starts
		if (lookingFor == false) {
			for (w = pos; w >= 0; w--) {
				if (!Character.isLetter(content.charAt(w))) {
					lookingFor = true;
					break;
				}
			}
		}

		if (pos - w < 1)
			return;

		String prefix = content.substring(w + 1);
		int n = Collections.binarySearch(words, prefix);
		if (n < 0 && -n <= words.size()) {
			String match = words.get(-n - 1);
			if (match.startsWith(prefix)) {
				// A completion is found
				String completion = match.substring(pos - w);
				// Submitting a new function to do the completion
				SwingUtilities.invokeLater(new CompletionTask(completion,
						pos + 1));
			}
		} else {
			// Nothing found
			mode = Mode.INSERT;
		}
	}

	public class CommitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ev) {
			if (mode == Mode.COMPLETION) {
				int pos = textField.getSelectionEnd();
				StringBuffer sb = new StringBuffer(textField.getText());
				sb.insert(pos, " ");
				textField.setText(sb.toString());
				textField.setCaretPosition(pos + 1);
				mode = Mode.INSERT;
			} else {
				textField.replaceSelection("");
			}
		}
	}

	private class CompletionTask implements Runnable {
		private String completion;
		private int position;

		CompletionTask(String completion, int position) {
			this.completion = completion;
			this.position = position;
		}

		public void run() {
			StringBuffer sb = new StringBuffer(textField.getText());
			sb.insert(position, completion);
			textField.setText(sb.toString());
			textField.setCaretPosition(position + completion.length());
			textField.moveCaretPosition(position);
			mode = Mode.COMPLETION;
		}
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}
}
