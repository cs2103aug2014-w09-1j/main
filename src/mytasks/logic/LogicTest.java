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

	//private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Test
	public void testAddCommand() {
		taskLogic.getMemory().clearMemory();
		// assertEquals("meeting 22.09.2014 #important added",
		// taskLogic.executeCommand("add meeting 22.09.2014 #important"));
		assertEquals("meeting added",
				taskLogic.executeCommand("add meeting 22.09.2014 #important"));
		assertEquals("22.Sep.2014" + "\n" + "meeting #important" + "\n",
				taskLogic.obtainPrintableOutput());
	}


	@Test
	public void testUndoRedoAdd() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("add meeting 22.09.2014 #important");
		assertEquals("meeting deleted", taskLogic.executeCommand("undo"));
		// The following essentially tests undo function for delete
		assertEquals("meeting added", taskLogic.executeCommand("redo"));
	}

    /***
	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
	};

	@Test
	public void testSearchCommand() {
		taskLogic.getMemory().clearMemory();
		// test 1
		assertEquals("unable to find task with keyword 'meeting'",
				taskLogic.executeCommand("search meeting"));
		taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("add CS2101 meeting 29.09.2014");
		assertEquals("task(s) with keyword 'meeting' searched",
				taskLogic.executeCommand("search meeting"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n"
				+ "CS2101 meeting on 29.09.2014\r\n", outContent.toString());
		// test 2
		outContent.reset();
		assertEquals("task(s) with keyword 'meeting #important' searched",
				taskLogic.executeCommand("search meeting #important"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n",
				outContent.toString());
		// test 3
		outContent.reset();
		taskLogic.executeCommand("add important date 1.10.2014 #meeting");
		assertEquals("task(s) with keyword 'meeting #important' searched",
				taskLogic.executeCommand("search meeting #important"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n",
				outContent.toString());
		outContent.reset();
		assertEquals("task(s) with keyword 'meeting' searched",
				taskLogic.executeCommand("search meeting"));
		assertEquals("CS2103T meeting on 22.09.2014 #important\r\n"
				+ "CS2101 meeting on 29.09.2014\r\n", outContent.toString());		
	}
	***/
	
	@Test
	public void testGetSnapshot(){
		taskLogic.getMemory().clearMemory();
		// sort by date - test 1
		taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("add CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("add CS2100 Midterm 25.09.2014 #important #urgent");
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n", taskLogic.obtainPrintableOutput());
		// sort by date - test 2 - tasks without date & time
		taskLogic.executeCommand("add play badminton #anytime");
		taskLogic.executeCommand("add medical check up 1.10.2014 13:00 #$100");
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "N.A.\n" + "play badminton #anytime\n", taskLogic.obtainPrintableOutput());
		// sort by date - test 3 - tasks with same fromDateTime
		taskLogic.executeCommand("add pay acceptance fee from 28.09.2014 to 29.09.2014 #$200");
		taskLogic.executeCommand("add do PS4 from 28.09.2014 to 30.09.2014");
		taskLogic.executeCommand("add MA1101R Midterm 25.09.2014");
		taskLogic.executeCommand("add eat sushi #KentRidge");
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n" + "MA1101R Midterm\n"
				+ "28.Sep.2014\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "30.Sep.2014\n" + "do PS4\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "N.A.\n" + "play badminton #anytime\n" + "eat sushi #KentRidge\n", taskLogic.obtainPrintableOutput());
		// sort by date - test 4 - tasks with from and to dateTime
		taskLogic.executeCommand("add work from 10.10.2014 10:00 to 12.10.2014 13:00");
		taskLogic.executeCommand("add gaming 3.10.2014 from 10:00 to 15:00");
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n" + "MA1101R Midterm\n"
				+ "28.Sep.2014\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "30.Sep.2014\n" + "do PS4\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "03.Oct.2014\n" + "gaming from 10:00 to 15:00\n"
				+ "10.Oct.2014\n" + "work 10:00\n"
				+ "11.Oct.2014\n" + "work\n"
				+ "12.Oct.2014\n" + "work till 13:00\n"
				+ "N.A.\n" + "play badminton #anytime\n" + "eat sushi #KentRidge\n", taskLogic.obtainPrintableOutput());
		// sort by labels - test 1
		taskLogic.executeCommand("sort important");
		assertEquals("CS2103T meeting on 22.09.2014 #important\n" 
				+ "CS2101 meeting on 29.09.2014 #important\n" 
				+ "CS2100 Midterm on 25.09.2014 #important #urgent\n" 
				+ "play badminton #anytime\n" 
				+ "medical check up on 01.10.2014 13:00 #$100\n" 
				+ "pay acceptance fee from 28.09.2014 to 29.09.2014 #$200\n" 
				+ "do PS4 from 28.09.2014 to 30.09.2014\n" 
				+ "MA1101R Midterm on 25.09.2014\n" 
				+ "eat sushi #KentRidge\n", taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testUpdateCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("add meeting");
		assertEquals("meeting updated",
				taskLogic.executeCommand("update meeting - CS2103T #important"));
	}

	@Test
	public void testUndoRedoUpdate() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("add play");
		taskLogic.executeCommand("update play - do homework");
		assertEquals("do homework reverted", taskLogic.executeCommand("undo"));
	}
	
	@Test
	public void testDeleteCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("add play");
		assertEquals("play deleted", taskLogic.executeCommand("delete play"));
		assertEquals(0, taskLogic.getMemory().getLocalMem().size());
	}
	
	@Test
	public void testUndoRedoDelete() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("add play");
		taskLogic.executeCommand("delete play");
		assertEquals("play added", taskLogic.executeCommand("undo"));
	}

	// TODO: add test cases for the working functions. Ie. search and update.
	// Follow conventions stated in v0.1
}