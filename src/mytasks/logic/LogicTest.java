package mytasks.logic;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LogicTest {
	
	private MyTasksLogic taskLogic = new MyTasksLogic(true);
	
	@Test
	public void testAddCommand() {
		assertEquals("meeting 22.09.2014 #important added", taskLogic.executeCommand("add meeting 22.09.2014 #important"));
	} 
	
	@Test
	public void testUpdateCommand() {
		assertEquals("CS2103T #important updated", taskLogic.executeCommand("meeting - CS2103T #important"));
	} 
	
	@Test
	public void testRemoveFirstWord() {
		Method method = null;
		try {
			method = MyTasksLogic.class.getDeclaredMethod("removeFirstWord",
					String.class);
			method.setAccessible(true);
			String result = (String) method.invoke(taskLogic, "add meeting 22sep #important added");
			assertEquals("meeting 22sep #important added", result);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}	
	}	
}