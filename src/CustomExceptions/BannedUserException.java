package CustomExceptions;
import model.DateTime;

public class BannedUserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BannedUserException(String id, DateTime bannedDateTime) {
		super("User with id " + id + " is banned until " + bannedDateTime.toString());
	}
}
