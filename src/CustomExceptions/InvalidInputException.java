package CustomExceptions;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class models the exception: InvalidInputException.
 * @author sebastian
 *
 */
public class InvalidInputException extends IllegalArgumentException{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param atr, String, name of the attribute.
	 * @param expectedFormat, String, expected format of attribute.
	 */
	public InvalidInputException(ArrayList<String> fields, String expectedFormat) {
		super("Invalid format for fields: " + fields.stream().map(Object::toString)
				   .collect(Collectors.joining(", ")) + ". Fields must contain only " + expectedFormat + ".");
	}
} 
