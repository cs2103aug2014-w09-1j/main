package mytasks.file;

/**
 * IUI interface defines abstract methods to be used by UI component
 * @author Wilson
 *
 */
public interface IUserInterface {
	
	/**
	 * printToUI communicates to user by printing onto screen. Temporarily accepts String as input.
	 * May consider other data structures in the future.
	 * @param output to be printed
	 */
	public void printToUI(String output);
}
