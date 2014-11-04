package mytasks.logic;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.util.ArrayList;

import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

public class LocalMemoryTest {
	
	private MyTasksLogic taskLogic = MyTasksLogic.getInstance(true);
	private ArrayList<String> labels = new ArrayList<String>();
	private Task newTask;
	
	@Test
	public void testAddLocalMemory() {
		taskLogic.executeCommand("add meeting 01.10.2014 from 12:23 to 20:30 #important");
		
		labels.add("important"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(1).parse("01.10.2014 12:23");
			date2 = MyTasksParser.dateFormats.get(1).parse("01.10.2014 20:30");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting", date1, date2, labels); 
		
		assertEquals(newTask.getDescription(), taskLogic.getMemory().getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), taskLogic.getMemory().getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testDeleteLocalMemory() {
		taskLogic.executeCommand("add meeting 02.10.2014 12:23 #important");
		taskLogic.executeCommand("add meeting two 03.10.2014 #wecandoit");
		taskLogic.executeCommand("delete meeting");

		labels.add("wecandoit"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("03.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(date1.toString());
		newTask = new Task("meeting two", date1, date2, labels);
		
		assertEquals(newTask.getDescription(), taskLogic.getMemory().getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), taskLogic.getMemory().getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateAllLocalMemory() {
		labels = new ArrayList<String>();
		
		taskLogic.executeCommand("add meeting 04.10.2014 #important");
		taskLogic.executeCommand("update meeting - meeting two 05.10.2014 #letsdothis");
		
		labels.add("letsdothis"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("05.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting two", date1, date2, labels); 

		assertEquals(newTask.getDescription(), taskLogic.getMemory().getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), taskLogic.getMemory().getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateTaskLocalMemory() {
		labels = new ArrayList<String>();
		
		taskLogic.executeCommand("add meeting 04.10.2014 #important");
		taskLogic.executeCommand("update meeting - meeting two");
		
		labels.add("important"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("04.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting two", date1, date2, labels); 

		assertEquals(newTask.getDescription(), taskLogic.getMemory().getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), taskLogic.getMemory().getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateDatetimeLocalMemory() {
		labels = new ArrayList<String>();
		
		taskLogic.executeCommand("add meeting 04.10.2014 #important");
		taskLogic.executeCommand("update meeting - 20.10.2014");
		
		labels.add("important"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("20.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting", date1, date2, labels); 

		assertEquals(newTask.getDescription(), taskLogic.getMemory().getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), taskLogic.getMemory().getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
	} 
	
	@Test
	public void testUpdateLabelsLocalMemory() {
		labels = new ArrayList<String>();
		
		taskLogic.executeCommand("add meeting 04.10.2014 #important");
		taskLogic.executeCommand("update meeting - #hiphiphooray");
		
		labels.add("hiphiphooray"); 
		Date date1 = null, date2 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("04.10.2014");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		newTask = new Task("meeting", date1, date2, labels); 

		assertEquals(newTask.getDescription(), taskLogic.getMemory().getLocalMem().get(0).getDescription());
		assertEquals(newTask.getFromDateTime(), taskLogic.getMemory().getLocalMem().get(0).getFromDateTime());
		assertEquals(newTask.getToDateTime(), taskLogic.getMemory().getLocalMem().get(0).getToDateTime());
		assertEquals(newTask.getLabels().get(0), taskLogic.getMemory().getLocalMem().get(0).getLabels().get(0));
	} 
}
