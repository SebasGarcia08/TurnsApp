package CustomExceptions;

public class InvalidInputException extends IllegalArgumentException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException(String atr, String expectedFormat) {
		super("Invalid format for field " + atr + ". Field must contain only " + expectedFormat + ".");
	}
} 
