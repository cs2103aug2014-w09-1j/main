package mytasks.logic;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;
import mytasks.file.Task;

public class LocalMemoryTest {
	
	private MyTasksLogic taskLogic = new MyTasksLogic(true);
	private ArrayList<String> labels = new ArrayList<String>();
	private Task newTask;
	
	@Test
	public void testAddLocalMemory() {
		taskLogic.executeCommand("add meeting 22.02.2014 12:23 #important");
		
		labels.add("important"); 
		Date newDate = null;
		try {
			newDate = mytasks.parser.MyTasksParser.dateTimeFormat.parse("22.02.2014 12:23");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting", newDate, labels); 
		
		assertEquals(newTask.getDescription(), taskLogic.mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getDateTime(), taskLogic.mLocalMem.getLocalMem().get(0).getDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
}
