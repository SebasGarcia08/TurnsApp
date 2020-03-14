package model;

import java.io.Serializable;

/**
 * This class models the Turn entity.
 * @author Sebastián Garcia Acosta
 *
 */
public class Turn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private TurnType turnTpye;
	private DateTime startingDateTime;
	private DateTime endingDateTime;

	// ------------------------------------------------------------------------------------------------
	// Constructor
	// ------------------------------------------------------------------------------------------------	
	/**
	 * Constructor
	 * @param id, String.
	 * @param usr, User.
	 */
	public Turn(String id, User usr, TurnType tt) {
		this.id = id;
		this.state = ON_HOLD;
		this.user = usr;
		this.turnTpye = tt;
	}
	

	/**
	 * @return the startingDateTime
	 */
	public DateTime getStartingDateTime() {
		return startingDateTime;
	}

	/**
	 * @param startingDateTime the startingDateTime to set
	 */
	public void setStartingDateTime(DateTime startingDateTime) {
		this.startingDateTime = startingDateTime;
	}

	/**
	 * @return the endingDateTime
	 */
	public DateTime getEndingDateTime() {
		return endingDateTime;
	}

	/**
	 * @param endingDateTime the endingDateTime to set
	 */
	public void setEndingDateTime(DateTime endingDateTime) {
		this.endingDateTime = endingDateTime;
	}


	public TurnType getTurnTpye() {
		return turnTpye;
	}

	public void setTurnTpye(TurnType turnTpye) {
		this.turnTpye = turnTpye;
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
		return "Turn [state=" + state + ", id=" + id + ", user=" + user + ", turnTpye=" + turnTpye
				+ ", startingDateTime=" + startingDateTime + ", endingDateTime=" + endingDateTime + "]";
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
}