package mytasks.ui;
import java.awt.*;
//waits for user to do something
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
//covers text with asterix **
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.util.NoSuchElementException;
import java.util.Scanner;

import mytasks.file.MyTasks;
import mytasks.logic.MyTasksLogic;


/**
 * MyTasksUI is the UI for MyTasks. As of v0.1, UI is yet to be implemented and will be simple CLI
 * @author Wilson
 *
 */
public class MyTasksUI extends Frame implements ActionListener {
	
	private Label commandLbl;
	private JTextField commandInput;
	
	
	public boolean isRunning;
	private Scanner sc;
	MyTasksLogic mLogic;
	
	//Constructor
	public MyTasksUI(){
		super("MyTasks");
		setLayout(new FlowLayout());
		
		initUI();
		printToUI(null);
		run();
		
		commandInput = new JTextField("Enter command here: ");
		add(commandInput); 
		
		//builds an action listener object
		thehandler handler = new thehandler();
		commandInput.addActionListener(handler);

	}
	
	// class that's inside inherits all the stuff from this class
		// class that handles the events 
		private class thehandler implements ActionListener {
			
			// handles the event like click or enter
			// actionPerformed is a built-in method that is in the ActionListener class
			public void actionPerformed(ActionEvent event) {
				String string = "";
				
				// if user clicks on textfield item1 what do we want to do 
				if(event.getSource() == commandInput) {
					// we will change the string to whatever they typed in 
					string = String.format("field 1: %s", event.getActionCommand());
				}
				
				//blank window  
				JOptionPane.showMessageDialog(null, string); 
				
			}
		}
	
	/**
	 * initProgram initializes all local variables to prevent and data overflow from previous sessions
	 */
	private void initUI() {
		isRunning = true;
		sc = new Scanner(System.in);
		mLogic = new MyTasksLogic(false);
	}
	
	/**
	 * run starts the process of accepting and executing input
	 */
	private void run() {
		while (isRunning) {
			String input = acceptInput();
			String feedback = mLogic.executeCommand(input);
			printToUI(feedback);
		}
	}
	
	private String acceptInput(){
		String input = null;
		try{
			input = sc.nextLine();
		} catch (NoSuchElementException e){
			input = MyTasks.SYSTEM_SHUTDOWN;
			isRunning = false;
		}
		return input;
	}

	/**
	 * {@inheritDoc}
	 */
	public void printToUI(String feedback) {
		System.out.print(mLogic.obtainPrintableOutput());
		if (feedback!=null){
			System.out.println(feedback);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
