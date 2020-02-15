package CustomExceptions;

/**
 * This class models the exception UserAlreadyRegisteredException.
 * @author sebastian
 *
 */
public class UserAlreadyRegisteredException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param id, String.
	 */
	public UserAlreadyRegisteredException(String id) {
		super("User with id "+ id +" is already registered, cannot overwrite information");
	}
}
