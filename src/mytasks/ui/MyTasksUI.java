package mytasks.ui;

import java.awt.*;
//waits for user to do something
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;
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
import mytasks.logic.ILogic;
import mytasks.logic.MyTasksLogicController;

//@author A0115034X

public class MyTasksUI extends JPanel implements ActionListener,
		DocumentListener, Serializable {
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
	private boolean lookingFor = false;
	private int w = 0;
	private static MyTasksUI INSTANCE = null;

	private Mode mode = Mode.INSERT;
	private List<String> words;
	private ArrayList<String> commands;

	private static enum Mode {
		INSERT, COMPLETION
	};
	
	public static MyTasksUI getInstance(){
		if (INSTANCE == null){
			INSTANCE= new MyTasksUI();
		}
		return INSTANCE;
	}
	
	protected Object readResolve() {
		return INSTANCE;
	}
	
	private MyTasksUI() {
		super(new GridBagLayout());
		mLogic = MyTasksLogicController.getInstance(false);
		
		// for tasks label and box 
		textAreaLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Tasks</font>");
		textAreaLabel.setOpaque(true);
		textAreaLabel.setFocusable(false);

		// A border that puts 10 extra pixels at the sides and bottom of each pane.
		paneEdge = BorderFactory.createEmptyBorder(0, 10, 10, 10);
		textAreaPanel = new JPanel();
		textAreaPanel.setBorder(paneEdge);
		textAreaPanel.setLayout(new BoxLayout(textAreaPanel, BoxLayout.Y_AXIS));
		textAreaPanel.removeAll();
		textAreaPanel.revalidate();
		textAreaPanel.repaint();
		textAreaPanel.setFocusable(false);

		if (mLogic.obtainPrintableOutput().size() == 0) {
			textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.setFocusable(false);

			Border colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
			titled = BorderFactory.createTitledBorder(colourLine, "Welcome! Add your tasks below (:");
			textArea.setBorder(titled);
			textArea.setFocusable(false);
			textAreaPanel.add(textArea);
			textArea.setCaretPosition(0);
		} else {
			for (int i = 0; i < mLogic.obtainPrintableOutput().size(); i++) {
				String firstWord = mLogic.obtainPrintableOutput().get(i).split("\\s+")[0];
  
				textArea = new JTextArea();
				textArea.setEditable(false);
				textArea.setFocusable(false);

				Border colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 200), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
				titled = BorderFactory.createTitledBorder(colourLine, firstWord);
				
				String content = mLogic.obtainPrintableOutput().get(i).replace(firstWord, "").trim();
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
			}
		}
		scrollPane = new JScrollPane(textAreaPanel);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPane.setPreferredSize(new Dimension(500, 400));
		scrollPane.setFocusable(true);       
        
		// for feedback label and box 
		feedbackLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Feedback Box</font>");
		feedbackLabel.setFocusable(false);
		textAreaFeedback = new JTextArea(10, 40);
		textAreaFeedback.setEditable(false);
		textAreaFeedback.setFocusable(false);
		JScrollPane scrollPaneFeedback = new JScrollPane(textAreaFeedback);
		scrollPaneFeedback.setBorder(BorderFactory.createLineBorder(Color.black));
		scrollPaneFeedback.setFocusable(true);
		
		// for textfield label and box 
		textfieldLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Input Tasks</font>");
		textfieldLabel.setFocusable(false);
		textField = new JTextField(20);
		textField.addActionListener(this);
		textField.getDocument().addDocumentListener(this);
		textField.setFocusable(true);

		// for autocomplete 
		InputMap im = textField.getInputMap();
		ActionMap am = textField.getActionMap();
		im.put(KeyStroke.getKeyStroke("RIGHT"), "commit");
		am.put("commit", new CommitAction());
		
		commands = new ArrayList<String>();
		commands.add("ad");
		commands.add("se");
		commands.add("de");
		commands.add("do");
		commands.add("un");
		commands.add("re");
		commands.add("up");
		commands.add("so");
		commands.add("?");
		commands.add("he");
		commands.add("help");

		words = new ArrayList<String>(commands);
		for (int i = 0; i < mLogic.obtainAllTaskDescription().size(); i++) {
			words.add(mLogic.obtainAllTaskDescription().get(i));
		}
		for(int i = 0; i < mLogic.obtainAllLabels().size(); i++) {
			words.add(mLogic.obtainAllLabels().get(i));
		}
		Collections.sort(words);

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
		lookingFor = false;
		w = 0;
		boolean isRed = false, isHide = false;
		Border colourLine;
		List<String> labelsToHide = new ArrayList<String>();
		
		String text = textField.getText();
		FeedbackObject feedback = mLogic.executeCommand(text);		
		
		if (feedback != null){
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
		textAreaPanel.removeAll();
		textAreaPanel.revalidate();
		textAreaPanel.repaint();
		
		words = new ArrayList<String>(commands);
		for(int i = 0; i < mLogic.obtainAllTaskDescription().size(); i++) {
			words.add(mLogic.obtainAllTaskDescription().get(i));
		}
		for(int i = 0; i < mLogic.obtainAllLabels().size(); i++) {
			words.add(mLogic.obtainAllLabels().get(i));
		}
		
		Collections.sort(words);
		
		if (mLogic.obtainPrintableOutput().size() == 0) {
			textArea = new JTextArea();
			textArea.setEditable(false);
			textArea.setFocusable(false);
			
			titled = BorderFactory.createTitledBorder("Welcome! Add your tasks below (:");
			textArea.setBorder(titled);
			textAreaPanel.add(textArea);
			textArea.setCaretPosition(0);
		} else {
			if(mLogic.checkIfToHide()) {
				labelsToHide = mLogic.labelsToHide();
			}
			for (int i = 0; i < mLogic.obtainPrintableOutput().size(); i++) {
				textArea = new JTextArea();
				textArea.setEditable(false);				
				textArea.setFocusable(false);
				isHide = false; 
				
				String firstWord = mLogic.obtainPrintableOutput().get(i).split("\\s+")[0];
				if(firstWord.equals("#important")) {
					isRed = true;
				}
				if(labelsToHide.size() > 0) {
					for(int k = 0; k < labelsToHide.size(); k++) {
						if(labelsToHide.get(k).equals(firstWord)) {
							isHide = true;
						}
					}
				}
				if(isHide) {
					if(isRed) {
						colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
					} else {
						colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
					}
						
					titled = BorderFactory.createTitledBorder(colourLine, firstWord);
					textArea.setText("");
					textArea.setBorder(titled);
						
					GridBagConstraints c = new GridBagConstraints();
					c.gridwidth = GridBagConstraints.REMAINDER;
					c.fill = GridBagConstraints.VERTICAL;
					c.gridx = 0;
					c.weightx = 1.0;
					c.weighty = 1.0;

					textAreaPanel.add(textArea, c);
					textArea.setCaretPosition(0);
				}
				else {
					if(isRed) {
						colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
					} else {
						colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
					}
					titled = BorderFactory.createTitledBorder(colourLine, firstWord);
					String content = mLogic.obtainPrintableOutput().get(i).replace(firstWord, "").trim();
					textArea.setText(content);
					textArea.setBorder(titled);
	
					GridBagConstraints c = new GridBagConstraints();
					c.gridwidth = GridBagConstraints.REMAINDER;
					c.fill = GridBagConstraints.VERTICAL;
					c.gridx = 0;
					c.weightx = 1.0;
					c.weighty = 1.0;

					textAreaPanel.add(textArea, c);
					textArea.setCaretPosition(0);
				}
			}
//				else if(firstWord.equals("#important")) {
//					isRed = true;
//					colourLine = BorderFactory.createLineBorder(new Color(255, 0, 0), 3);
//					titled = BorderFactory.createTitledBorder(colourLine, firstWord);
//					
//					String content = mLogic.obtainPrintableOutput().get(i).replace(firstWord, "").trim();
//					textArea.setText(content);
//					textArea.setBorder(titled);
//					textAreaPanel.add(textArea);
//				} else {
//					if(isRed) {
//						colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
//					} else {
//						colourLine = BorderFactory.createLineBorder(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)), 3);
//					}
//					titled = BorderFactory.createTitledBorder(colourLine, firstWord);
//					String content = mLogic.obtainPrintableOutput().get(i).replace(firstWord, "").trim();
//					textArea.setText(content);
//					textArea.setBorder(titled);
//
//					GridBagConstraints c = new GridBagConstraints();
//					c.gridwidth = GridBagConstraints.REMAINDER;
//					c.fill = GridBagConstraints.VERTICAL;
//					c.gridx = 0;
//					c.weightx = 1.0;
//					c.weighty = 1.0;
//
//					textAreaPanel.add(textArea, c);
//					textArea.setCaretPosition(0);
//				}
			
		}
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
		
		// pressing the esc key will close the window
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EXIT");
		frame.getRootPane().getActionMap().put("EXIT", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
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

		// Too few chars
		if (pos - w < 1)
			return;

		String prefix = content.substring(w + 1).toLowerCase();
		int n = Collections.binarySearch(words, prefix);
		if (n < 0 && -n <= words.size()) {
			String match = words.get(-n - 1);
			if (match.startsWith(prefix)) {
				// A completion is found
				String completion = match.substring(pos - w);
				// We cannot modify Document from within notification,
				// so we submit a task that does the change later
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
