package mytasks.ui;

/**
 * IUI interface defines abstract methods to be used by UI component
 * @author Wilson
 *
 */
public interface IUserInterface {
	
	/**
	 * printToUI communicates to user by printing onto screen.
	 * @input feedback from Logic with regards to status of last command 
	 */
	public void printToUI(String feedback);
}
