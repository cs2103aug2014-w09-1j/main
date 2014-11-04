package mytasks.file;

import java.util.Date;

import mytasks.logic.ILogic;
import mytasks.logic.MyTasksLogicController;
import mytasks.ui.MyTasksUI;

/**
 * MyTasks is a tasks management program designed for users who like to use command line interface 
 * to manage daily schedule. API for myTasks is provided in the user guide.
 * MyTasks hides the creation of UI and simply runs it upon program call
 * 
 * @author Wilson, Micheal, Huiwen, Shuansiang
 * @version 0.1
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
