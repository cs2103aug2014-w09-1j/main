package mytasks.ui;

import java.awt.*;
//waits for user to do something
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import mytasks.logic.ILogic;
import mytasks.logic.MyTasksLogic;


/**
 * MyTasksUI is the UI for MyTasks. As of v0.1, UI is yet to be implemented and will be simple CLI
 * logic calls UI 
 * @author Wilson
 *
 */
public class MyTasksUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected JTextField textField;
    protected JTextArea textArea;
   	private ILogic mLogic;
	
    public MyTasksUI() {
        super(new GridBagLayout());
        
        mLogic = new MyTasksLogic(false);
        
        textField = new JTextField(20);
        textField.addActionListener(this);

        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        //Add Components to this panel.
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;

        c.fill = GridBagConstraints.HORIZONTAL;
        add(textField, c);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);        
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
        mLogic.executeCommand(text);
        textArea.setText(mLogic.obtainPrintableOutput());
        //textArea.append(mLogic.obtainPrintableOutput() + "\n");
        textField.selectAll();

        //Make sure the new text is visible, even if there
        //was a selection in the text area.
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
