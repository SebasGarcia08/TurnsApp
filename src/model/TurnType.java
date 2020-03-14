package model;

import java.io.Serializable;

public class TurnType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private float durationMinutes;
	
	public TurnType(String name, float duration) {
		this.name = name;
		this.durationMinutes = duration;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the durationMinutes
	 */
	public float getDurationMinutes() {
		return durationMinutes;
	}

	/**
	 * @param durationMinutes the durationMinutes to set
	 */
	public void setDurationMinutes(float durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	@Override
	public String toString() {
		return "name: " + name + ", durationMinutes: " + durationMinutes;
	}	
}
