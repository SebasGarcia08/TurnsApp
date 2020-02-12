package CustomExceptions;

import model.*;

public class UserAlreadyHasATurnException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserAlreadyHasATurnException(User usr) {
		super("User with id " + usr.getId() + " already has turn " 
			  + usr.getTurn().getId() + " assgined and its state is: " + usr.getTurn().getState());
	}
}
