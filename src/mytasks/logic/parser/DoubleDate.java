package mytasks.logic.parser;

import java.util.Date;

//@author A0114302A
/**
 * DoubleDate is a data structure to hold 2 dates for the parser to use. This is
 * a protected class that cannot be accessed from outside package
 */
class DoubleDate {

	private Date mDate1 = null;
	private Date mDate2 = null;

	protected DoubleDate(Date date1, Date date2) {
		mDate1 = date1;
		mDate2 = date2;
	}

	public Date getDate1() {
		return mDate1;
	}

	public Date getDate2() {
		return mDate2;
	}

	@Override
	public boolean equals(Object otherTask) {
		if (otherTask == this) {
			return true;
		}

		if (!(otherTask instanceof DoubleDate)) {
			return false;
		} else {
			DoubleDate otherDate = (DoubleDate) otherTask;
			boolean is1Equal = compareFromDateTime(otherDate.getDate1());
			boolean is2Equal = compareToDateTime(otherDate.getDate2());
			if (!is1Equal||!is2Equal) {
				return false;
			}
		}
		return true;
	}
	
	private boolean compareFromDateTime(Date other){
		boolean result = true;
		if (other == null) {
			if (mDate1 != null) {
				return false;
			}
		} else {
			if (mDate1 == null) {
				return false;
			}
			if (!other.equals(mDate1)) {
				return false;
			}
		}
		return result;
	}
	
	private boolean compareToDateTime(Date other){
		boolean result = true;
		if (other == null) {
			if (mDate2 != null) {
				return false;
			}
		} else {
			if (mDate2 == null) {
				return false;
			}
			if (!other.equals(mDate2)) {
				return false;
			}
		}
		return result;
	}
}