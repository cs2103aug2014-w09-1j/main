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
		taskLogic.executeCommand("add meeting 01.10.2014 12:23 #important");
		
		labels.add("important"); 
		Date newDate = null;
		try {
			newDate = mytasks.parser.MyTasksParser.dateTimeFormat.parse("01.10.2014 12:23");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting", newDate, labels); 
		
		assertEquals(newTask.getDescription(), taskLogic.mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getDateTime(), taskLogic.mLocalMem.getLocalMem().get(0).getDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testDeleteLocalMemory() {
		taskLogic.executeCommand("add meeting 02.10.2014 12:23 #important");
		taskLogic.executeCommand("add meeting two 03.10.2014 #wecandoit");
		taskLogic.executeCommand("delete meeting");

		labels.add("wecandoit"); 
		Date date = null;
		try {
			date = mytasks.parser.MyTasksParser.dateFormat.parse("03.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(date.toString());
		newTask = new Task("meeting two", date, labels);
		
		assertEquals(newTask.getDescription(), taskLogic.mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getDateTime(), taskLogic.mLocalMem.getLocalMem().get(0).getDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateLocalMemory() {
		taskLogic.executeCommand("add meeting 04.10.2014 #important");
		taskLogic.executeCommand("update meeting - meeting two 05.10.2014 #letsdothis");
		
		labels.add("letsdothis"); 
		Date newDate = null;
		try {
			newDate = mytasks.parser.MyTasksParser.dateFormat.parse("05.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting two", newDate, labels); 

		assertEquals(newTask.getDescription(), taskLogic.mLocalMem.getLocalMem().get(0).getDescription());
		assertEquals(newTask.getDateTime(), taskLogic.mLocalMem.getLocalMem().get(0).getDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.mLocalMem.getLocalMem().get(0).getLabels().get(0));
	} 
}
