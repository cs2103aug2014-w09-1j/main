package mytasks.ui;

import java.util.NoSuchElementException;
import java.util.Scanner;

import mytasks.file.MyTasks;
import mytasks.logic.MyTasksLogic;


/**
 * MyTasksUI is the UI for MyTasks. As of v0.1, UI is yet to be implemented and will be simple CLI
 * @author Wilson
 *
 */
public class MyTasksUI{
	
	public boolean isRunning;
	private Scanner sc;
	MyTasksLogic mLogic;
	
	//Constructor
	public MyTasksUI(){
		initUI();
		printToUI(null);
		run();
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

}
