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

	@Test
	public void parseInputTest() {
		MyTasksParser tester = new MyTasksParser();
		initTestObjects();
		assertObjFields(test1, tester.parseInput("add dinner"));
		assertObjFields(test2, tester.parseInput("add dinner 18.09.2014"));
		assertObjFields(test3, tester.parseInput("add submit assignment 20.09.2014 12:00"));
		assertObjFields(test4, tester.parseInput("add do homework 19.09.2014 #cs2103"));
		assertObjFields(test5, tester.parseInput("add do homework 19.09.2014 #cs2103 #urgent #gg"));
		assertObjFields(test6, tester.parseInput("add have fun! #notpossible 18.09.2014"));
	}
	
	private void initTestObjects() {
		try {
			test1 = new CommandInfo("add","dinner",null,null);
			
			Date date2 = MyTasksParser.dateFormat.parse("18.09.2014");
			test2 = new CommandInfo("add","dinner",date2,null);
			
			Date date3 = MyTasksParser.dateTimeFormat.parse("20.09.2014 12:00");
			test3 = new CommandInfo("add","submit assignment",date3,null);
			
			ArrayList<String> list4 = new ArrayList<String>();
			list4.add("cs2103");
			Date date4 = MyTasksParser.dateFormat.parse("19.09.2014");
			test4 = new CommandInfo("add","do homework",date4,list4);
			
			ArrayList<String> list5 = new ArrayList<String>();
			list5.add("cs2103");
			list5.add("urgent");
			list5.add("gg");
			Date date5 = MyTasksParser.dateFormat.parse("19.09.2014");
			test5 = new CommandInfo("add","do homework",date5,list5);
			
			ArrayList<String> list6 = new ArrayList<String>();
			list6.add("notpossible");
			Date date6 = MyTasksParser.dateFormat.parse("18.09.2014");
			test6 = new CommandInfo("add","have fun!",date6,list6);
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
		assertEquals(testCaseTask.getDescription(),resultTask.getDescription());
		//System.out.println(testCaseTask.getDescription());
		//System.out.println(resultTask.getDescription());
		
		//Assert dateTime
		if (testCaseTask.getDateTime()==null) {
			assertEquals(null,resultTask.getDateTime());
		} else {
			//System.out.println(resultTask.getDateTime().toString());
			//System.out.println(testCaseTask.getDateTime().toString());
			assertEquals(testCaseTask.getDateTime().toString(), resultTask.getDateTime().toString());
			
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
	}

}
