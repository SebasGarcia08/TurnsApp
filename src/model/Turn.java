package model;

/**
 * This class models the Turn entity.
 * @author Sebastián Garcia Acosta
 *
 */
public class Turn {

	// ------------------------------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------------------------------	
	public static final String USER_NOT_PRESENT = "User not present";
	public static final String ATTENDED = "Attended";
	public static final String ON_HOLD = "On hold...";
	
	// ------------------------------------------------------------------------------------------------
	// Attributes
	// ------------------------------------------------------------------------------------------------	
	private String state;
	private String id;
	private User user;

	// ------------------------------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------------------------------	
	/**
	 * Constructor
	 * @param id, String.
	 * @param usr, User.
	 */
	public Turn(String id, User usr) {
		this.id = id;
		this.state = ON_HOLD;
		this.user = usr;
	}

	/**
	 * Returns user
	 * @return User, user
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * Updates user.
	 * @param usr, User.
	 */
	public void setUser(User usr) {
		this.user = usr;
	}

	/**
	 * Returns id.
	 * @return String, id.
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
	
	/**
	 * Returns state
	 * @return String, state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * Updates state.
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	public boolean equals(Turn obj) {
		return (this.id.equalsIgnoreCase(obj.getId()));
	}
}