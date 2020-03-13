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
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}
	
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
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
	
	public void plusMinutes(int newMinutes) {
		int actualMins = this.minutes + newMinutes;
		if(actualMins >= 60) {
			this.minutes = actualMins % 60;
			int newHours = actualMins / 60;
			plusHours(newHours);
		}else { this.minutes = actualMins; }
	}
	
	public void plusHours(int newHours) {
		int actualHours = this.hours + newHours;
		if(actualHours >= 24) {
			this.hours = actualHours % 24;
			int newDays = actualHours / 24;
			plusDays(newDays);
		}else { this.hours = actualHours; }
	}
	
	public void plusDays(int newDays) {
		int actualDays = this.day + newDays;
		if( actualDays >= MONTHS[month - 1] ) {
			this.day = actualDays % MONTHS[month -1];
			int newMonth = actualDays / MONTHS[month - 1];
			plusMonths(newMonth);
		} else { this.day = actualDays; }
	}
	
	public void plusMonths(int newMonths) {
		int actualMonth = this.month + newMonths;
		if(actualMonth >= 12) {
			this.month = actualMonth % 12;
			int newYears = actualMonth / 12;
			plusYears(newYears);
		}else {this.month = actualMonth; }
	}
	
	public void plusYears(int newYears) {
		int actualYear = this.year + newYears;
		this.year = actualYear;
/*		if(actualYear >= 1000) {
			this.year = actualYear % 1000;
		} else { this.year = actualYear; }*/
	}
	
	public LocalDateTime toLocalDateTime() {
		return LocalDateTime.of(year, month, day, hours, minutes, seconds);
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
		MONTHS[1] = ( year % 4 == 0 ) ? 29: 28;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return year + "-" +  month + "-" + day + " " +  hours + ":" + minutes + ":" + seconds;
	}
	
	public boolean isBefore( DateTime dt) {
		return ( this.compareTo(dt) == -1 );
	}
	
	public boolean isAfter(DateTime dt) {
		return ( this.compareTo(dt) == 1 );		
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
