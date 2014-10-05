package mytasks.parser;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.CommandInfo;
import mytasks.file.Task;

import org.junit.Test;

public class ParserTest {
	
	private CommandInfo test1 = null;
	private CommandInfo test2 = null;
	private CommandInfo test3 = null;
	private CommandInfo test4 = null;
	private CommandInfo test5 = null;
	private CommandInfo test6 = null;
	private CommandInfo test7 = null;
	private CommandInfo test8 = null;
	private CommandInfo test9 = null;
	private CommandInfo test10 = null;
	private CommandInfo test11 = null;
	private CommandInfo test12 = null;
	MyTasksParser tester = new MyTasksParser();

	@Test
	public void addTest() {
		initTestObjects();
		assertObjFields(test1, tester.parseInput("add dinner"));
		assertObjFields(test2, tester.parseInput("add dinner 18.09.2014"));
		assertObjFields(test3, tester.parseInput("add submit assignment 20.09.2014 12:00"));
		assertObjFields(test4, tester.parseInput("add do homework 19.09.2014 #cs2103"));
		assertObjFields(test5, tester.parseInput("add do homework 19.09.2014 #cs2103 #urgent #gg"));
		//assertObjFields(test12, tester.parseInput("add code for project 06.10.2014 from 12:00 to 14:00"));
	}
	
	@Test
	public void deleteTest() {
		initTestObjects();
		assertObjFields(test7, tester.parseInput("delete CS2103 meeting"));
	}
	
	@Test
	public void updateTest() {
		initTestObjects();
		assertObjFields(test6, tester.parseInput("add have fun! #notpossible 18.09.2014"));
		assertObjFields(test8, tester.parseInput("update meeting - CS2103 meeting"));
		assertObjFields(test9, tester.parseInput("update meeting cs2103 - 20.09.2014"));
		assertObjFields(test10, tester.parseInput("update meeting - #CS2103"));
		assertObjFields(test11, tester.parseInput("update meeting - play 19.09.2014 #yolo"));
	}
	
	private void initTestObjects() {
		try {
			test1 = new CommandInfo("add", "dinner", null, null, null, null);
			
			Date date2 = MyTasksParser.dateFormat.parse("18.09.2014");
			test2 = new CommandInfo("add", "dinner", date2, null, null, null);
			
			Date date3 = MyTasksParser.dateTimeFormat.parse("20.09.2014 12:00");
			test3 = new CommandInfo("add", "submit assignment", date3, null, null, null);
			
			ArrayList<String> list4 = new ArrayList<String>();
			list4.add("cs2103");
			Date date4 = MyTasksParser.dateFormat.parse("19.09.2014");
			test4 = new CommandInfo("add", "do homework", date4, null, list4, null);
			
			ArrayList<String> list5 = new ArrayList<String>();
			list5.add("cs2103");
			list5.add("urgent");
			list5.add("gg");
			Date date5 = MyTasksParser.dateFormat.parse("19.09.2014");
			test5 = new CommandInfo("add", "do homework", date5, null ,list5, null);
			
			ArrayList<String> list6 = new ArrayList<String>();
			list6.add("notpossible");
			Date date6 = MyTasksParser.dateFormat.parse("18.09.2014");
			test6 = new CommandInfo("add", "have fun!", date6, null, list6, null);
			
			test7 = new CommandInfo("delete", "CS2103 meeting", null, null, null, null);
			
			test8 = new CommandInfo("update", "CS2103 meeting", null, null, null, "meeting");
			
			Date date9 = MyTasksParser.dateFormat.parse("20.09.2014");
			test9 = new CommandInfo("update", null, date9, null, null, "meeting cs2103");
			
			ArrayList<String> list10 = new ArrayList<String>();
			list10.add("CS2103");
			test10 = new CommandInfo("update", null, null, null, list10, "meeting");
			
			ArrayList<String> list11 = new ArrayList<String>();
			list11.add("yolo");
			Date date11 = MyTasksParser.dateFormat.parse("19.09.2014");
			test11 = new CommandInfo("update", "play", date11, null, list11, "meeting");
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void assertObjFields(CommandInfo testCase, CommandInfo result){
		
		Task testCaseTask = testCase.getTask();
		Task resultTask = result.getTask();
		//Assert type
		assertEquals(testCase.getType(), result.getType());
		
		
		//Assert task description
		if (testCaseTask.getDescription()==null) {
			assertEquals(null, resultTask.getDescription());
		} else {
			assertEquals(testCaseTask.getDescription(), resultTask.getDescription());
			//System.out.println(testCaseTask.getDescription());
			//System.out.println(resultTask.getDescription());
		}
		
		//Assert fromDateTime
		if (testCaseTask.getFromDateTime()==null) {
			assertEquals(null, resultTask.getFromDateTime());
		} else {
			//System.out.println(resultTask.getFromDateTime().toString());
			//System.out.println(testCaseTask.getFromDateTime().toString());
			assertEquals(testCaseTask.getFromDateTime(), resultTask.getFromDateTime());
		}
		
		//Assert toDateTime
		if (testCaseTask.getToDateTime()==null) {
			assertEquals(null, resultTask.getToDateTime());
		} else {
			//System.out.println(resultTask.getToDateTime().toString());
			//System.out.println(testCaseTask.getToDateTime().toString());
			assertEquals(testCaseTask.getToDateTime(), resultTask.getToDateTime());	
		}		
		
		//Assert labels
		if (testCaseTask.getLabels()==null) {
			assertEquals(null,resultTask.getLabels());
		} else {
			assertEquals(testCaseTask.getLabels().size(),resultTask.getLabels().size());
			for (int i = 0; i<testCaseTask.getLabels().size(); i++){
				//System.out.println(resultTask.getLabels().get(i));
				assertEquals(testCaseTask.getLabels().get(i),resultTask.getLabels().get(i));
			}
		}
		
		//Assert update task's description
		assertEquals(testCase.getToUpdateTaskDesc(),result.getToUpdateTaskDesc());
		//System.out.println(testCase.getUpdateDesc());
		//System.out.println(result.getUpdateDesc());
	}

}
