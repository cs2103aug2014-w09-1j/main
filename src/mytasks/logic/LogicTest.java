package mytasks.logic;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LogicTest {

	private MyTasksLogic taskLogic = new MyTasksLogic(true);

	// private final ByteArrayOutputStream outContent = new
	// ByteArrayOutputStream();
//
//	@Test
//	public void testAddCommand() {
//		taskLogic.getMemory().clearMemory();
//		// assertEquals("meeting 22.09.2014 #important added",
//		// taskLogic.executeCommand("add meeting 22.09.2014 #important"));
//		assertEquals("meeting added",
//				taskLogic.executeCommand("add meeting 22.09.2014 #important"));
//		// assertEquals("meeting on 22.09.2014 #important" + "\n",
//		// taskLogic.obtainPrintableOutput());
//		assertEquals("22.Sep.2014" + "\n" + "meeting #important" + "\n",
//				taskLogic.obtainPrintableOutput());
//	}

//	@Test
//	public void testUndoRedoAdd() {
//		taskLogic.getMemory().clearMemory();
//		taskLogic.executeCommand("add meeting 22.09.2014 #important");
//		assertEquals("meeting deleted", taskLogic.executeCommand("undo"));
//		// The following essentially tests undo function for delete
//		assertEquals("meeting added", taskLogic.executeCommand("redo"));
//	}

	@Test
	public void testUpdateCommand() {
		taskLogic = new MyTasksLogic(true);
		taskLogic.executeCommand("add meeting");
		assertEquals("CS2103T updated", taskLogic.executeCommand("update meeting - CS2103T #important"));
	}
//
//	@Before
//	public void setUpStreams() {
//		System.setOut(new PrintStream(outContent));
//	}
//
//	@After
//	public void cleanUpStreams() {
//		System.setOut(null);
//	};

//	@Test
//	public void testUpdateCommand() {
//		assertEquals("CS2103T updated",
//				taskLogic.executeCommand("update meeting - CS2103T #important"));
//		//Check if fields are correct
//	}
//
//	@Test
//	public void testDeleteCommand() {
//		taskLogic.getMemory().clearMemory();
//		taskLogic.executeCommand("add play");
//		assertEquals("play deleted", taskLogic.executeCommand("delete play"));
//		assertEquals(0, taskLogic.getMemory().getLocalMem().size());
//	}

	/*
	 * @Before public void setUpStreams() { System.setOut(new
	 * PrintStream(outContent)); }
	 * 
	 * @After public void cleanUpStreams() { System.setOut(null); };
	 * 
	 * @Test public void testSearchCommand() { // test 1
	 * assertEquals("unable to find task with keyword 'meeting'",
	 * taskLogic.executeCommand("search meeting"));
	 * taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
	 * taskLogic.executeCommand("add CS2101 meeting 29.09.2014");
	 * assertEquals("task(s) with keyword 'meeting' searched",
	 * taskLogic.executeCommand("search meeting"));
	 * assertEquals("CS2103T meeting on 22.09.2014 #important\r\n" +
	 * "CS2101 meeting on 29.09.2014\r\n", outContent.toString()); // test 2
	 * outContent.reset();
	 * assertEquals("task(s) with keyword 'meeting #important' searched",
	 * taskLogic.executeCommand("search meeting #important"));
	 * assertEquals("CS2103T meeting on 22.09.2014 #important\r\n",
	 * outContent.toString()); // test 3 outContent.reset();
	 * taskLogic.executeCommand("add important date 1.10.2014 #meeting");
	 * assertEquals("task(s) with keyword 'meeting #important' searched",
	 * taskLogic.executeCommand("search meeting #important"));
	 * assertEquals("CS2103T meeting on 22.09.2014 #important\r\n",
	 * outContent.toString()); outContent.reset();
	 * assertEquals("task(s) with keyword 'meeting' searched",
	 * taskLogic.executeCommand("search meeting"));
	 * assertEquals("CS2103T meeting on 22.09.2014 #important\r\n" +
	 * "CS2101 meeting on 29.09.2014\r\n", outContent.toString()); }
	 * 
	 * @Test public void testGetSnapshot() { // sort by date - test 1
	 * taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
	 * taskLogic.executeCommand("add CS2101 meeting 29.09.2014");
	 * taskLogic.executeCommand("add CS2100 Midterm 25.09.2014");
	 * assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n" +
	 * "25.Sep.2014\n" + "CS2100 Midterm\n" + "29.Sep.2014\n" +
	 * "CS2101 meeting\n", taskLogic.obtainPrintableOutput()); // sort by date -
	 * test 2 - tasks without date & time
	 * taskLogic.executeCommand("add play badminton");
	 * taskLogic.executeCommand("add medical check up 1.10.2014 13:00");
	 * assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n" +
	 * "25.Sep.2014\n" + "CS2100 Midterm\n" + "29.Sep.2014\n" +
	 * "CS2101 meeting\n" + "01.Oct.2014\n" + "medical check up 13:00\n" +
	 * "N.A.\n" + "play badminton\n", taskLogic.obtainPrintableOutput()); //
	 * sort by date - test 3 - tasks with same fromDateTime taskLogic
	 * .executeCommand("add pay acceptance fee from 28.09.2014 to 29.09.2014");
	 * taskLogic.executeCommand("add do PS4 from 28.09.2014 to 30.09.2014");
	 * taskLogic.executeCommand("add MA1101R Midterm 25.09.2014");
	 * taskLogic.executeCommand("add eat sushi"); assertEquals("22.Sep.2014\n" +
	 * "CS2103T meeting #important\n" + "25.Sep.2014\n" + "CS2100 Midterm\n" +
	 * "MA1101R Midterm\n" + "28.Sep.2014\n" + "pay acceptance fee\n" +
	 * "do PS4\n" + "29.Sep.2014\n" + "CS2101 meeting\n" +
	 * "pay acceptance fee\n" + "do PS4\n" + "30.Sep.2014\n" + "do PS4\n" +
	 * "01.Oct.2014\n" + "medical check up 13:00\n" + "N.A.\n" +
	 * "play badminton\n" + "eat sushi\n", taskLogic.obtainPrintableOutput()); }
	 */
	// TODO: add test cases for the working functions. Ie. search and update.
	// Follow conventions stated in v0.1
}