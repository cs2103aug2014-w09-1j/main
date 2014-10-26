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
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import mytasks.logic.ILogic;
import mytasks.logic.MyTasksLogic;


/**
 * MyTasksUI is the UI for MyTasks. As of v0.1, UI is yet to be implemented and will be simple CLI
 * logic calls UI 
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
	
    public MyTasksUI() {
        super(new GridBagLayout());
        
        mLogic = new MyTasksLogic(false);
        textAreaLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Tasks</font>");  
        textAreaLabel.setOpaque(true);
        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.black));
        
        feedbackLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Feedback Box</font>");
        textAreaFeedback = new JTextArea(5, 50);
        textAreaFeedback.setEditable(false);
        JScrollPane scrollPaneFeedback = new JScrollPane(textAreaFeedback);
        scrollPaneFeedback.setBorder(BorderFactory.createLineBorder(Color.black));

        textfieldLabel = new JLabel("<html><center>" + "<font color=#7c5cff>Input Tasks</font>");
        textField = new JTextField(20);
        textField.addActionListener(this);        
        
//        Add Components to this panel.
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
        
        textArea.setText(mLogic.obtainPrintableOutput());  

//      Container contentPane = getRootPane();
//      contentPane.add(tasksBox, BorderLayout.CENTER);
      
//      //Create a split pane with the two scroll panes in it.
//      splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
//      		scrollPane, scrollPaneFeedback);
//      splitPane.setOneTouchExpandable(false);
//      splitPane.setDividerLocation(0.25);
//      
//      Dimension minSize = new Dimension(0, 0);
//      scrollPane.setMinimumSize(minSize);
//      scrollPaneFeedback.setMinimumSize(minSize);
//      splitPane.setResizeWeight(1.0);
	}
	
	/**
	 * run starts the process of accepting and executing input
	 */
	public void run() {     
		//Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
    
    public void actionPerformed(ActionEvent evt) {    	
        String text = textField.getText();
        textAreaFeedback.setText(mLogic.executeCommand(text));
        textArea.setText(mLogic.obtainPrintableOutput());  
        textField.selectAll();
        
//        textField.setText("");
//        textArea.append(mLogic.obtainPrintableOutput() + "\n");

//        Make sure the new text is visible, even if there
//        was a selection in the text area.
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("MyTasks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add contents to the window.
        frame.add(new MyTasksUI());

        //Display the window.
        frame.pack();
        frame.setVisible(true);  
    }
}
