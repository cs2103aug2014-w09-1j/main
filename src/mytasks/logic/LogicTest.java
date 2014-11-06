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

	private MyTasksLogicController taskLogic = MyTasksLogicController.getInstance(true);


	@Test
	public void testAddCommand() {
		taskLogic.getMemory().clearMemory();	
		taskLogic.executeCommand("ad meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad Beyond Earth #CivV");
		taskLogic.executeCommand("ad OP2 3.11.2014");
		taskLogic.executeCommand("ad demo 5.11.2014 #CS2103 #V0.4");
		taskLogic.executeCommand("ad lab quiz 11.11.2014 #MA1101R");
		//assertEquals("meeting added",
		//		taskLogic.executeCommand("add meeting 22.09.2014 #important"));
		//assertEquals("22.Sep.2014" + "\n" + "meeting #important" + "\n",
		//		taskLogic.obtainPrintableOutput());
	}


	@Test
	public void testUndoRedoAdd() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting 22.09.2014 #important");
		assertEquals("meeting deleted", taskLogic.executeCommand("un"));
		// The following essentially tests undo function for delete
		assertEquals("meeting added", taskLogic.executeCommand("re"));
	}

	//@author A0112139R
	@Test
	public void testSearchCommand() {
		taskLogic.getMemory().clearMemory();
		// test 1 - search for 1 keyword
		assertEquals("unable to find task with keyword 'meeting'", taskLogic.executeCommand("se meeting"));
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		taskLogic.executeCommand("ad important");
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n"
				+ "2. CS2101 meeting on 29.09.2014\n" 
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("se meeting"));
		// test 2 - search for multiple keywords
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n" 
				+ "2. CS2101 meeting on 29.09.2014\n" 
				+ "3. important\n"
				+ "task(s) with keyword 'meeting important' searched", taskLogic.executeCommand("se meeting important"));
	}

	@Test
	public void testGetSnapshot(){
		taskLogic.getMemory().clearMemory();
		String output = "";
		 taskLogic.executeCommand("so date");
		// sort by date - test 1 - tasks with only start date partition
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("ad CS2100 Midterm 25.09.2014 #important #urgent");
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n", output);
		// sort by date - test 2 - tasks without date & time and task with date & time 
		taskLogic.executeCommand("ad play badminton #anytime");
		taskLogic.executeCommand("ad medical check up 1.10.2014 13:00 #$100");
		output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "N.A.\n" + "play badminton #anytime\n", output);
		// sort by date - test 3 - tasks with same startDate and tasks with start and end date
		taskLogic.executeCommand("ad pay acceptance fee from 28.09.2014 to 29.09.2014 #$200");
		taskLogic.executeCommand("ad do PS4 from 28.09.2014 to 30.09.2014");
		taskLogic.executeCommand("ad MA1101R Midterm 25.09.2014");
		taskLogic.executeCommand("ad eat sushi #KentRidge");
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
		taskLogic.executeCommand("ad work from 10.10.2014 10:00 to 12.10.2014 13:00");
		taskLogic.executeCommand("ad gaming 3.10.2014 from 10:00 to 15:00");
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
		taskLogic.executeCommand("so important");
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
		// sort by labels - test 2 - sort multiple labels
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad tutorial #CS2103");
		taskLogic.executeCommand("ad demo 5.11.2014 #CS2103 #important");
		taskLogic.executeCommand("ad tutorial #CS2100");
		taskLogic.executeCommand("ad lab #CS2100 #important");
		taskLogic.executeCommand("ad meeting 3.11.2014 #CS2101 #important");
		taskLogic.executeCommand("ad video making #CS2101");
		taskLogic.executeCommand("so CS2103 CS2100 important");
		output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("#CS2103#important\n" 
				+ "demo on 05.11.2014 #CS2103 #important\n"
				+ "#CS2100#important\n"
				+ "lab #CS2100 #important\n"
				+ "#CS2103\n"
				+ "tutorial #CS2103\n"
				+ "#CS2100\n"
				+ "tutorial #CS2100\n"
				+ "#important\n"
				+ "meeting on 03.11.2014 #CS2101 #important\n"
				+ "N.A.\n"
				+ "video making #CS2101\n", output);
	}
	
	@Test
	public void testDoneCommand(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting");
        taskLogic.executeCommand("do meeting");
        taskLogic.executeCommand("so done");
		String output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		assertEquals("#done\n" + "meeting #done\n", output);
	}
	
	@Test
	public void testUpdateCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting");
		assertEquals("'meeting' updated",
				taskLogic.executeCommand("up meeting - CS2103T #important"));
	}

	@Test
	public void testUndoRedoUpdate() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play");
		taskLogic.executeCommand("up play - do homework");
		assertEquals("do homework reverted", taskLogic.executeCommand("un"));
		assertEquals("'play' updated", taskLogic.executeCommand("re"));
		assertEquals("do homework reverted", taskLogic.executeCommand("un"));
	}
	
	@Test
	public void testDeleteCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play");
		assertEquals("'play' deleted", taskLogic.executeCommand("de play"));
		assertEquals(0, taskLogic.getMemory().getLocalMem().size());
	}
	
	@Test
	public void testUndoRedoDelete() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play");
		taskLogic.executeCommand("de play");
		assertEquals("play added", taskLogic.executeCommand("un"));
		assertEquals(1, taskLogic.getMemory().getLocalMem().size());
		assertEquals("'play' deleted", taskLogic.executeCommand("re"));
		assertEquals(0, taskLogic.getMemory().getLocalMem().size());
	}

	//@author A0108543J
	@Test
	public void testHideCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play #fun");
		taskLogic.executeCommand("ad homework");
		assertEquals("Label with 'fun' hidden", taskLogic.executeCommand("hi fun"));
	}
	// TODO: add test cases for the working functions. Ie. search and update.
	// Follow conventions stated in v0.1
}