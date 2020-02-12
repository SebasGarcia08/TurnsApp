package CustomExceptions;

public class UserAlreadyRegisteredException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyRegisteredException(String id) {
		super("User with id "+ id +" is already registered, cannot overwrite information");
	}
}
