package CustomExceptions;

public class InvalidFormatException extends IllegalArgumentException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFormatException(String atr, String expectedFormat) {
		super("Invalid format for field " + atr + ". Field must contain only " + expectedFormat + ".");
	}
} 
