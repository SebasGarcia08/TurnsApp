package CustomExceptions;

import model.User;

/**
 * This Class models the Exception: UserAlreadyHasATurnException,
 * @author Sebastián García Acosta
 *
 */
public class UserAlreadyHasATurnException extends Exception{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * @param usr, User.
	 */
	public UserAlreadyHasATurnException(User usr) {
		super("User with id " + usr.getId() + " already has turn " 
			  + usr.getTurn().getId() + " assgined and its state is: " + usr.getTurn().getState());
	}
}
