package mytasks.file;

/**
 * MyTasks is a tasks management program designed for users who like to use command line interface 
 * to manage daily schedule. API for myTasks is provided in the user guide.
 * MyTasks hides the creation of UI and simply runs it upon program call
 * 
 * @author Wilson, Micheal, Huiwen, Shuansiang
 * @version 0.1
 */
public class MyTasks {
	
	//Constructor
	public MyTasks(){
		MyTasksUI uiCompo = new MyTasksUI();  //Creates ui. Ui will run in its constructor
	}
	
	public static void main(String[] args){
		MyTasks runningProgram = new MyTasks();	
	}
	
}
