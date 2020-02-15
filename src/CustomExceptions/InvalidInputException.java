package CustomExceptions;

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
	public InvalidInputException(String atr, String expectedFormat) {
		super("Invalid format for field " + atr + ". Field must contain only " + expectedFormat + ".");
	}
} 
