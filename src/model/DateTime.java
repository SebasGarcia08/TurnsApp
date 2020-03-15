package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DateTime implements Serializable, Comparable<DateTime>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int year;
	private int month;
	private int day;
	private int hours;
	private int minutes;
	private int seconds;
	
	public static int[] MONTHS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	public static String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October","November", "December"};
	
	public DateTime(int Y, int M, int D, int h, int m, int s) {
		super();
		this.year = Y;
		this.day = D;
		this.month = M;
		this.hours = h;
		this.minutes = m;
		this.seconds = s;
		MONTHS[1] = ( (year % 4) == 0 ) ? 29: 28;
	}
	
	/**
	 * Fabric method for returning a new object of this class with specified fields
	 * @param year, int 
	 * @param month, in
	 * @param day, int
	 * @param hours, int
	 * @param minutes, int
	 * @param seconds, int
	 * @return DateTime
	 * @throws Exception if fields are not valid
	 */
	public static DateTime of(int year, int month, int day, int hours, int minutes, int seconds) throws Exception {
		MONTHS[1] = ( (year % 4) == 0 ) ? 29: 28;
		if( month == 2 && day > 28 && year % 4 != 0  )
			throw new Exception("February 29 as " + year + " is not a leap year");
		else if ( month < 0 || month > 12 )
			throw new Exception("Invalid month");
		else if( hours < 0 || hours > 24 )
			throw new Exception("Invalid hours");
		else if(seconds < 0 || seconds > 60)
			throw new Exception("Invalid seconds");
		else if( year < 0 || year > 2999 )
			throw new Exception("Unsupported year");
		else if( day > MONTHS[month -1] || day < 0 )
			throw new Exception( MONTH_NAMES[month - 1] + "has not " + day + " days." );
		else return new DateTime(year, month, day, minutes, hours, seconds);
	}
	
	/**
	 * Adds the milliseconds passed as parameter to the datetime
	 * @param millis
	 */
	public void plusMillis(long millis) {
		int newSecs = (int) (millis / 1000);
		int actualSecs = this.seconds + newSecs;
		if(actualSecs >= 60 ) {
			this.seconds = actualSecs % 60;
			int newMinutes = actualSecs / 60;
			plusMinutes(newMinutes);
		}
		else { this.seconds = actualSecs; }
	}
	
	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * @param seconds the seconds to set
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * Fabric method for returning object with same fields 
	 * @param dt, the DateTime to be cloned
	 * @return DateTime, new object with same fields as the one passed by params.
	 */
	public static DateTime copyOf( DateTime dt){
		return new DateTime(dt.year, dt.month, dt.day, dt.hours, dt.minutes, dt.seconds);
	}
	
	/**
	 * Adds the minutes passed as parameter to the datetime
	 * @param newMinutes
	 */
	public void plusMinutes(int newMinutes) {
		int actualMins = this.minutes + newMinutes;
		if(actualMins >= 60) {
			this.minutes = actualMins % 60;
			int newHours = actualMins / 60;
			plusHours(newHours);
		}else { this.minutes = actualMins; }
	}
	
	/**
	 * Adds the hours passed as parameter to the datetime
	 * @param newHours
	 */
	public void plusHours(int newHours) {
		int actualHours = this.hours + newHours;
		if(actualHours >= 24) {
			this.hours = actualHours % 24;
			int newDays = actualHours / 24;
			plusDays(newDays);
		}else { this.hours = actualHours; }
	}
	
	/**
	 * Adds the days passed as parameter to the datetime
	 * @param newDays
	 */
	public void plusDays(int newDays) {
		int actualDays = this.day + newDays;
		if( actualDays >= MONTHS[month - 1] ) {
			this.day = actualDays % MONTHS[month -1];
			int newMonth = actualDays / MONTHS[month - 1];
			plusMonths(newMonth);
		} else { this.day = actualDays; }
	}
	
	/**
	 * Adds the motnhs passed as parameter to the datetime
	 * @param newMonths
	 */
	public void plusMonths(int newMonths) {
		int actualMonth = this.month + newMonths;
		if(actualMonth >= 12) {
			this.month = actualMonth % 12;
			int newYears = actualMonth / 12;
			plusYears(newYears);
		}else {this.month = actualMonth; }
	}
	
	/**
	 * Adds the year passed as parameter to the datetime
	 * <b>pre: </b> newYears must be les than 1000
	 * @param newYears
	 */
	public void plusYears(int newYears) {
		int actualYear = this.year + newYears;
		this.year = actualYear;
/*		if(actualYear >= 1000) {
			this.year = actualYear % 1000;
		} else { this.year = actualYear; }*/
	}
	
	/**
	 * Fabric method for returning the current datetime
	 * @return
	 */
	public static DateTime now() {
		return new DateTime(LocalDateTime.now().getYear(), 
				LocalDateTime.now().getMonthValue(), 
				LocalDateTime.now().getDayOfMonth(), 
				LocalDateTime.now().getHour(), 
				LocalDateTime.now().getMinute(), 
				LocalDateTime.now().getSecond());
	}
	
	/**
	 * Converts datetime to LocalDateTime object
	 * @return LocalDateTime
	 */
	public LocalDateTime toLocalDateTime() {
		return LocalDateTime.of(year, month, day, hours, minutes, seconds);
	}
	
	/**
	 * Accelerates the time in order to make this datetime at midnight
	 * @return DateTime, this datetime in midnight
	 */
	public DateTime asMidnight() {
		return new DateTime(year, month, day, 23, 59, 59);
	}
	
	/**
	 * Tells if two this datetime is between other tow
	 * @param d1 DateTime
	 * @param d2 DateTime
	 * @return boolean, if this datetime is between d1 and d2
	 */
	public boolean isBetween(DateTime d1, DateTime d2) {
		return ( this.isAfter(d1) && this.isBefore(d2) );
	}
	
	/**
	 * Converts minutes to milliscoends
	 * @param minutes, flaot
	 * @return millisconds of minutes
	 */
	public static long minutes2Millis(float minutes) {
		return (long) ((minutes * 60) * 1000);
	}

	@Override
	public String toString() {
		return year + "-" +  month + "-" + day + " " +  hours + ":" + minutes + ":" + seconds;
	}
	
	/**
	 * Tells if this datetime is before the one passed by param
	 * @param dt
	 * @return
	 */
	public boolean isBefore( DateTime dt) {
		return ( this.compareTo(dt) == -1 );
	}
	
	/**
	 * Tells if this datetime is after the one passed by param
	 * @param dt
	 * @return
	 */
	public boolean isAfter(DateTime dt) {
		return ( this.compareTo(dt) == 1 );		
	}
	
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public int compareTo(DateTime o) {
		int result;
		if( year < o.getYear() ) result = -1;
		else if(year > o.getYear() ) result = 1;
		else {
			if( month < o.getMonth()) result = -1;
			else if(month > o.getMonth()) result =1;
			else {
				if( day < o.getDay()) result = -1;
				else if(day > o.getDay()) result =1;
				else {
					if( hours < o.getHours()) result = -1;
					else if(hours  > o.getHours()) result =1;
					else {
						if( minutes < o.getMinutes() ) result = -1;
						else if(minutes < o.getMinutes() ) result = 1;
						else {
							if( seconds < o.getSeconds() ) result = -1;
							else if(seconds > o.getSeconds() ) result = 1;
							else result = 0;
						}
					}
				}
			}
		}
		return result;
	}
}
