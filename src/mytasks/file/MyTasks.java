package mytasks.file;

import java.util.Date;

import mytasks.ui.MyTasksUI;

/**
 * MyTasks is a tasks management program designed for users who like to use command line interface 
 * to manage daily schedule. API for myTasks is provided in the user guide.
 * MyTasks hides the creation of UI and simply runs it upon program call
 * 
 * @author Wilson, Micheal, Huiwen, Shuansiang
 * @version 0.1
 */
public class MyTasks {
	
	public final static String DEFAULT_FILENAME = "externalmemoryfile";
	public final static String[] DEFAULT_VIEW = {"date"};
	public final static String SYSTEM_SHUTDOWN = "system off";
	public static String default_log = "log";
	
	//Constructor
	public MyTasks(){
		Date today = new Date();
		default_log +=today.hashCode();
	}
	
	public static void main(String[] args){
		MyTasks runningProgram = new MyTasks();	
		MyTasksUI uiCompo = new MyTasksUI();
		uiCompo.run();
	}
}
