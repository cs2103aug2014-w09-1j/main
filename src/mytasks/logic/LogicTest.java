package mytasks.logic;

import static org.junit.Assert.*;
import mytasks.file.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class LogicTest {
	
	private MyTasksLogic taskLogic = new MyTasksLogic(true);
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Test
	public void testAddCommand() {
		assertEquals("meeting 22.09.2014 #important added", taskLogic.executeCommand("add meeting 22.09.2014 #important"));
	} 
	
	@Test
	public void testUpdateCommand() {
		assertEquals("CS2103T #important updated", taskLogic.executeCommand("update meeting - CS2103T #important"));
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
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	
	private void initTestCases(){
		//test 1
		taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
		//test 2
		taskLogic.executeCommand("add CS2101 meeting 29.09.2014");
	}
	
	@Test
	public void testSearchCommand(){
		//test 1
		assertEquals("unable to find task with keyword 'meeting'", taskLogic.executeCommand("search meeting"));
		initTestCases();
		assertEquals("task(s) with keyword 'meeting' searched", taskLogic.executeCommand("search meeting"));
		assertEquals("CS2103T meeting 22.09.2014 #important " + "\n"
		            + "CS2101 meeting 29.09.2014 ", outContent.toString());
		//test 2
		assertEquals("task(s) with keyword 'meeting #important' searched", taskLogic.executeCommand("search meeting #important"));
		assertEquals("CS2103T meeting 22.09.2014 #important ", outContent.toString());

	}
}