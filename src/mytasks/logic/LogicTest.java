package mytasks.logic;

import static org.junit.Assert.*;

import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

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
		assertEquals("meeting on 22.09.2014 #important" + "\n", taskLogic.obtainPrintableOutput());
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
	public void setUpStreams(){
	    System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
;
	@Test
	public void testSearchCommand(){
		//test 1
		assertEquals("unable to find task with keyword 'meeting'", taskLogic.executeCommand("search meeting"));
		taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("add CS2101 meeting 29.09.2014");
		assertEquals("task(s) with keyword 'meeting' searched", taskLogic.executeCommand("search meeting"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n"
				     + "CS2101 meeting on 29.09.2014\r\n", outContent.toString());
		//test 2
		outContent.reset();
		assertEquals("task(s) with keyword 'meeting #important' searched", taskLogic.executeCommand("search meeting #important"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n", outContent.toString());
		//test 3
		outContent.reset();
		taskLogic.executeCommand("add important date 1.10.2014 #meeting");
		assertEquals("task(s) with keyword 'meeting #important' searched", taskLogic.executeCommand("search meeting #important"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n", outContent.toString());
		outContent.reset();
		assertEquals("task(s) with keyword 'meeting' searched", taskLogic.executeCommand("search meeting"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n"
				     + "CS2101 meeting on 29.09.2014\r\n", outContent.toString());
	}
	
	@Test
	public void testGetSnapshot(){
		// sort by date - test 1
		taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("add CS2101 meeting 29.09.2014");
		taskLogic.executeCommand("add CS2100 Midterm 25.09.2014");
		assertEquals("CS2103T meeting on 22.09.2014 #important\n"
				     + "CS2100 Midterm on 25.09.2014\n"
				     + "CS2101 meeting on 29.09.2014\n", taskLogic.obtainPrintableOutput());
		
	}
	
	
	
	//TODO: add test cases for the working functions. Ie. search and update. Follow conventions stated in v0.1
}