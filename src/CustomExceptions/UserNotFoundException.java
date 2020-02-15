package CustomExceptions;

import java.util.NoSuchElementException;

/**
 * This class models the exception: UserNotFoundException 
 * @author Sebastián Grcía Acosta
 *
 */
public class UserNotFoundException extends NoSuchElementException {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * @param id, String. 
	 */
	public UserNotFoundException(String id) {
		super("User with id " + id + " was not found.");
	}
}
