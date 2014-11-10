package mytasks.logic.controller;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.logic.parser.MyTasksParser;

//@author A0115034X 
/**
 * TODO: check show/hide see correct not. check error handling? check boundary cases 
 *
 */

public class IntegrationTesting {
	
	private MyTasksLogicController taskLogic = MyTasksLogicController.getInstance(true);
	private LocalMemory mLocalMem = LocalMemory.getInstance(); 
	private ArrayList<String> labels = new ArrayList<String>();
	private Task newTask;
	
	private void init() {
		mLocalMem.clearMemory();
		labels = new ArrayList<String>();
	}
	
	private Task createDateTimeTask(String taskDesc, String string, String string2) {
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateTimeFormats.get(0).parse(string);
			date2 = MyTasksParser.dateTimeFormats.get(0).parse(string2);
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return new Task(taskDesc, date1, date2, labels);		 
	}
	
	private Task createDateTask(String taskDesc, String string, String string2) {
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Task(taskDesc, date1, date2, labels);		 
	}
	
	@Test
	public void testAddAll() {		
		init();
		
		FeedbackObject feedback = taskLogic.executeCommand("ad meeting 01.10.2014 from 12:23 to 20:30 #important");		
		newTask = createDateTimeTask("meeting", "01.10.2014 12:23", "01.10.2014 20:30"); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "meeting added");
		
		//checking whether task is added correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("01.Oct.2014\n" + "meeting from 12:23 to 20:30 #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testAddFail() {		
		init();
		
		FeedbackObject feedback = taskLogic.executeCommand("ad #important 24.11.2014");	
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), false);
		assertEquals(feedback.getFeedback(), "Invalid input");
	}
	
	@Test
	public void testUndoAdd() {
		init();

		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete coding for project from 05.11.2014 12pm to 07.11.2014 4pm #finalrush");
		taskLogic.executeCommand("ad complete assignment #important");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTimeTask("complete coding for project", "05.11.2014 12:00", "07.11.2014 16:00"); 
		labels.add("finalrush");
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "complete assignment deleted");
		
		//checking whether task is deleted from local memory 
		assertEquals(2, mLocalMem.getLocalMem().size());	
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(1).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(1).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(1).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(1).getLabels().get(0));
				
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("05.Nov.2014\n" + "complete coding for project 12:00 #finalrush\n");
		toCheck.add("06.Nov.2014\n" + "complete coding for project #finalrush\n");
		toCheck.add("07.Nov.2014\n" + "complete coding for project till 16:00 #finalrush\n");
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testRedoAdd() {
		init();

		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete coding for project from 05.11.2014 12pm to 07.11.2014 4pm #finalrush");
		taskLogic.executeCommand("ad complete assignment #important");
		taskLogic.executeCommand("un");
		FeedbackObject feedback = taskLogic.executeCommand("re");
		
		labels.add("important");
		newTask = new Task("complete assignment", null, null, labels);		
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "complete assignment added");
		
		//checking whether redone task is added into local memory 
		assertEquals(3, mLocalMem.getLocalMem().size());	
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(2).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(2).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(2).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(2).getLabels().get(0));
				
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("05.Nov.2014\n" + "complete coding for project 12:00 #finalrush\n");
		toCheck.add("06.Nov.2014\n" + "complete coding for project #finalrush\n");
		toCheck.add("07.Nov.2014\n" + "complete coding for project till 16:00 #finalrush\n");
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n" + "complete assignment #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}

	@Test
	public void testDelete() {
		init();
		
		FeedbackObject feedback = taskLogic.executeCommand("ad meeting two 03.10.2014 #wecandoit");
		taskLogic.executeCommand("ad meeting 02.10.2014 12:23 #important");
		taskLogic.executeCommand("de meeting");
		
		newTask = createDateTask("meeting two", "03.10.2014", null); 
		labels.add("wecandoit"); 

		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "meeting two added");
		
		//checking whether the correct task is deleted from memory 
		assertEquals(1, mLocalMem.getLocalMem().size());
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("03.Oct.2014\n" + "meeting two #wecandoit\n");;
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	} 
	
	@Test
	public void testUndoDelete() {
		init();
		
		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete homework 15.11.2014 #finals");
		taskLogic.executeCommand("de complete homework");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTask("complete homework", "15.11.2014", null); 
		labels.add("finals");
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "complete homework added");
		
		//checking whether deleted task is added properly into local memory
		assertEquals(2, mLocalMem.getLocalMem().size());
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(1).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(1).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(1).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(1).getLabels().get(0));

		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("15.Nov.2014\n" + "complete homework #finals\n");
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testRedoDelete() {
		init();
		
		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete homework 15.11.2014 #finals");
		taskLogic.executeCommand("de complete homework");
		taskLogic.executeCommand("un");
		FeedbackObject feedback = taskLogic.executeCommand("re");
		
		labels.add("tomorrow");
		labels.add("test");
		newTask = new Task("do homework", null, null, labels);

		//checking for feedback to user
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'complete homework' deleted");
		
		//checking whether task is deleted correctly 
		assertEquals(1, mLocalMem.getLocalMem().size());
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));

		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testUpdateAll() {
		init();
		
		taskLogic.executeCommand("ad meeting 04.10.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up meeting - meeting for CS2103T 05.10.2014 #letsdothis");	
		
		newTask = createDateTask("meeting for CS2103T", "05.10.2014", null); 
		labels.add("letsdothis"); 

		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'meeting' updated");
		
		//checking whether task is updated correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("05.Oct.2014\n" + "meeting for CS2103T #letsdothis\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	} 
	
	@Test
	public void testUndoUpdateAll() {
		init();
		
		taskLogic.executeCommand("ad meeting 04.10.2014 #important");
		taskLogic.executeCommand("up meeting - meeting for CS2103T 05.10.2014 #letsdothis");
		FeedbackObject feedback = taskLogic.executeCommand("un");	
		
		newTask = createDateTask("meeting", "04.10.2014", null); 
		labels.add("important"); 

		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "meeting for CS2103T reverted");
		
		//checking whether task is reverted correctly in local memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("04.Oct.2014\n" + "meeting #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testRedoUpdateAll() {
		init();
		
		taskLogic.executeCommand("ad meeting 04.10.2014 #important");
		taskLogic.executeCommand("up meeting - meeting for CS2103T 05.10.2014 #letsdothis");	
		taskLogic.executeCommand("un");	
		FeedbackObject feedback = taskLogic.executeCommand("re");	
		
		newTask = createDateTask("meeting for CS2103T", "05.10.2014", null); 
		labels.add("letsdothis"); 

		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'meeting' updated");
		
		//checking whether task is updated correctly in local memory 
		assertEquals(1, mLocalMem.getLocalMem().size());
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("05.Oct.2014\n" + "meeting for CS2103T #letsdothis\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testUpdateTaskDesc() {
		init();
		
		taskLogic.executeCommand("ad meeting for 2103 04.11.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up meeting for 2103 - documentation meeting");
				
		newTask = createDateTask("documentation meeting", "04.11.2014", null); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'meeting for 2103' updated");
		
		//checking whether task is updated correctly in local memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("04.Nov.2014\n" + "documentation meeting #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	} 
	
	@Test
	public void testUndoUpdateTaskDesc() {
		init();

		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete assignment homework 08.11.2014 #important");
		taskLogic.executeCommand("up complete assignment homework - finish assignment");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTask("complete assignment homework", "08.11.2014", null); 
		labels.add("important");

		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "finish assignment reverted");
		
		//checking whether task is undone properly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
				
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("08.Nov.2014\n" + "complete assignment homework #important\n");
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testRedoUpdateTaskDesc() {
		init();

		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete assignment homework 08.11.2014 #important");
		taskLogic.executeCommand("up complete assignment homework - finish assignment");
		taskLogic.executeCommand("un");
		FeedbackObject feedback = taskLogic.executeCommand("re");
		
		newTask = createDateTask("finish assignment", "08.11.2014", null); 
		labels.add("important");

		//checking for feedback to user
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'complete assignment homework' updated");
		
		//checking whether task is updated correctly in memory 
		assertEquals(2, mLocalMem.getLocalMem().size());
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(1).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(1).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(1).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(1).getLabels().get(0));
				
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("08.Nov.2014\n" + "finish assignment #important\n");
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testUpdateDatetime() {
		init();
		
		taskLogic.executeCommand("ad last meeting for CS2103T 04.11.2014 from 2pm to 5pm #important");
		FeedbackObject feedback = taskLogic.executeCommand("up last meeting for CS2103T - 08.11.2014 from 4pm to 6pm");
		
		newTask = createDateTimeTask("last meeting for CS2103T", "08.11.2014 16:00", "08.11.2014 18:00"); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'last meeting for CS2103T' updated");
		
		//checking whether task is updated correctly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("08.Nov.2014\n" + "last meeting for CS2103T from 16:00 to 18:00 #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	} 
	
	@Test
	public void testUndoUpdateDateTime() {
		init();
		
		taskLogic.executeCommand("ad last meeting for CS2103T 04.11.2014 from 2pm to 5pm #important");
		taskLogic.executeCommand("up last meeting for CS2103T - 08.11.2014 from 4pm to 6pm");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTimeTask("last meeting for CS2103T", "04.11.2014 14:00", "04.11.2014 17:00"); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "last meeting for CS2103T reverted");
		
		//checking whether task is reverted correctly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("04.Nov.2014\n" + "last meeting for CS2103T from 14:00 to 17:00 #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testRedoUpdateDateTime() {
		init();
		
		taskLogic.executeCommand("ad last meeting for CS2103T 04.11.2014 from 2pm to 5pm #important");
		taskLogic.executeCommand("up last meeting for CS2103T - 08.11.2014 from 4pm to 6pm");
		taskLogic.executeCommand("un");
		FeedbackObject feedback = taskLogic.executeCommand("re");
		
		newTask = createDateTimeTask("last meeting for CS2103T", "08.11.2014 16:00", "08.11.2014 18:00"); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'last meeting for CS2103T' updated");
		
		//checking whether task is updated correctly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("08.Nov.2014\n" + "last meeting for CS2103T from 16:00 to 18:00 #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testUpdateLabel() {
		init();
		
		taskLogic.executeCommand("ad end of exams 28.11.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up end of exams - #hiphiphooray");
		
		newTask = createDateTask("end of exams", "28.11.2014", null); 
		labels.add("hiphiphooray"); 	
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'end of exams' updated");
		
		//checking whether task is updated correctly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("28.Nov.2014\n" + "end of exams #hiphiphooray\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	} 
	
	@Test
	public void testUndoUpdateLabel() {
		init();
		
		taskLogic.executeCommand("ad end of exams 28.11.2014 #important");
		taskLogic.executeCommand("up end of exams - #hiphiphooray");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTask("end of exams", "28.11.2014", null); 
		labels.add("important"); 	
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "end of exams reverted");
		
		//checking whether task is reverted correctly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("28.Nov.2014\n" + "end of exams #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}	
	
	@Test
	public void testRedoUpdateLabel() {
		init();
		
		taskLogic.executeCommand("ad end of exams 28.11.2014 #important");
		taskLogic.executeCommand("up end of exams - #hiphiphooray");
		taskLogic.executeCommand("un");
		FeedbackObject feedback = taskLogic.executeCommand("re");
		
		newTask = createDateTask("end of exams", "28.11.2014", null); 
		labels.add("hiphiphooray"); 	
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'end of exams' updated");
		
		//checking whether task is updated correctly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("28.Nov.2014\n" + "end of exams #hiphiphooray\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	} 
	
	
	@Test
	public void testSort() {
		init();
		
		taskLogic.executeCommand("ad bio lab from 12pm to 2pm #cells #tutorial #today");
		taskLogic.executeCommand("ad CS2010 tut from 4pm to 5pm #tutorial");
		taskLogic.executeCommand("ad CS2106 tut from 5pm to 6pm #tutorial #today");
		taskLogic.executeCommand("ad CS2103T tut from 9am to 10am #important #tutorial #today");
		taskLogic.executeCommand("ad collect welfare pack from 12pm to 2pm #today");
		
		FeedbackObject feedback = taskLogic.executeCommand("so tutorial");
		FeedbackObject feedbackTwo = taskLogic.executeCommand("so today tutorial");
				
		//checking for feedback to user - sorting by labels
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "tutorial sorted");
		assertEquals(feedbackTwo.getValidity(), true);
		assertEquals(feedbackTwo.getFeedback(), "today tutorial sorted");
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("#today#tutorial\n" + "bio lab from 12pm to 2pm #cells\n" + "CS2106 tut from 5pm to 6pm\n" + "CS2103T tut from 9am to 10am #important\n");
		toCheck.add("#today\n" + "collect welfare pack from 12pm to 2pm\n");
		toCheck.add("#tutorial\n" + "CS2010 tut from 4pm to 5pm\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
		taskLogic.executeCommand("so date");
	}
	
	@Test
	public void testUndoSort() {
		init();
		
		taskLogic.executeCommand("ad bio lab from 12pm to 2pm #cells #tutorial #today");
		taskLogic.executeCommand("ad CS2010 tut from 4pm to 5pm #tutorial");
		taskLogic.executeCommand("ad CS2106 tut from 5pm to 6pm #tutorial #today");
		taskLogic.executeCommand("ad CS2103T tut from 9am to 10am #important #tutorial #today");
		taskLogic.executeCommand("ad collect welfare pack from 12pm to 2pm #today");
		taskLogic.executeCommand("so tutorial");
		FeedbackObject feedback = taskLogic.executeCommand("un");
				
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "date sorted");
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("N.A.\n" + "bio lab from 12pm to 2pm #cells #tutorial #today\n" + "CS2010 tut from 4pm to 5pm #tutorial\n" + "CS2106 tut from 5pm to 6pm #tutorial #today\n" + "CS2103T tut from 9am to 10am #important #tutorial #today\n" + "collect welfare pack from 12pm to 2pm #today\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
		taskLogic.executeCommand("so date");
	}
	
	@Test
	public void testRedoSort() {
		init();
		
		taskLogic.executeCommand("ad bio lab from 12pm to 2pm #cells #tutorial #today");
		taskLogic.executeCommand("ad CS2010 tut from 4pm to 5pm #tutorial");
		taskLogic.executeCommand("ad CS2106 tut from 5pm to 6pm #tutorial #today");
		taskLogic.executeCommand("ad CS2103T tut from 9am to 10am #important #tutorial #today");
		taskLogic.executeCommand("ad collect welfare pack from 12pm to 2pm #today");
		taskLogic.executeCommand("so today tutorial");
		taskLogic.executeCommand("un");
		FeedbackObject feedback = taskLogic.executeCommand("re");
				
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "today tutorial sorted");
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("#today#tutorial\n" + "bio lab from 12pm to 2pm #cells\n" + "CS2106 tut from 5pm to 6pm\n" + "CS2103T tut from 9am to 10am #important\n");
		toCheck.add("#today\n" + "collect welfare pack from 12pm to 2pm\n");
		toCheck.add("#tutorial\n" + "CS2010 tut from 4pm to 5pm\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
		taskLogic.executeCommand("so date");
	}
	
	
	@Test
	public void testSearch() {
		mLocalMem.clearMemory();
		
		taskLogic.executeCommand("ad bio exam 22.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2010 exam 24.11.2014 from 5pm to 7pm #exam");
		taskLogic.executeCommand("ad CS2103T exam 26.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2106 exam 28.11.2014 #important #exams");
		taskLogic.executeCommand("ad christmas celebration 25.12.2014 #xmas");
		taskLogic.executeCommand("ad buy christmas presents 25.12.2014 #xmas");
		taskLogic.executeCommand("ad buy christmas groceries 25.12.2014 #xmas");
		
		FeedbackObject feedback = taskLogic.executeCommand("se exam");
		FeedbackObject feedbackTwo = taskLogic.executeCommand("se christmas");
				
		//checking for feedback to user - searching for labels 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "1. bio exam from 22.11.2014 13:00 to 22.11.2014 15:00 #exam" + "\n" + "2. CS2010 exam from 24.11.2014 17:00 to 24.11.2014 19:00 #exam" + "\n" + "3. CS2103T exam from 26.11.2014 13:00 to 26.11.2014 15:00 #exam" + "\n" + "4. CS2106 exam on 28.11.2014 #important #exams" + "\n" + "task(s) with keyword 'exam' searched");
	
		//checking for feedback to user - searching for words in task descriptions 
		assertEquals(feedbackTwo.getValidity(), true);
		assertEquals(feedbackTwo.getFeedback(), "1. christmas celebration on 25.12.2014 #xmas" + "\n" + "2. buy christmas presents on 25.12.2014 #xmas" + "\n" + "3. buy christmas groceries on 25.12.2014 #xmas" + "\n" + "task(s) with keyword 'christmas' searched");
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("22.Nov.2014\n" + "bio exam from 13:00 to 15:00 #exam\n");
		toCheck.add("24.Nov.2014\n" + "CS2010 exam from 17:00 to 19:00 #exam\n");
		toCheck.add("26.Nov.2014\n" + "CS2103T exam from 13:00 to 15:00 #exam\n");
		toCheck.add("28.Nov.2014\n" + "CS2106 exam #important #exams\n");
		toCheck.add("25.Dec.2014\n" + "christmas celebration #xmas\n" + "buy christmas presents #xmas\n" + "buy christmas groceries #xmas\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testSearchDelete() {
		mLocalMem.clearMemory();
		
		taskLogic.executeCommand("ad bio exam 22.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2010 exam 24.11.2014 from 5pm to 7pm #exam");
		taskLogic.executeCommand("ad CS2103T exam 26.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2106 exam 28.11.2014 #important #exams");
		taskLogic.executeCommand("ad christmas celebration 25.12.2014 #xmas");
		taskLogic.executeCommand("ad buy christmas presents 25.12.2014 #xmas");
		taskLogic.executeCommand("ad buy christmas groceries 25.12.2014 #xmas");
		taskLogic.executeCommand("se exam");
		FeedbackObject feedback = taskLogic.executeCommand("de 3");
		
		//checking for feedback to user - searching for words in task descriptions 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'CS2103T exam' deleted");
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("22.Nov.2014\n" + "bio exam from 13:00 to 15:00 #exam\n");
		toCheck.add("24.Nov.2014\n" + "CS2010 exam from 17:00 to 19:00 #exam\n");
		toCheck.add("28.Nov.2014\n" + "CS2106 exam #important #exams\n");
		toCheck.add("25.Dec.2014\n" + "christmas celebration #xmas\n" + "buy christmas presents #xmas\n" + "buy christmas groceries #xmas\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testSearchUpdate() {
		mLocalMem.clearMemory();
		
		taskLogic.executeCommand("ad bio exam 22.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2010 exam 24.11.2014 from 5pm to 7pm #exam");
		taskLogic.executeCommand("ad CS2103T exam 26.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2106 exam 28.11.2014 #important #exams");
		taskLogic.executeCommand("ad christmas celebration 25.12.2014 #xmas");
		taskLogic.executeCommand("ad buy christmas presents 25.12.2014 #xmas");
		taskLogic.executeCommand("ad buy christmas groceries 25.12.2014 #xmas");
		taskLogic.executeCommand("se christmas");
		FeedbackObject feedback = taskLogic.executeCommand("up 1 - christmas celebration at aunt's house");
		
		//checking for feedback to user - searching for words in task descriptions 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'christmas celebration at aunt's house' updated");
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("22.Nov.2014\n" + "bio exam from 13:00 to 15:00 #exam\n");
		toCheck.add("24.Nov.2014\n" + "CS2010 exam from 17:00 to 19:00 #exam\n");
		toCheck.add("26.Nov.2014\n" + "CS2103T exam from 13:00 to 15:00 #exam\n");
		toCheck.add("28.Nov.2014\n" + "CS2106 exam #important #exams\n");
		toCheck.add("25.Dec.2014\n" + "buy christmas presents #xmas\n" + "buy christmas groceries #xmas\n" + "christmas celebration at aunt's house #xmas\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	
	@Test
	public void testDone() {
		init();
		
		taskLogic.executeCommand("ad bio exam 22.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2010 exam 24.11.2014 from 5pm to 7pm #exam");
		taskLogic.executeCommand("ad buy stationeries 10.11.2014 #forexam");
		
		FeedbackObject feedback = taskLogic.executeCommand("do buy stationeries");
		newTask = createDateTask("buy stationeries", "10.11.2014", null); 
		labels.add("forexam"); 
		
		//checking for feedback to user  
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'buy stationeries' mark as done");
		
		//checking if task is still in local memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(2).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(2).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(2).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(2).getLabels().get(0));
		
		//checking if task is hidden from user
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("22.Nov.2014\n" + "bio exam from 13:00 to 15:00 #exam\n");
		toCheck.add("24.Nov.2014\n" + "CS2010 exam from 17:00 to 19:00 #exam\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	
	@Test
	public void testUndoDone() {
		init();
		
		taskLogic.executeCommand("ad bio exam 22.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2010 exam 24.11.2014 from 5pm to 7pm #exam");
		taskLogic.executeCommand("ad buy stationeries 10.11.2014 #forexam");
		taskLogic.executeCommand("do buy stationeries");
		
		FeedbackObject feedback = taskLogic.executeCommand("un");			
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "buy stationeries undone");		
		
		//checking if task is shown to user
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("10.Nov.2014\n" + "buy stationeries #forexam\n");
		toCheck.add("22.Nov.2014\n" + "bio exam from 13:00 to 15:00 #exam\n");
		toCheck.add("24.Nov.2014\n" + "CS2010 exam from 17:00 to 19:00 #exam\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	
	@Test
	public void testRedoDone() {
		init();
		
		taskLogic.executeCommand("ad bio exam 22.11.2014 from 1pm to 3pm #exam");
		taskLogic.executeCommand("ad CS2010 exam 24.11.2014 from 5pm to 7pm #exam");
		taskLogic.executeCommand("ad buy stationeries 10.11.2014 #forexam");
		taskLogic.executeCommand("do buy stationeries");
		taskLogic.executeCommand("un");
		
		FeedbackObject feedback = taskLogic.executeCommand("re");
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "'buy stationeries' mark as done");
		
		//checking if task is hidden from user
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("22.Nov.2014\n" + "bio exam from 13:00 to 15:00 #exam\n");
		toCheck.add("24.Nov.2014\n" + "CS2010 exam from 17:00 to 19:00 #exam\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testHideLabels() {
		init();
		
		taskLogic.executeCommand("ad bio lab from 12pm to 2pm #cells #tutorial #today");
		taskLogic.executeCommand("ad CS2010 tut from 4pm to 5pm #tutorial");
		taskLogic.executeCommand("ad CS2106 tut from 5pm to 6pm #tutorial #today");
		taskLogic.executeCommand("ad CS2103T tut from 9am to 10am #important #tutorial #today");
		taskLogic.executeCommand("ad collect welfare pack from 12pm to 2pm #today");
		taskLogic.executeCommand("so tutorial");
		FeedbackObject feedback = taskLogic.executeCommand("hi tutorial");
		
		//checking for feedback to users 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "Labels hidden");
		
		//checking for boolean value from parser
		assertEquals(true, taskLogic.labelsHidden);
		
		//checking for correct labels to return to GUI 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("#tutorial");
		assertEquals(toCheck, taskLogic.labelsToHide());
		taskLogic.executeCommand("so date");
	}
	
	@Test
	public void testShowLabels() {
		init();
		
		taskLogic.executeCommand("ad bio lab from 12pm to 2pm #cells #tutorial #today");
		taskLogic.executeCommand("ad CS2010 tut from 4pm to 5pm #tutorial");
		taskLogic.executeCommand("ad CS2106 tut from 5pm to 6pm #tutorial #today");
		taskLogic.executeCommand("ad CS2103T tut from 9am to 10am #important #tutorial #today");
		taskLogic.executeCommand("ad collect welfare pack from 12pm to 2pm #today");
		taskLogic.executeCommand("so today tutorial");
		taskLogic.executeCommand("hi tutorial");
		FeedbackObject feedback = taskLogic.executeCommand("sh tutorial");
		
		//checking for boolean value from parser
		assertEquals(false, taskLogic.labelsHidden);
				
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), true);
		assertEquals(feedback.getFeedback(), "All labels shown");
		taskLogic.executeCommand("so date");
	}
}