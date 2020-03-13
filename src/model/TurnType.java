package model;

public class TurnType {
	private String name;
	private float durationMinutes;
	private DateTime dt;
	
	public TurnType(String name, float duration) {
		this.name = name;
		this.durationMinutes = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getDuration() {
		return durationMinutes;
	}

	public void setDuration(float duration) {
		this.durationMinutes = duration;
	}
	
}
