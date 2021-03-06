package model;

import java.io.Serializable;

/**
 * This class models the entity of User.
 * @author Sebastián García acosta
 *
 */
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ------------------------------------------------------------------------------------------------
	// Constants
	// ------------------------------------------------------------------------------------------------	
	public static final String CC = "citizenship card";
	public static final String FI = "foreigner ID";
	public static final String PA = "passport";
	public static final String IC = "Identity card";
	public static final String CR = "Civil register";
	public static final String[] TYPES_OF_DOCUMENTS = {CC, FI, PA, IC, CR};
	
	// ------------------------------------------------------------------------------------------------
	// Attributes
	// ------------------------------------------------------------------------------------------------
	private String names;
	@SuppressWarnings("unused")
	private String surnames;
	private String id;
	private String typeOfDocument;	
	private String cellphoneNumber;
	private String address;
	private Turn turn;
	private DateTime lastBannedDateTime;
	public int numberOfAbsences;

	/**
	 * Creates an User object.
	 * @param n, String, name.
	 * @param id, String, id.
	 * @param tod, String, type of document.
	 * @param cpn, String, cell-phone number.
	 * @param a, String, address.
	 * @param t, Turn, turn.
	 */
	public User(String n, String s, String id, String tod, String cpn, String a, Turn t) {
		this.names = n;
		this.surnames = s;
		this.id = id;
		this.typeOfDocument = tod;
		this.cellphoneNumber = cpn;
		this.address = a;
		this.turn = t;
		this.lastBannedDateTime = null;
	}
	
	/**
	 * Returns the user's turn.
	 * @return Turn, turn.
	 */
	public Turn getTurn() {
		return turn;
	}

	/**
	 * @return the names
	 */
	public String getNames() {
		return names;
	}

	/**
	 * @return the surnames
	 */
	public String getSurnames() {
		return surnames;
	}

	/**
	 * @param surnames the surnames to set
	 */
	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(String names) {
		this.names = names;
	}

	/**
	 * Updates the user's turn.
	 * @param turn, Turn turn.
	 */
	public void setTurn(Turn turn) {
		this.turn = turn;
	}

	/**
	 * Returns the Id of user.
	 * @return String, user id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return DateTime, the bannedDateTime
	 */
	public DateTime getLastBannedDateTime() {
		return lastBannedDateTime;
	}

	/**
	 * @param DateTime, bannedDateTime the bannedDateTime to set
	 */
	public void setLastBannedDateTime(DateTime bannedDateTime) {
		this.lastBannedDateTime = bannedDateTime;
	}

	/**
	 * Returns the User string representation.
	 * @return, String, attributes of user.
	 */
	@Override
	public String toString() {
		return names + "," + surnames + "," + numberOfAbsences;
	}
}