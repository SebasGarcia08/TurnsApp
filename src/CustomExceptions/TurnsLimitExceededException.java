package CustomExceptions;

public class TurnsLimitExceededException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	TurnsLimitExceededException(){
		super("Turns limit exceeded. There are only 2600 turns per day");
	}
}
