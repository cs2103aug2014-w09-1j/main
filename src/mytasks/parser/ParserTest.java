package mytasks.parser;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mytasks.file.Task;
import mytasks.logic.AddCommand;
import mytasks.logic.Command;
import mytasks.logic.DeleteCommand;
import mytasks.logic.SortCommand;
import mytasks.logic.UpdateCommand;

import org.junit.Test;

public class ParserTest {
	//TODO: fix wrong test cases
	private Command test1 = null;
	private Command test2 = null;
	private Command test3 = null;
	private Command test4 = null;
	private Command test5 = null;
	private Command test6 = null;
	private Command test7 = null;
	private Command test8 = null;
	private Command test9 = null;
	private Command test10 = null;
	private Command test11 = null;
	private Command test12 = null;
	private Command test13 = null;
	private Command test14 = null;
	private Command test15 = null;
	private Command test16 = null;
	private Command test17 = null;
	private Command test18 = null;
	MyTasksParser tester = new MyTasksParser();

	@Test
	public void addTest() {
		initAddObjects();
		assertObjFields(test1, tester.parseInput("add dinner"));
		assertObjFields(test2, tester.parseInput("add dinner 18.09.2014"));
		assertObjFields(test3, tester.parseInput("add submit assignment 20.09.2014 12:00"));
		assertObjFields(test4, tester.parseInput("add do homework 19.09.2014 #cs2103"));
		assertObjFields(test5, tester.parseInput("add do homework 19.09.2014 #cs2103 #urgent #gg"));
		assertObjFields(test6, tester.parseInput("add have fun! #notpossible 18.09.2014"));
		assertObjFields(test12, tester.parseInput("add code for project 06.10.2014 from 12:00 to 14:00"));
	}
	
	@Test
	public void deleteTest() {
		initDeleteObjects();
		assertObjFields(test7, tester.parseInput("delete CS2103 meeting"));
	}
	
	@Test
	public void testTimedTask(){
		initAddObjects();
		initUpdateObjects();
		//assertObjFields(test12, tester.parseInput("add code for project 06.10.2014 from 12pm to 2pm"));
		//assertObjFields(test14, tester.parseInput("update sleep - wake up from 05.Oct.2014 to 06.Oct.2014"));
		//assertObjFields(test13, tester.parseInput("update read book - write report from 09.10.2014 2pm to 10.Oct.2014 14:00  #gg"));
		assertObjFields(test17, tester.parseInput("add do homework next tuesday from 2am to 3am"));
		//assertObjFields(test18, tester.parseInput("add coding from today 2am to tomorrow 3am"));
		
	}
	
	@Test
	public void updateTest() {
		initUpdateObjects();
		assertObjFields(test8, tester.parseInput("update meeting - CS2103 meeting"));
		assertObjFields(test9, tester.parseInput("update meeting cs2103 - 20.09.2014"));
		assertObjFields(test10, tester.parseInput("update meeting - #CS2103"));
		assertObjFields(test11, tester.parseInput("update meeting - play 19.09.2014 #yolo"));
		assertObjFields(test13, tester.parseInput("update read book - write report from 09.Oct.2014 14:00 to 10.Oct.2014 14:00  #gg"));
		assertObjFields(test14, tester.parseInput("update sleep - wake up from 05.Oct.2014 to 06.Oct.2014"));
	}
	
	@Test
	public void sortTest() {
		initSortObjects();
		assertObjFields(test15, tester.parseInput("sort important"));
		assertObjFields(test16, tester.parseInput("sort CS2103 CS2106 CS2101"));
	}
	
	@Test
	public void locateLabelsTest() {
		String[] words = {"#hashtag", "#can", "not", "#be" ,"#anywhere"};
		assertEquals("hashtag",tester.locateLabels(words).get(0));
		assertEquals("can",tester.locateLabels(words).get(1));
		assertEquals("be",tester.locateLabels(words).get(2));
		assertEquals("anywhere",tester.locateLabels(words).get(3));
	}
	
	@Test
	public void removeLabelTest() {
		String[] words = {"#hashtag", "#can", "not", "#be" ,"#anywhere"};
		assertEquals("not", tester.removeLabels(words)[0]);
	}
	
	@Test
	public void extractDateTest() {

		Date date1 = null;
		Date date2 = null;
		Date date3 = null;
		Date date4 = null;
		Date date5 = null;
		Date date6 = null;
		Date date7 = null;
		try {
			date1 = MyTasksParser.dateFormats.get(0).parse("23.09.2014");
			date2 = MyTasksParser.dateFormats.get(1).parse("30.10.2014 14:00");
			date3 = MyTasksParser.dateFormats.get(2).parse("05.Oct.2014");
			date4 = MyTasksParser.dateFormats.get(3).parse("29.Oct.2014 14:00");
			date5 = MyTasksParser.dateFormats.get(3).parse("29.Oct.2014 15:00");
			date6 = MyTasksParser.dateFormats.get(1).parse("05.10.2014 11:00");
			date7 = MyTasksParser.dateFormats.get(1).parse("06.10.2014 01:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String[] words = {"#hashtag", "#can", "not", "#be" ,"#anywhere"};
		assertEquals(null, tester.extractDate(words).getDate1());
		
		String[] words1 = {"#hashtag", "#can", "not", "#be" ,"23.09.2014"};
		assertEquals(date1, tester.extractDate(words1).getDate1());
		
		String[] words2 = {"#hashtag", "#can", "not", "23.09.2014", "#be"};
		assertEquals(date1, tester.extractDate(words2).getDate1());
		
		String[] words3 = {"30.10.2014", "14:00", "#nothing"};
		assertEquals(date2, tester.extractDate(words3).getDate1());
		
		String[] words4 = {"05.10.2014", "desc"};
		assertEquals(date3, tester.extractDate(words4).getDate1());
		
		String[] words5 = {"random", "29.Oct.2014", "from", "14:00", "to", "15:00"};
		assertEquals(date4, tester.extractDate(words5).getDate1());
		assertEquals(date5, tester.extractDate(words5).getDate2());
		
		String[] words6 = {"desc", "from", "05.10.2014", "11:00", "to", "06.10.2014", "01:00"};
		assertEquals(date6, tester.extractDate(words6).getDate1());
		assertEquals(date7, tester.extractDate(words6).getDate2());
	}
	
	private void initAddObjects() {
		try {
			test1 = new AddCommand("dinner", null, null, null, null);
			
			Date date2 = MyTasksParser.dateFormats.get(0).parse("18.09.2014");
			test2 = new AddCommand("dinner", date2, null, null, null);
			
			Date date3 = MyTasksParser.dateTimeFormats.get(0).parse("20.09.2014 12:00");
			test3 = new AddCommand("submit assignment", date3, null, null, null);
			
			ArrayList<String> list4 = new ArrayList<String>();
			list4.add("cs2103");
			Date date4 = MyTasksParser.dateFormats.get(0).parse("19.09.2014");
			test4 = new AddCommand("do homework", date4, null, list4, null);
			
			ArrayList<String> list5 = new ArrayList<String>();
			list5.add("cs2103");
			list5.add("urgent");
			list5.add("gg");
			Date date5 = MyTasksParser.dateFormats.get(0).parse("19.09.2014");
			test5 = new AddCommand("do homework", date5, null ,list5, null);
			
			ArrayList<String> list6 = new ArrayList<String>();
			list6.add("notpossible");
			Date date6 = MyTasksParser.dateFormats.get(0).parse("18.09.2014");
			test6 = new AddCommand("have fun!", date6, null, list6, null);
			
			Date date121 = MyTasksParser.dateTimeFormats.get(0).parse("06.10.2014 12:00");
			Date date122 = MyTasksParser.dateTimeFormats.get(0).parse("06.10.2014 14:00");
			test12 = new AddCommand("code for project", date121, date122, null, null);
			
			Date temp = new Date();
			String dateOnly = MyTasksParser.dateFormats.get(0).format(temp);
			Date date171 = MyTasksParser.dateTimeFormats.get(1).parse(dateOnly+ " 2am");
			Date date172 = MyTasksParser.dateTimeFormats.get(1).parse(dateOnly+ " 3am");
			test17 = new AddCommand("do homework", date171, date172, null, null);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void initDeleteObjects() {
		test7 = new DeleteCommand("CS2103 meeting", null, null, null, null);
	}
	
	private void initUpdateObjects() {
		try {
			test8 = new UpdateCommand("CS2103 meeting", null, null, null, "meeting");
			
			Date date9 = MyTasksParser.dateFormats.get(0).parse("20.09.2014");
			test9 = new UpdateCommand(null, date9, null, null, "meeting cs2103");
			
			ArrayList<String> list10 = new ArrayList<String>();
			list10.add("CS2103");
			test10 = new UpdateCommand(null, null, null, list10, "meeting");
			
			ArrayList<String> list11 = new ArrayList<String>();
			list11.add("yolo");
			Date date11 = MyTasksParser.dateFormats.get(0).parse("19.09.2014");
			test11 = new UpdateCommand("play", date11, null, list11, "meeting");
			
			ArrayList<String> list13 = new ArrayList<String>();
			list13.add("gg");
			Date date131 = MyTasksParser.dateTimeFormats.get(0).parse("09.10.2014 14:00");
			Date date132 = MyTasksParser.dateTimeFormats.get(0).parse("10.10.2014 14:00");
			test13 = new UpdateCommand("write report", date131, date132, list13, "read book");
			
			Date date141 = MyTasksParser.dateFormats.get(0).parse("05.10.2014");
			Date date142 = MyTasksParser.dateFormats.get(0).parse("06.10.2014");
			test14 = new UpdateCommand("wake up", date141, date142, null, "sleep");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void initSortObjects(){
		ArrayList<String> list15 = new ArrayList<String>();
		list15.add("important");
		test15 = new SortCommand(null, null, null, list15, null);
		
		ArrayList<String> list16 = new ArrayList<String>();
		list16.add("CS2103");
		list16.add("CS2106");
		list16.add("CS2101");
		test16 = new SortCommand(null, null, null, list16, null);
	}
	
	private void assertObjFields(Command testCase, Command result){
		Task testCaseTask = testCase.getTask();
		Task resultTask = result.getTask();
		// System.out.println(testCaseTask.getDescription());
		// System.out.println(resultTask.getDescription());
		// System.out.println(resultTask.getFromDateTime());
		// System.out.println(resultTask.getToDateTime());
		assertEquals(testCase.getToUpdateTaskDesc(),result.getToUpdateTaskDesc());
		assertEquals(testCaseTask, resultTask);
	}

}
