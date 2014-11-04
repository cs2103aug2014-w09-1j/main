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
	MyTasksParser tester = new MyTasksParser();

	@Test
	public void addTest1() {
		Command test1 = new AddCommand("dinner", null, null, null, null);
		assertObjFields(test1, tester.parseInput("add dinner"));
	}

	@Test
	public void addDateTest1() {
		try {
			Date date2 = MyTasksParser.dateFormats.get(0).parse("18.09.2014");
			Command test2 = new AddCommand("dinner", date2, null, null, null);
			assertObjFields(test2, tester.parseInput("add dinner 18.09.2014"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDateTest2() {
		try {
			Date date3 = MyTasksParser.dateTimeFormats.get(0).parse(
					"20.09.2014 12:00");
			Command test3 = new AddCommand("submit assignment", date3, null,
					null, null);
			assertObjFields(test3,
					tester.parseInput("add submit assignment 20.09.2014 12:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDateTest3() {
		try {
			Date temp = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(temp);
			String date3Only = MyTasksParser.dateFormats.get(0).format(
					cal.getTime());
			Date date20 = MyTasksParser.dateFormats.get(0).parse(date3Only);
			cal.setTime(date20);
			int today = cal.get(Calendar.DAY_OF_WEEK);
			int toAdd = -1;
			if (today == Calendar.MONDAY) {
				toAdd = 7;
			} else {
				toAdd = (Calendar.SATURDAY - today + 2) % 7;
			}
			toAdd += 7;
			cal.add(Calendar.DATE, toAdd);
			Date nextXDay = cal.getTime();
			Command test20 = new AddCommand("sleep", nextXDay, null, null, null);
			assertObjFields(test20, tester.parseInput("add sleep next monday"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//TODO: pass this testcase
	@Test
	public void addDateTest4() {
		try {
			Date temp = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(temp);
			String date3Only = MyTasksParser.dateFormats.get(0).format(
					cal.getTime());
			Date date20 = MyTasksParser.dateFormats.get(0).parse(date3Only);
			cal.setTime(date20);
			int today = cal.get(Calendar.DAY_OF_WEEK);
			int toAdd = -1;
			if (today == Calendar.TUESDAY) {
				toAdd = 7;
			} else {
				toAdd = (Calendar.SATURDAY - today + 3) % 7;
			}
			toAdd += 7;
			cal.add(Calendar.DATE, toAdd);
			Date nextXDay = cal.getTime();
			String dayFormat = MyTasksParser.dateFormats.get(0)
					.format(nextXDay);
			Date includeTime = MyTasksParser.dateTimeFormats.get(0).parse(
					dayFormat + " 17:00");
			Command test20 = new AddCommand("sleep", includeTime, null, null,
					null);
			assertObjFields(test20,
					tester.parseInput("add sleep next tuesday 5pm"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDateLabelsTest1() {
		try {
			ArrayList<String> list5 = new ArrayList<String>();
			list5.add("cs2103");
			list5.add("urgent");
			list5.add("gg");
			Date date5 = MyTasksParser.dateFormats.get(0).parse("19.09.2014");
			Command test5 = new AddCommand("do homework", date5, null, list5,
					null);
			assertObjFields(
					test5,
					tester.parseInput("add do homework 19.09.2014 #cs2103 #urgent #gg"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addDateLabelsTest2() {
		try {
			ArrayList<String> list6 = new ArrayList<String>();
			list6.add("notpossible");
			Date date6 = MyTasksParser.dateFormats.get(0).parse("18.09.2014");
			Command test6 = new AddCommand("have fun!", date6, null, list6,
					null);
			assertObjFields(test6,
					tester.parseInput("add have fun! #notpossible 18.09.2014"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addTimedTest1() {
		try {
			Date date121 = MyTasksParser.dateTimeFormats.get(0).parse(
					"06.10.2014 12:00");
			Date date122 = MyTasksParser.dateTimeFormats.get(0).parse(
					"06.10.2014 14:00");
			Command test12 = new AddCommand("code for project", date121,
					date122, null, null);
			assertObjFields(
					test12,
					tester.parseInput("add code for project 06.10.2014 from 12:00 to 14:00"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addTimedTest2() {
		try {
			Date temp = new Date();
			String dateOnly = MyTasksParser.dateFormats.get(0).format(temp);
			Date date171 = MyTasksParser.dateTimeFormats.get(1).parse(
					dateOnly + " 2am");
			Date date172 = MyTasksParser.dateTimeFormats.get(1).parse(
					dateOnly + " 3am");
			Command test17 = new AddCommand("do homework", date171, date172,
					null, null);
			assertObjFields(test17,
					tester.parseInput("add do homework today from 2am to 3am"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addTimedTest3() {
		try {
			Date temp = new Date();
			String dateOnly = MyTasksParser.dateFormats.get(0).format(temp);
			Calendar cal = Calendar.getInstance();
			cal.setTime(temp);
			cal.add(Calendar.DATE, 1);
			String date2Only = MyTasksParser.dateFormats.get(0).format(
					cal.getTime());
			Date date181 = MyTasksParser.dateTimeFormats.get(1).parse(
					dateOnly + " 2am");
			Date date182 = MyTasksParser.dateTimeFormats.get(1).parse(
					date2Only + " 3am");
			Command test18 = new AddCommand("coding", date181, date182, null,
					null);
			assertObjFields(
					test18,
					tester.parseInput("add coding from today 2am to tomorrow 3am"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//TODO pass this testcase. dates with next still fails
	@Test
	public void addTimedTest4() {
		try {
			Date temp = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(temp);
			String date3Only = MyTasksParser.dateFormats.get(0).format(
					cal.getTime());
			Date date20 = MyTasksParser.dateFormats.get(0).parse(date3Only);
			cal.setTime(date20);
			int today = cal.get(Calendar.DAY_OF_WEEK);
			int toAdd = -1;
			if (today == Calendar.MONDAY) {
				toAdd = 7;
			} else {
				toAdd = (Calendar.SATURDAY - today + 2) % 7;
			}
			toAdd += 7;
			cal.add(Calendar.DATE, toAdd);
			Date nextXDay = cal.getTime();

			cal.add(Calendar.DATE, -7);
			Date date212 = cal.getTime();
			Command test21 = new AddCommand("sleep more", date212, nextXDay,
					null, null);
			assertObjFields(
					test21,
					tester.parseInput("add sleep more from monday to next monday"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	//TODO pass this
	@Test
	public void addTimedTest5() {
		try {
			Date temp = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(temp);
			String date3Only = MyTasksParser.dateFormats.get(0).format(
					cal.getTime());
			Date date20 = MyTasksParser.dateFormats.get(0).parse(date3Only);
			cal.setTime(date20);
			int today = cal.get(Calendar.DAY_OF_WEEK);
			int toAdd = -1;
			if (today == Calendar.MONDAY) {
				toAdd = 7;
			} else {
				toAdd = (Calendar.SATURDAY - today + 2) % 7;
			}
			toAdd += 7;
			cal.add(Calendar.DATE, toAdd);
			Date nextXDay = cal.getTime();
			String nextXDayOnly = MyTasksParser.dateFormats.get(0).format(nextXDay);
			nextXDay = MyTasksParser.dateTimeFormats.get(0).parse(nextXDayOnly + " 10:00");

			cal.add(Calendar.DATE, -7);
			Date date212 = cal.getTime();
			String date212Only = MyTasksParser.dateFormats.get(0).format(date212);
			date212 = MyTasksParser.dateTimeFormats.get(0).parse(date212Only + " 17:00");
			Command test21 = new AddCommand("sleep more", date212, nextXDay,
					null, null);
			assertObjFields(
					test21,
					tester.parseInput("add sleep more from monday 5pm to next monday 10am"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteTest1() {
		Command test7 = new DeleteCommand("CS2103 meeting", null, null, null,
				null);
		assertObjFields(test7, tester.parseInput("delete CS2103 meeting"));
	}

	@Test
	public void updateDescTest1() {
		Command test8 = new UpdateCommand("CS2103 meeting", null, null, null,
				"meeting");
		assertObjFields(test8,
				tester.parseInput("update meeting - CS2103 meeting"));
	}

	@Test
	public void updateDateTest1() {
		try {
			Date date9 = MyTasksParser.dateFormats.get(0).parse("20.09.2014");
			Command test9 = new UpdateCommand(null, date9, null, null,
					"meeting cs2103");
			assertObjFields(test9,
					tester.parseInput("update meeting cs2103 - 20.09.2014"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateDateTest2() {
		try {
			Date date141 = MyTasksParser.dateFormats.get(0).parse("05.10.2014");
			Date date142 = MyTasksParser.dateFormats.get(0).parse("06.10.2014");
			Command test14 = new UpdateCommand("wake up", date141, date142, null,
					"sleep");
			assertObjFields(
					test14,
					tester.parseInput("update sleep - wake up from 05.Oct.2014 to 06.Oct.2014"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateDateTest3() {
		try {
			Date temp = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(temp);
			cal.add(Calendar.DATE, 1);
			String datetmr = MyTasksParser.dateFormats.get(0).format(
					cal.getTime());
			Date date191 = MyTasksParser.dateTimeFormats.get(1).parse(
					datetmr + " 3pm");
			Date date192 = MyTasksParser.dateTimeFormats.get(1).parse(
					datetmr + " 4pm");
			ArrayList<String> labels19 = new ArrayList<String>();
			labels19.add("yolo");
			Command test19 = new UpdateCommand(null, date191, date192,
					labels19, "play");
			assertObjFields(
					test19,
					tester.parseInput("update play - tomorrow from 3pm to 4pm #yolo"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateLabelTest1() {
		ArrayList<String> list10 = new ArrayList<String>();
		list10.add("CS2103");
		Command test10 = new UpdateCommand(null, null, null, list10, "meeting");
		assertObjFields(test10, tester.parseInput("update meeting - #CS2103"));
	}

	@Test
	public void updateAllTest1() {
		try {
			ArrayList<String> list13 = new ArrayList<String>();
			list13.add("gg");
			Date date131 = MyTasksParser.dateTimeFormats.get(0).parse(
					"09.10.2014 14:00");
			Date date132 = MyTasksParser.dateTimeFormats.get(0).parse(
					"10.10.2014 14:00");
			Command test13 = new UpdateCommand("write report", date131,
					date132, list13, "read book");
			assertObjFields(
					test13,
					tester.parseInput("update read book - write report from 09.Oct.2014 14:00 to 10.Oct.2014 14:00  #gg"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void sortTest1() {
		ArrayList<String> list16 = new ArrayList<String>();
		list16.add("CS2103");
		list16.add("CS2106");
		list16.add("CS2101");
		Command test16 = new SortCommand(null, null, null, list16, null);
		assertObjFields(test16, tester.parseInput("sort CS2103 CS2106 CS2101"));
	}

	@Test
	public void locateLabelsTest1() {
		String[] words = { "#hashtag", "#can", "not", "#be", "#anywhere" };
		ArrayList<String> test = new ArrayList<String>();
		test.add("hashtag");
		test.add("can");
		test.add("be");
		test.add("anywhere");
		assertEquals(test, tester.locateLabels(words));
	}

	@Test
	public void removeLabelTest1() {
		String[] words = { "#hashtag", "#can", "not", "#be", "#anywhere" };
		assertEquals("not", tester.removeLabels(words)[0]);
	}

	@Test
	public void extractDateTest1() {
		String[] words = { "#hashtag", "#can", "not", "#be", "#anywhere" };
		assertEquals(null, tester.extractDate(words).getDate1());
	}

	@Test
	public void extractDateTest2() {
		try {
			Date date1 = MyTasksParser.dateFormats.get(0).parse("23.09.2014");
			String[] words1 = { "#hashtag", "#can", "not", "#be", "23.09.2014" };
			assertEquals(date1, tester.extractDate(words1).getDate1());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void extractDateTest3() {
		try {
			Date date2 = MyTasksParser.dateTimeFormats.get(0).parse(
					"30.10.2014 14:00");
			String[] words3 = { "30.10.2014", "14:00", "#nothing" };
			assertEquals(date2, tester.extractDate(words3).getDate1());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void extractDateTest4() {
		try {
			Date date4 = MyTasksParser.dateTimeFormats.get(3).parse(
					"29.Oct.2014 14:00");
			Date date5 = MyTasksParser.dateTimeFormats.get(3).parse(
					"29.Oct.2014 15:00");
			DoubleDate temp = new DoubleDate(date4, date5);
			String[] words5 = { "random", "29.Oct.2014", "from", "14:00", "to",
					"15:00" };
			assertEquals(temp, tester.extractDate(words5));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void extractDateTest5() {
		try {
			Date date6 = MyTasksParser.dateTimeFormats.get(0).parse(
					"05.10.2014 11:00");
			Date date7 = MyTasksParser.dateTimeFormats.get(0).parse(
					"06.10.2014 01:00");
			DoubleDate temp = new DoubleDate(date6, date7);
			String[] words6 = { "desc", "from", "05.10.2014", "11:00", "to",
					"06.10.2014", "01:00" };
			assertEquals(temp, tester.extractDate(words6));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void assertObjFields(Command testCase, Command result) {
		Task testCaseTask = testCase.getTask();
		Task resultTask = result.getTask();
		assertEquals(testCase.getToUpdateTaskDesc(),
				result.getToUpdateTaskDesc());
		assertEquals(testCaseTask, resultTask);
	}

}
