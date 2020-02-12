package CustomExceptions;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(String id) {
		super("User with id " + id + " was not found.");
	}
}
