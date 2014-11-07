package mytasks.file;

import java.util.Date;

import mytasks.ui.MyTasksUI;

//@author A0114302A
/**
 * MyTasksController acts as the controller of MyTasks. However, this is shared with the UI
 * MyTasksController hides the creation of UI and simply runs it upon program call
 */
public class MyTasksController {
	
	public final static String DEFAULT_FILENAME = "externalmemoryfile";
	public final static String[] DEFAULT_VIEW = {"date"};
	public final static String SYSTEM_SHUTDOWN = "system off";
	public static String default_log = "log";
	
	private MyTasksUI UIComponent = null;
	
	//Constructor
	public MyTasksController(){
		Date today = new Date();
		default_log +=today.hashCode();
		UIComponent = MyTasksUI.getInstance();
	}
	
	public static void main(String[] args){
		MyTasksController runningProgram = new MyTasksController();
		runningProgram.UIComponent.run();
	}
}
