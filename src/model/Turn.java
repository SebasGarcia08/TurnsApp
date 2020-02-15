package model;

public class Turn {

	public static final String USER_NOT_PRESENT = "User not present";
	public static final String ATTENDED = "Attended";
	public static final String ON_HOLD = "On hold...";
	private String state;
	private String id;
	private User user;

	/**
	 * Constructor
	 * @param id
	 * @param user
	 */
	public Turn(String id, User usr) {
		// TODO - implement Turn.Turn
		this.id = id;
		this.state = ON_HOLD;
		this.user = usr;
	}

	/**
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
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
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}