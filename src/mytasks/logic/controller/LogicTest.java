package mytasks.logic.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class LogicTest {

	private MyTasksLogicController taskLogic = MyTasksLogicController.getInstance(true);

	@Test
	public void testAddCommand() {
		taskLogic.getMemory().clearMemory();
		assertEquals("meeting added", taskLogic.executeCommand("ad meeting 22.09.2014 #important").getFeedback());
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
	}


	@Test
	public void testUndoRedoAdd() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting 22.09.2014 #important");
		assertEquals("meeting deleted", taskLogic.executeCommand("un").getFeedback());
		// The following essentially tests undo function for delete
		assertEquals("meeting added", taskLogic.executeCommand("re").getFeedback());
	}
	
	@Test
	public void testUpdateCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting");
		assertEquals("'meeting' updated",
				taskLogic.executeCommand("up meeting - CS2103T #important").getFeedback());
	}

	@Test
	public void testUndoRedoUpdate() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play");
		taskLogic.executeCommand("up play - do homework");
		assertEquals("do homework reverted", taskLogic.executeCommand("un").getFeedback());
		assertEquals("'play' updated", taskLogic.executeCommand("re").getFeedback());
		assertEquals("do homework reverted", taskLogic.executeCommand("un").getFeedback());
	}
	
	@Test
	public void testDeleteCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play");
		assertEquals("'play' deleted", taskLogic.executeCommand("de play").getFeedback());
		assertEquals(0, taskLogic.getMemory().getLocalMem().size());
	}
	
	@Test
	public void testUndoRedoDelete() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play");
		taskLogic.executeCommand("de play");
		assertEquals("play added", taskLogic.executeCommand("un").getFeedback());
		assertEquals(1, taskLogic.getMemory().getLocalMem().size());
		assertEquals("'play' deleted", taskLogic.executeCommand("re").getFeedback());
		assertEquals(0, taskLogic.getMemory().getLocalMem().size());
	}


	//@author A0112139R
	@Test
	public void testSearchCommand1() {
		taskLogic.getMemory().clearMemory();
		assertEquals("unable to find task with keyword 'meeting'", taskLogic.executeCommand("se meeting").getFeedback());
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		taskLogic.executeCommand("ad important");
		// search for 1 keyword
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n"
				+ "2. CS2101 meeting on 29.09.2014\n" 
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("se meeting").getFeedback());
	}
	
	@Test
	public void testSearchCommand2() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		taskLogic.executeCommand("ad important");
		// search for multiple keywords
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n" 
				+ "2. CS2101 meeting on 29.09.2014\n" 
				+ "3. important\n"
				+ "task(s) with keyword 'meeting important' searched", taskLogic.executeCommand("se meeting important").getFeedback());
	}
	
	@Test
	public void testSearchCommand3() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		taskLogic.executeCommand("ad do homework from 21.09.2014 to 23.09.2014");
		// search for startDate
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n" 
				+ "2. do homework from 21.09.2014 to 23.09.2014\n"
				+ "task(s) with keyword 'on 22.09.2014' searched", taskLogic.executeCommand("se 22.09.2014").getFeedback());
	}
	
	@Test
	public void testSearchCommand4() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		taskLogic.executeCommand("ad do project from 24.09.2014 to 30.09.2014");
		taskLogic.executeCommand("ad pay electric bill from 1.10.2014 to 3.10.2014");
		// search for tasks between startDate and endDate
		assertEquals("1. CS2101 meeting on 29.09.2014\n" 
				+ "2. do project from 24.09.2014 to 30.09.2014\n"
				+ "3. pay electric bill from 01.10.2014 to 03.10.2014\n"
				+ "task(s) with keyword 'from 27.09.2014 to 01.10.2014' searched", taskLogic.executeCommand("se from 27.09.2014 to 1.10.2014").getFeedback());
	}
	
	@Test
	public void testDeleteWithSearch1(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		// delete by search results
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n"
				+ "2. CS2101 meeting on 29.09.2014\n" 
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("se meeting").getFeedback());
		assertEquals("'CS2103T meeting' deleted", taskLogic.executeCommand("de 1").getFeedback());
	}
	
	@Test
	public void testDeleteWithSearch2(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
		// when deleting task with same description
		assertEquals("There are multiple tasks 'meeting'. Auto search to delete the specific one.\n"
				+ "1. meeting on 01.11.2014 #CS2101\n"
				+ "2. meeting on 02.11.2014 #CS2103T\n"
				+ "3. meeting on 03.11.2014 #MA1101R\n"
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("de meeting").getFeedback());
		assertEquals("'meeting' deleted", taskLogic.executeCommand("de 1").getFeedback());
		String output = obtainOutput();
		assertEquals("02.Nov.2014\n" + "meeting #CS2103T\n"
				+ "03.Nov.2014\n" + "meeting #MA1101R\n", output);
	}
	
	@Test
	public void testDeleteWithSearch3(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
        taskLogic.executeCommand("de meeting");
		assertEquals("'meeting' deleted", taskLogic.executeCommand("de 1").getFeedback());
		// undo
		assertEquals("meeting added", taskLogic.executeCommand("un").getFeedback());
		String output = obtainOutput();
		assertEquals("01.Nov.2014\n" + "meeting #CS2101\n"
				+ "02.Nov.2014\n" + "meeting #CS2103T\n"
				+ "03.Nov.2014\n" + "meeting #MA1101R\n", output);
	}
	
	@Test
	public void testDeleteWithSearch4(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
        taskLogic.executeCommand("de meeting");
		assertEquals("'meeting' deleted", taskLogic.executeCommand("de 1").getFeedback());
		assertEquals("meeting added", taskLogic.executeCommand("un").getFeedback());
		// redo
		assertEquals("'meeting' deleted", taskLogic.executeCommand("re").getFeedback());
		String output = obtainOutput();
		assertEquals("02.Nov.2014\n" + "meeting #CS2103T\n"
				+ "03.Nov.2014\n" + "meeting #MA1101R\n", output);
	}
	
	@Test
	public void testUpdateWithSearch1(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		// update by search results
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n"
				+ "2. CS2101 meeting on 29.09.2014\n" 
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("se meeting").getFeedback());
		assertEquals("'CS2103T meeting' updated", taskLogic.executeCommand("up 1 - 25.09.2014").getFeedback());
		String output = obtainOutput();
		assertEquals("25.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "29.Sep.2014\n" + "CS2101 meeting\n", output);
	}
	
	@Test
	public void testUpdateWithSearch2(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
		// when updating task with same description
		assertEquals("There are multiple tasks 'meeting'. Auto search to update the specific one.\n"
				+ "1. meeting on 01.11.2014 #CS2101\n"
				+ "2. meeting on 02.11.2014 #CS2103T\n"
				+ "3. meeting on 03.11.2014 #MA1101R\n"
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("up meeting - 4.11.2014").getFeedback());
		assertEquals("'meeting' updated", taskLogic.executeCommand("up 1 - 4.11.2014").getFeedback());
		String output = obtainOutput();
		assertEquals("02.Nov.2014\n" + "meeting #CS2103T\n"
				+ "03.Nov.2014\n" + "meeting #MA1101R\n"
				+ "04.Nov.2014\n" + "meeting #CS2101\n", output);
	}
	
	@Test
	public void testUpdateWithSearch3(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
		taskLogic.executeCommand("up meeting - 4.11.2014");
		assertEquals("'meeting' updated", taskLogic.executeCommand("up 1 - 4.11.2014").getFeedback());
		// undo
		assertEquals("meeting reverted", taskLogic.executeCommand("un").getFeedback());
		String output = obtainOutput();
		assertEquals("01.Nov.2014\n" + "meeting #CS2101\n"
				+ "02.Nov.2014\n" + "meeting #CS2103T\n"
				+ "03.Nov.2014\n" + "meeting #MA1101R\n", output);
	}
	
	@Test
	public void testUpdateWithSearch4(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
		taskLogic.executeCommand("up meeting - 4.11.2014");
		assertEquals("'meeting' updated", taskLogic.executeCommand("up 1 - 4.11.2014").getFeedback());
		assertEquals("meeting reverted", taskLogic.executeCommand("un").getFeedback());
		// test 4 - redo
		assertEquals("'meeting' updated", taskLogic.executeCommand("re").getFeedback());
		String output = obtainOutput();
		assertEquals("02.Nov.2014\n" + "meeting #CS2103T\n"
				+ "03.Nov.2014\n" + "meeting #MA1101R\n"
				+ "04.Nov.2014\n" + "meeting #CS2101\n", output);
	}
	
	@Test
	public void testDoneWithSearch1(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so done");
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014");
		// done by search results
		assertEquals("1. CS2103T meeting on 22.09.2014 #important\n"
				+ "2. CS2101 meeting on 29.09.2014\n" 
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("se meeting").getFeedback());
		assertEquals("'CS2103T meeting' mark as done", taskLogic.executeCommand("do 1").getFeedback());
		String output = obtainOutput();
		assertEquals("#done\n" + "CS2103T meeting on 22.09.2014 #important\n"
				+ "N.A.\n" + "CS2101 meeting on 29.09.2014\n", output);
	}
	
	@Test
	public void testDoneWithSearch2(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so done");
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
		// done task with same description
		assertEquals("There are multiple tasks 'meeting'. Auto search to mark the specific one as done.\n"
				+ "1. meeting on 01.11.2014 #CS2101\n"
				+ "2. meeting on 02.11.2014 #CS2103T\n"
				+ "3. meeting on 03.11.2014 #MA1101R\n"
				+ "task(s) with keyword 'meeting' searched", taskLogic.executeCommand("do meeting").getFeedback());
		assertEquals("'meeting' mark as done", taskLogic.executeCommand("do 1").getFeedback());
		String output = obtainOutput();
		assertEquals("#done\n" + "meeting on 01.11.2014 #CS2101\n"
				+ "N.A.\n" + "meeting on 02.11.2014 #CS2103T\n" 
				+ "meeting on 03.11.2014 #MA1101R\n", output);
	}
	
	@Test
	public void testDoneWithSearch3(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so done");
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
		taskLogic.executeCommand("do meeting");
		assertEquals("'meeting' mark as done", taskLogic.executeCommand("do 1").getFeedback());
		// undo
		assertEquals("meeting undone", taskLogic.executeCommand("un").getFeedback());
		String output = obtainOutput();
		assertEquals("N.A.\n" + "meeting on 01.11.2014 #CS2101\n"
				+ "meeting on 02.11.2014 #CS2103T\n" 
				+ "meeting on 03.11.2014 #MA1101R\n", output);
	}
	
	@Test
	public void testDoneWithSearch4(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so done");
		taskLogic.executeCommand("ad meeting 1.11.2014 #CS2101");
		taskLogic.executeCommand("ad meeting 2.11.2014 #CS2103T");
		taskLogic.executeCommand("ad meeting 3.11.2014 #MA1101R");
		taskLogic.executeCommand("do meeting");
		assertEquals("'meeting' mark as done", taskLogic.executeCommand("do 1").getFeedback());
		assertEquals("meeting undone", taskLogic.executeCommand("un").getFeedback());
		// redo
		assertEquals("'meeting' mark as done", taskLogic.executeCommand("re").getFeedback());
		String output = obtainOutput();
		assertEquals("#done\n" + "meeting on 01.11.2014 #CS2101\n"
				+ "N.A.\n" + "meeting on 02.11.2014 #CS2103T\n" 
				+ "meeting on 03.11.2014 #MA1101R\n", output);
	}


	@Test
	public void testGetSnapshot1(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		// sort by date - tasks with only start date partition
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("ad CS2100 Midterm 25.09.2014 #important #urgent");
		String output = obtainOutput();
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n", output);
	}

	@Test
	public void testGetSnapshot2(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("ad CS2100 Midterm 25.09.2014 #important #urgent");
		// sort by date - tasks without date & time and task with date & time ;
		taskLogic.executeCommand("ad play badminton #anytime");
		taskLogic.executeCommand("ad medical check up 1.10.2014 13:00 #$100");
		String output = obtainOutput();	
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "N.A.\n" + "play badminton #anytime\n", output);
	}

	@Test
	public void testGetSnapshot3(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("ad CS2100 Midterm 25.09.2014 #important #urgent");
		taskLogic.executeCommand("ad play badminton #anytime");
		taskLogic.executeCommand("ad medical check up 1.10.2014 13:00 #$100");
		// sort by date - tasks with same startDate and tasks with start and end date
		taskLogic.executeCommand("ad pay acceptance fee from 28.09.2014 to 29.09.2014 #$200");
		taskLogic.executeCommand("ad do PS4 from 28.09.2014 to 30.09.2014");
		taskLogic.executeCommand("ad MA1101R Midterm 25.09.2014");
		taskLogic.executeCommand("ad eat sushi #KentRidge");
		String output = obtainOutput();	
		assertEquals("22.Sep.2014\n" + "CS2103T meeting #important\n"
				+ "25.Sep.2014\n" + "CS2100 Midterm #important #urgent\n" + "MA1101R Midterm\n"
				+ "28.Sep.2014\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "29.Sep.2014\n" + "CS2101 meeting #important\n" + "pay acceptance fee #$200\n" + "do PS4\n"
				+ "30.Sep.2014\n" + "do PS4\n"
				+ "01.Oct.2014\n" + "medical check up 13:00 #$100\n"
				+ "N.A.\n" + "play badminton #anytime\n" + "eat sushi #KentRidge\n", output);
		
	}
	
	@Test
	public void testGetSnapshot4(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("so date");
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("ad CS2100 Midterm 25.09.2014 #important #urgent");
		taskLogic.executeCommand("ad play badminton #anytime");
		taskLogic.executeCommand("ad medical check up 1.10.2014 13:00 #$100");
		taskLogic.executeCommand("ad pay acceptance fee from 28.09.2014 to 29.09.2014 #$200");
		taskLogic.executeCommand("ad do PS4 from 28.09.2014 to 30.09.2014");
		taskLogic.executeCommand("ad MA1101R Midterm 25.09.2014");
		taskLogic.executeCommand("ad eat sushi #KentRidge");
		// sort by date - tasks with start and end DateTime partition
		taskLogic.executeCommand("ad work from 10.10.2014 10:00 to 12.10.2014 13:00");
		taskLogic.executeCommand("ad gaming 3.10.2014 from 10:00 to 15:00");
		String output = obtainOutput();	
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
	}
	
	@Test
	public void testGetSnapshot5(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad CS2103T meeting 22.09.2014 #important");
		taskLogic.executeCommand("ad CS2101 meeting 29.09.2014 #important");
		taskLogic.executeCommand("ad CS2100 Midterm 25.09.2014 #important #urgent");
		taskLogic.executeCommand("ad play badminton #anytime");
		taskLogic.executeCommand("ad medical check up 1.10.2014 13:00 #$100");
		taskLogic.executeCommand("ad pay acceptance fee from 28.09.2014 to 29.09.2014 #$200");
		taskLogic.executeCommand("ad do PS4 from 28.09.2014 to 30.09.2014");
		taskLogic.executeCommand("ad MA1101R Midterm 25.09.2014");
		taskLogic.executeCommand("ad eat sushi #KentRidge");
		taskLogic.executeCommand("ad work from 10.10.2014 10:00 to 12.10.2014 13:00");
		taskLogic.executeCommand("ad gaming 3.10.2014 from 10:00 to 15:00");
		// sort by labels - sort 1 labels
		taskLogic.executeCommand("so important");
		String output = obtainOutput();
		assertEquals("#important\n" + "CS2103T meeting on 22.09.2014\n" 
				+ "CS2101 meeting on 29.09.2014\n" 
				+ "CS2100 Midterm on 25.09.2014 #urgent\n" 
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
	public void testGetSnapshot6(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad tutorial #CS2103");
		taskLogic.executeCommand("ad demo 5.11.2014 #CS2103 #important");
		taskLogic.executeCommand("ad tutorial #CS2100");
		taskLogic.executeCommand("ad lab #CS2100 #important");
		taskLogic.executeCommand("ad meeting 3.11.2014 #CS2101 #important");
		taskLogic.executeCommand("ad video making #CS2101");
		// sort by labels - sort multiple labels
		taskLogic.executeCommand("so CS2103 CS2100 important");
		String output = obtainOutput();
		assertEquals("#CS2103#important\n" + "demo on 05.11.2014\n"
				+ "#CS2100#important\n" + "lab\n"
				+ "#CS2103\n" + "tutorial\n"
				+ "#CS2100\n" + "tutorial\n"
				+ "#important\n" + "meeting on 03.11.2014 #CS2101\n"
				+ "N.A.\n" + "video making #CS2101\n", output);
	}
	
	private String obtainOutput(){
		String output = "";
		for (int i=0; i < taskLogic.obtainPrintableOutput().size(); i++)
			output += taskLogic.obtainPrintableOutput().get(i);		
		return output;
	}
	
	@Test
	public void testDoneCommand(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting");
        assertEquals("'meeting' mark as done", taskLogic.executeCommand("do meeting").getFeedback());
        taskLogic.executeCommand("so done");
        String output = obtainOutput();	
		assertEquals("#done\n" + "meeting\n", output);
	}
	
	@Test
	public void testUndoRedoDone1(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting");
		taskLogic.executeCommand("ad do homework 26.10.2014 #important");
		taskLogic.executeCommand("ad written quiz 2 25.10.2014 #CS2010 #important");
		// undo once
		assertEquals("'meeting' mark as done", taskLogic.executeCommand("do meeting").getFeedback());
		assertEquals("meeting undone", taskLogic.executeCommand("un").getFeedback());
	}
	
	public void testUndoRedoDone2(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting");
		taskLogic.executeCommand("ad do homework 26.10.2014 #important");
		taskLogic.executeCommand("ad written quiz 2 25.10.2014 #CS2010 #important");
		// undo multiple times
		taskLogic.executeCommand("do meeting");
		taskLogic.executeCommand("do do homework");
		taskLogic.executeCommand("do written quiz 2");
		assertEquals("written quiz 2 undone", taskLogic.executeCommand("un").getFeedback());
		assertEquals("do homework undone", taskLogic.executeCommand("un").getFeedback());
		assertEquals("meeting undone", taskLogic.executeCommand("un").getFeedback());
	}
	
	public void testUndoRedoDone3(){
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad meeting");
		taskLogic.executeCommand("ad do homework 26.10.2014 #important");
		taskLogic.executeCommand("ad written quiz 2 25.10.2014 #CS2010 #important");
		taskLogic.executeCommand("do meeting");
		taskLogic.executeCommand("do do homework");
		taskLogic.executeCommand("do written quiz 2");
		assertEquals("written quiz 2 undone", taskLogic.executeCommand("un").getFeedback());
		assertEquals("do homework undone", taskLogic.executeCommand("un").getFeedback());
		assertEquals("meeting undone", taskLogic.executeCommand("un").getFeedback());
		// redo 
		assertEquals("'meeting' mark as done", taskLogic.executeCommand("re").getFeedback());
		assertEquals("'do homework' mark as done", taskLogic.executeCommand("re").getFeedback());
	}
	
	//@author A0108543J
	@Test
	public void testHideCommand() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play #fun");
		taskLogic.executeCommand("ad homework");
		taskLogic.executeCommand("so fun");
		taskLogic.executeCommand("hi fun");
		assertEquals(true, taskLogic.labelsHidden);
		ArrayList<String> toHide = new ArrayList<String>();
		toHide.add("#fun");
		assertEquals(toHide, taskLogic.toHide);
		
		taskLogic.executeCommand("hi all");
		assertEquals(true, taskLogic.labelsHidden);
		ArrayList<String> allTasks = new ArrayList<String>();
		allTasks.add("#fun");
		allTasks.add("N.A.");
		assertEquals(allTasks, taskLogic.toHide);
		
		taskLogic.executeCommand("hi all fun");
		assertEquals(allTasks, taskLogic.toHide);
		
		allTasks.remove(0);
		taskLogic.executeCommand("hi N.A.");
		assertEquals(allTasks, taskLogic.toHide);
		
	}
	
	//@author A0114302A
	@Test
	public void testHideCommand2() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play #fun 18.09.2014");
		taskLogic.executeCommand("ad homework");
		taskLogic.executeCommand("so date");
		ArrayList<String> newTasks = new ArrayList<String>();
		newTasks.add("18.Sep.2014");
		taskLogic.executeCommand("hi 18.Sep.2014");
		assertEquals(true, taskLogic.labelsHidden);
		assertEquals(newTasks, taskLogic.toHide);
	}
	
	@Test
	public void testHideCommand3() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play #fun 18.09.2014");
		taskLogic.executeCommand("ad homework #fun #gg");
		taskLogic.executeCommand("ad playmore");
		taskLogic.executeCommand("ad playgg #gg");
		taskLogic.executeCommand("so fun gg");
		ArrayList<String> newTasks = new ArrayList<String>();
		newTasks.add("#fun");
		newTasks.add("#fun#gg");
		newTasks.add("#gg");
		taskLogic.executeCommand("hi fun gg");
		assertEquals(true, taskLogic.labelsHidden);
		assertEquals(newTasks, taskLogic.toHide);
	}
	
	@Test
	public void testHideCommand4() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad play #fun 18.09.2014");
		taskLogic.executeCommand("ad homework #fun #gg");
		taskLogic.executeCommand("ad playmore");
		taskLogic.executeCommand("ad playgg #gg");
		taskLogic.executeCommand("so fun gg");
		taskLogic.executeCommand("hi fun gg");
		taskLogic.executeCommand("so date");
		assertEquals(false, taskLogic.labelsHidden);
	}
	
	@Test
	public void testShowCommand1() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad watch webcast #boring");
		taskLogic.executeCommand("ad do tutorial");
		taskLogic.executeCommand("so boring");
		taskLogic.executeCommand("hi boring");
		
		taskLogic.executeCommand("sh boring");
		assertEquals(false, taskLogic.labelsHidden);
	}
	
	@Test
	public void testShowCommand2() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad watch webcast #boring");
		taskLogic.executeCommand("ad do tutorial");
		taskLogic.executeCommand("so boring");
		taskLogic.executeCommand("hi boring");
		
		taskLogic.executeCommand("sh all");
		assertEquals(false, taskLogic.labelsHidden);
	}
	
	@Test
	public void testShowCommand3() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad watch webcast #boring");
		taskLogic.executeCommand("ad do tutorial");
		taskLogic.executeCommand("so boring");
		taskLogic.executeCommand("hi all");
		
		taskLogic.executeCommand("sh boring");
		assertEquals(true, taskLogic.labelsHidden);
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("N.A.");
		assertEquals(labels, taskLogic.toHide);
	}
	
	@Test
	public void testShowCommand4() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad watch webcast #boring");
		taskLogic.executeCommand("ad do tutorial #fun");
		taskLogic.executeCommand("ad sleep #boring #fun");
		taskLogic.executeCommand("ad randomcomd");
		taskLogic.executeCommand("so boring fun");
		taskLogic.executeCommand("hi all");
		
		taskLogic.executeCommand("sh boring fun");
		assertEquals(true, taskLogic.labelsHidden);
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("N.A.");
		assertEquals(labels, taskLogic.toHide);
	}
	
	@Test
	public void testShowCommand5() {
		taskLogic.getMemory().clearMemory();
		taskLogic.executeCommand("ad watch webcast #boring");
		taskLogic.executeCommand("ad do tutorial #fun");
		taskLogic.executeCommand("ad sleep #boring #fun");
		taskLogic.executeCommand("ad randomcomd");
		taskLogic.executeCommand("so boring fun");
		taskLogic.executeCommand("hi all");
		
		taskLogic.executeCommand("sh boring");
		assertEquals(true, taskLogic.labelsHidden);
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("#boring#fun");
		labels.add("#fun");
		labels.add("N.A.");
		assertEquals(labels, taskLogic.toHide);
	}
}