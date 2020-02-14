package CustomExceptions;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlankRequiredFieldException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BlankRequiredFieldException(ArrayList<String> requiredFields) {
		super("Parameters: " + 
			   requiredFields.stream().map(Object::toString)
			   .collect(Collectors.joining(", ")) + " must be filled."); 
	}
	
}
