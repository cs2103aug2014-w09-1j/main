package mytasks.parser;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import mytasks.file.CommandInfo;
import mytasks.file.Task;

import org.junit.Test;

public class ParserTest {
	
	private CommandInfo test1 = new CommandInfo("add","dinner",null,null);
	private CommandInfo test2 = null;
	private CommandInfo test3 = null;

	@Test
	public void parseInputTest() {
		MyTasksParser tester = new MyTasksParser();
		initTestObjects();
		assertObjFields(test1, tester.parseInput("add dinner"));
		assertObjFields(test2, tester.parseInput("add dinner 18.09.2014"));
		assertObjFields(test3, tester.parseInput("add submit assignment 20.09.2014 12:00"));
	}
	
	private void initTestObjects() {
		try {
			Date date2 = MyTasksParser.dateFormat.parse("18.09.2014");
			test2 = new CommandInfo("add","dinner",date2,null);
			Date date3 = MyTasksParser.dateTimeFormat.parse("20.09.2014 12:00");
			test3  = new CommandInfo("add","submit assignment",date3,null);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void assertObjFields(CommandInfo testCase, CommandInfo result){
		assertEquals(testCase.getType(), result.getType());
		Task testCaseTask = testCase.getTask();
		Task resultTask = result.getTask();
		assertEquals(testCaseTask.getDescription(),resultTask.getDescription());
		System.out.println(testCaseTask.getDescription());
		System.out.println(resultTask.getDescription());
		if (testCaseTask.getDateTime()==null) {
			assertEquals(null,resultTask.getDateTime());
		} else {
			System.out.println(resultTask.getDateTime().toString());
			System.out.println(testCaseTask.getDateTime().toString());
			assertEquals(resultTask.getDateTime().toString(), testCaseTask.getDateTime().toString());
			
		}
		//labels may need fixing later on as it is an array
		assertEquals(testCaseTask.getLabels(), resultTask.getLabels());
	}

}
