package mytasks.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;

import mytasks.file.FeedbackObject;
import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

public class IntegrationTesting {
	
	private MyTasksLogicController taskLogic = MyTasksLogicController.getInstance(true);
	private LocalMemory mLocalMem = LocalMemory.getInstance(); 
	private ArrayList<String> labels = new ArrayList<String>();
	private Task newTask;
	private boolean isTrue = false; 
	
	@Test
	public void testAddAllLocalMemory() {		
		mLocalMem.clearMemory();
		isTrue = true;
		FeedbackObject feedback = taskLogic.executeCommand("ad meeting 01.10.2014 from 12:23 to 20:30 #important");
		
		labels.add("important"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateTimeFormats.get(0).parse("01.10.2014 12:23");
			date2 = MyTasksParser.dateTimeFormats.get(0).parse("01.10.2014 20:30");
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		newTask = new Task("meeting", date1, date2, labels); 
		
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
		mLocalMem.clearMemory();
		
		isTrue = true;
		FeedbackObject feedback = taskLogic.executeCommand("ad meeting two 03.10.2014 #wecandoit");
		taskLogic.executeCommand("ad meeting 02.10.2014 12:23 #important");
		taskLogic.executeCommand("de meeting");

		labels.add("wecandoit"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("03.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		newTask = new Task("meeting two", date1, date2, labels);
		
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
		mLocalMem.clearMemory();
		labels = new ArrayList<String>();
		isTrue = true;
		taskLogic.executeCommand("ad meeting 04.10.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up meeting - meeting two 05.10.2014 #letsdothis");
		
		labels.add("letsdothis"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("05.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting two", date1, date2, labels); 
		
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
		mLocalMem.clearMemory();
		labels = new ArrayList<String>();
		isTrue = true;
		
		taskLogic.executeCommand("ad meeting for 2103 04.10.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up meeting for 2103 - documentation meeting");
		
		labels.add("important"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("04.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("documentation meeting", date1, date2, labels); 

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
		mLocalMem.clearMemory();
		labels = new ArrayList<String>();
		isTrue = true; 
		taskLogic.executeCommand("ad meeting 04.10.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up meeting - 20.10.2014");
		
		labels.add("important"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("20.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting", date1, date2, labels); 
		
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
	public void testUpdateLabelsLocalMemory() {
		mLocalMem.clearMemory();
		isTrue = true; 
		labels = new ArrayList<String>();
		
		taskLogic.executeCommand("ad end of exams 28.11.2014 #important");
		FeedbackObject feedback = taskLogic.executeCommand("up end of exams - #hiphiphooray");
		
		labels.add("hiphiphooray"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("28.11.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("end of exams", date1, date2, labels); 
		
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
	public void testSearchForLabels() {
		mLocalMem.clearMemory();
		isTrue = true; 
		labels = new ArrayList<String>();
		
		taskLogic.executeCommand("ad end of exams 28.11.2014 #important #exams");
		taskLogic.executeCommand("ad christmas celebration 25.12.2014 #christmas #xmas");
		
		FeedbackObject feedback = taskLogic.executeCommand("se important");
		FeedbackObject feedbackTwo = taskLogic.executeCommand("se christmas");
				
		//checking for feedback to user 
		assertEquals(feedback.getValidity(), isTrue);
		assertEquals(feedback.getFeedback(), "1. end of exams on 28.11.2014 #important #exams\ntask(s) with keyword 'important' searched");
	}
}
