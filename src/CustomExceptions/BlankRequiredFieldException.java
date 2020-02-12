package CustomExceptions;

import java.util.ArrayList;

public class BlankRequiredFieldException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BlankRequiredFieldException(ArrayList<String> requiredFields) {
		super("Parameters: " + String.join(", ", requiredFields) + " must be filled."); 
	}
	
}
