package mytasks.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

//@author A0115034X 
public class IntegrationTesting {
	
	private MyTasksLogicController taskLogic = MyTasksLogicController.getInstance(true);
	private LocalMemory mLocalMem = LocalMemory.getInstance(); 
	private ArrayList<String> labels = new ArrayList<String>();
	private Task newTask;
	private boolean isTrue = false; 
	
	private void init() {
		mLocalMem.clearMemory();
		isTrue = true; 
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
	public void testAddAllLocalMemory() {		
		init();
		
		FeedbackObject feedback = taskLogic.executeCommand("ad meeting 01.10.2014 from 12:23 to 20:30 #important");		
		newTask = createDateTimeTask("meeting", "01.10.2014 12:23", "01.10.2014 20:30"); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "meeting added");
		
		//checking whether task is added correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
	}

	@Test
	public void testDeleteLocalMemory() {
		init();
		
		FeedbackObject feedback = taskLogic.executeCommand("ad meeting two 03.10.2014 #wecandoit");
		taskLogic.executeCommand("ad meeting 02.10.2014 12:23 #important");
		taskLogic.executeCommand("de meeting");
		
		newTask = createDateTask("meeting two", "03.10.2014", null); 
		labels.add("wecandoit"); 

		//checking for feedback to user 
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "meeting two added");
		//checking whether task is added correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateAllLocalMemory() {
		init();
		
		taskLogic.executeCommand("ad meeting 04.10.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up meeting - meeting for CS2103T 05.10.2014 #letsdothis");	
		
		newTask = createDateTask("meeting for CS2103T", "05.10.2014", null); 
		labels.add("letsdothis"); 

		//checking for feedback to user 
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "'meeting' updated");
		//checking whether task is added correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateTaskLocalMemory() {
		init();
		
		taskLogic.executeCommand("ad meeting for 2103 04.11.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up meeting for 2103 - documentation meeting");
				
		newTask = createDateTask("documentation meeting", "04.11.2014", null); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "'meeting for 2103' updated");
		//checking whether task is added correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateDatetimeLocalMemory() {
		init();
		
		taskLogic.executeCommand("ad last meeting for CS2103T 04.11.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up last meeting for CS2103T - 08.11.2014");
		
		newTask = createDateTask("last meeting for CS2103T", "08.11.2014", null); 
		labels.add("important"); 
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "'last meeting for CS2103T' updated");
		//checking whether task is added correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateLabelsLocalMemory() {
		init();
		
		taskLogic.executeCommand("ad end of exams 28.11.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up end of exams - #hiphiphooray");
		
		newTask = createDateTask("end of exams", "28.11.2014", null); 
		labels.add("hiphiphooray"); 	
		
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "'end of exams' updated");
		//checking whether task is added correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testSearch() {
		mLocalMem.clearMemory();
		isTrue = true; 
		
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
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "1. bio exam from 22.11.2014 13:00 to 22.11.2014 15:00 #exam" + "\n" + "2. CS2010 exam from 24.11.2014 17:00 to 24.11.2014 19:00 #exam" + "\n" + "3. CS2103T exam from 26.11.2014 13:00 to 26.11.2014 15:00 #exam" + "\n" + "4. CS2106 exam on 28.11.2014 #important #exams" + "\n" + "task(s) with keyword 'exam' searched");
		//checking for feedback to user - searching for words in task descriptions 
		assertEquals(feedbackTwo.getValidity(), isTrue);
		assertEquals(feedbackTwo.getFeedback(), "1. christmas celebration on 25.12.2014 #xmas" + "\n" + "2. buy christmas presents on 25.12.2014 #xmas" + "\n" + "3. buy christmas groceries on 25.12.2014 #xmas" + "\n" + "task(s) with keyword 'christmas' searched");
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
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "tutorial sorted");
		assertEquals(feedbackTwo.getValidity(), isTrue);
		assertEquals(feedbackTwo.getFeedback(), "today tutorial sorted");
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("#today#tutorial\n" + "bio lab from 12pm to 2pm #cells\n" + "CS2106 tut from 5pm to 6pm\n" + "CS2103T tut from 9am to 10am #important\n");
		toCheck.add("#today\n" + "collect welfare pack from 12pm to 2pm\n");
		toCheck.add("#tutorial\n" + "CS2010 tut from 4pm to 5pm\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
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
		
		//checking for feedback to user - sorting by labels
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "complete assignment deleted");
		
		//checking whether task is undo-ed properly in memory 
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
	public void testUndoDelete() {
		init();
		
		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete homework 15.11.2014 #finals");
		taskLogic.executeCommand("de complete homework");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTask("complete homework", "15.11.2014", null); 
		labels.add("finals");
		
		//checking for feedback to user - sorting by labels
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "complete homework added");
		
		//checking whether task is undo-ed properly in memory 
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
	public void testUndoUpdateTaskDesc() {
		init();

		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad complete assignment homework 08.11.2014 #important");
		taskLogic.executeCommand("up complete assignment homework - finish assignment");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTask("complete assignment homework", "08.11.2014", null); 
		labels.add("important");

		//checking for feedback to user - sorting by labels
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "finish assignment reverted");
		
		//checking whether task is undo-ed properly in memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(1).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(1).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(1).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(1).getLabels().get(0));
				
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("08.Nov.2014\n" + "complete assignment homework #important\n");
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testUndoUpdateDateTime() {
		init();

		taskLogic.executeCommand("ad do homework #tomorrow #test");
		taskLogic.executeCommand("ad finish math homework 07.11.2014 #lasttutorial");
		taskLogic.executeCommand("up finish math homework - tomorrow");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTask("finish math homework", "07.11.2014", null); 
		labels.add("lasttutorial");

		//checking for feedback to user - sorting by labels
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "finish math homework reverted");
		
		//checking whether task is updated correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(1).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(1).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(1).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(1).getLabels().get(0));
				
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("07.Nov.2014\n" + "finish math homework #lasttutorial\n");
		toCheck.add("N.A.\n" + "do homework #tomorrow #test\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}
	
	@Test
	public void testUndoUpdateLabel() {
		init();
		
		taskLogic.executeCommand("ad finish math homework 07.11.2014 #important");
		taskLogic.executeCommand("ad meet prof tmr #important");
		taskLogic.executeCommand("up meet prof tmr - #over");
		FeedbackObject feedback = taskLogic.executeCommand("un");
		
		newTask = createDateTask("finish math homework", "07.11.2014", null); 
		labels.add("important");
		
		//checking for feedback to user - sorting by labels
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "meet prof tmr reverted");
		
		//checking whether task is updated correctly into memory 
		assertEquals(newTask.getDescription(), mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), mLocalMem.getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), mLocalMem.getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), mLocalMem.getLocalMem().get(0).getLabels().get(0));
		
		//checking for output to user 
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("07.Nov.2014\n" + "finish math homework #important\n");
		toCheck.add("N.A.\n" + "meet prof tmr #important\n");
		assertEquals(toCheck, taskLogic.obtainPrintableOutput());
	}	
}
