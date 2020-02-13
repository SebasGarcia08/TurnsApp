package model;

public class Turn {

	public static final String USER_NOT_PRESENT = "user not present";
	public static final String ATTENDED = "attended";
	public static final String ON_HOLD = "On hold...";
	private String state;
	private String id;

	/**
	 * Constructor
	 * @param id
	 * @param user
	 */
	public Turn(String id) {
		// TODO - implement Turn.Turn
		this.id = id;
		this.state = ON_HOLD;
	}

	/**
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "Turn [state=" + state + ", id=" + id + "]";
	}
	
	@Override
	public boolean equals(Object o) {
		return ( ((Turn) o).getId() == this.id );
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}