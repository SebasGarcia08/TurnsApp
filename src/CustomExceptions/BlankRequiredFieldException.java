package CustomExceptions;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class models the BlankRequiredFieldException.
 * @author Sebastián García Acosta
 *
 */
public class BlankRequiredFieldException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor.
	 * @param requiredFields, ArrayList<String> containing the names of the required fields that were blank.
	 */
	public BlankRequiredFieldException(ArrayList<String> requiredFields) {
		super("Parameters: " + 
			   requiredFields.stream().map(Object::toString)
			   .collect(Collectors.joining(", ")) + " must be filled."); 
	}
	
}
