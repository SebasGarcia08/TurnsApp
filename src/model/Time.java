package model;

public class Time {
	private int hours;
	private int minutes;
	private int seconds;
	
	public Time(int h, int m, int s) {
		super();
		this.hours = h;
		this.minutes = m;
		this.seconds = s;
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
	
	@Override
	public String toString() {
		return hours + ":" + minutes + ":" + seconds;
	}
}
