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


	@Test
	public void testAddCommand() {
		taskLogic.getMemory().clearMemory();	
		// taskLogic.executeCommand("add meeting 22.09.2014 #important"));
		//assertEquals("meeting added",
		//		taskLogic.executeCommand("add meeting 22.09.2014 #important"));
		//assertEquals("22.Sep.2014" + "\n" + "meeting #important" + "\n",
		//		taskLogic.obtainPrintableOutput());
	}


	@Test
	public void testUndoRedoAdd() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("add meeting 22.09.2014 #important");
		assertEquals("meeting deleted", taskLogic.executeCommand("undo"));
		// The following essentially tests undo function for delete
		assertEquals("meeting added", taskLogic.executeCommand("redo"));
	}

	
	@Test
	public void testSearchCommand() {
		taskLogic.getMemory().clearMemory();
		// test 1
		assertEquals("unable to find task with keyword 'meeting'", taskLogic.executeCommand("search meeting"));
		taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("add CS2101 meeting 29.09.2014");
		assertEquals("CS2103T meeting on 22.09.2014 #important\n"
				+ "CS2101 meeting on 29.09.2014\n" 
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("search meeting"));
		// test 2
		assertEquals("CS2103T meeting on 22.09.2014 #important\n" + "CS2101 meeting on 29.09.2014\n"
				+ "task(s) with keyword 'meeting #important' searched", taskLogic.executeCommand("search meeting #important"));
		// test 3
		taskLogic.executeCommand("add important date 1.10.2014 #meeting");
		assertEquals("CS2103T meeting on 22.09.2014 #important\n" 
				+ "CS2101 meeting on 29.09.2014\n" 
				+ "important date on 01.10.2014 #meeting\n"
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("search meeting"));
	}
	

	@Test
	public void testGetSnapshot(){
		taskLogic.getMemory().clearMemory();
		String output = "";
		// sort by date - test 1 - tasks with only start date partition
		taskLogic.executeCommand("add CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("add CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("add CS2100 Midterm 25.09.2014 #important #urgent");
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n", output);
		// sort by date - test 2 - tasks without date & time and task with date & time 
		taskLogic.executeCommand("add play badminton #anytime");
		taskLogic.executeCommand("add medical check up 1.10.2014 13:00 #$100");
		output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "N.A.\n" + "play badminton #anytime\n", output);
		// sort by date - test 3 - tasks with same startDate and tasks with start and end date
		taskLogic.executeCommand("add pay acceptance fee from 28.09.2014 to 29.09.2014 #$200");
		taskLogic.executeCommand("add do PS4 from 28.09.2014 to 30.09.2014");
		taskLogic.executeCommand("add MA1101R Midterm 25.09.2014");
		taskLogic.executeCommand("add eat sushi #KentRidge");
		output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n" + "MA1101R Midterm\n"
				+ "28.Sep.2014\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "30.Sep.2014\n" + "do PS4\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "N.A.\n" + "play badminton #anytime\n" + "eat sushi #KentRidge\n", output);
		// sort by date - test 4 - tasks with start and end DateTime partition
		taskLogic.executeCommand("add work from 10.10.2014 10:00 to 12.10.2014 13:00");
		taskLogic.executeCommand("add gaming 3.10.2014 from 10:00 to 15:00");
		output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
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
				+ "N.A.\n" + "play badminton #anytime\n" + "eat sushi #KentRidge\n", output);
		// sort by labels - test 1 - sort 1 labels
		taskLogic.executeCommand("sort important");
		output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("#important\n" + "CS2103T meeting on 22.09.2014 #important\n" 
				+ "CS2101 meeting on 29.09.2014 #important\n" 
				+ "CS2100 Midterm on 25.09.2014 #important #urgent\n" 
				+ "N.A.\n" + "play badminton #anytime\n" 
				+ "medical check up on 01.10.2014 13:00 #$100\n" 
				+ "pay acceptance fee from 28.09.2014 to 29.09.2014 #$200\n" 
				+ "do PS4 from 28.09.2014 to 30.09.2014\n" 
				+ "MA1101R Midterm on 25.09.2014\n" 
				+ "eat sushi #KentRidge\n"
				+ "work from 10.10.2014 10:00 to 12.10.2014 13:00\n"
				+ "gaming from 03.10.2014 10:00 to 03.10.2014 15:00\n", output);
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
		assertEquals("play updated", taskLogic.executeCommand("redo"));
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
		assertEquals(1, taskLogic.getMemory().getLocalMem().size());
		assertEquals("play deleted", taskLogic.executeCommand("redo"));
		assertEquals(0, taskLogic.getMemory().getLocalMem().size());
	}

	// TODO: add test cases for the working functions. Ie. search and update.
	// Follow conventions stated in v0.1
}