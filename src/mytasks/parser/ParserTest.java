package mytasks.parser;

import static org.junit.Assert.*;
import mytasks.file.CommandInfo;
import mytasks.file.Task;

import org.junit.Test;

public class ParserTest {
	
	private CommandInfo test1 = new CommandInfo("add",null,null,null);

	@Test
	public void parseInputTest() {
		MyTasksParser tester = new MyTasksParser();
		assertObjFields(test1, tester.parseInput("add dinner"));
	}
	
	private void assertObjFields(CommandInfo testCase, CommandInfo result){
		assertEquals(testCase.getType(), result.getType());
		Task testCaseTask = testCase.getTask();
		Task resultTask = result.getTask();
		assertEquals(testCaseTask.getDescription(),resultTask.getDescription());
		assertEquals(testCaseTask.getDateTime(), resultTask.getDateTime());
		//labels may need fixing later on as it is an array
		assertEquals(testCaseTask.getLabels(), resultTask.getLabels());
	}

}
