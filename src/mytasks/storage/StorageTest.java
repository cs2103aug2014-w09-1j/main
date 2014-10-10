package mytasks.storage;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import mytasks.file.Task;
import mytasks.parser.MyTasksParser;

import org.junit.Test;

class StorageTest {
	
	ArrayList<Task> mem1;
	String test1;
	MyTasksStorage tester = new MyTasksStorage();

	@Test
	public void convertToTasksTest() {
		initOutputArrays();
		assertEquals(mem1, tester.convertToTasks(test1));
	}
	
	@Test
	public void determineOutputTest() {
		initOutputArrays();
		assertEquals(test1,tester.determineOutput(mem1));
	}
	
	private void initOutputArrays() {
		try {
			mem1 = new ArrayList<Task>();
			Date d1 = MyTasksParser.dateFormats.get(0).parse("06.10.2014");
			Date d2 = MyTasksParser.dateFormats.get(1).parse("06.10.2014 14:00");
			Date d3 = MyTasksParser.dateFormats.get(1).parse("06.10.2014 15:00");
			Date d4 = MyTasksParser.dateFormats.get(1).parse("07.10.2014 15:00");
			ArrayList<String> l1 = new ArrayList<String>();
			l1.add("yolo");
			l1.add("gg");
			Task t1 = new Task("do homework", d1, null, null);
			Task t2 = new Task("read book", d2, d3, null);
			Task t3 = new Task("relax", d3, d4, l1);
			mem1.add(t1);
			mem1.add(t2);
			mem1.add(t3);
			test1 = "do homework/06.10.2014 00:00/ / /read book/06.10.2014 14:00/"
					+ "06.10.2014 15:00/ /relax/06.10.2014 15:00/07.10.2014 15:00/"
					+ "yolo,gg,/";
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
