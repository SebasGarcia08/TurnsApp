package CustomExceptions;

public class TurnsLimitExceededException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TurnsLimitExceededException(long numberOfPossible){
		super("Number of turns exceeded. There are only " + numberOfPossible + " possible valid turns per day. Create more users.");
	}
}
