package CustomExceptions;
import model.TurnsManager;

public class TurnsLimitExceededException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	TurnsLimitExceededException(){
		super("Turns limit exceeded. There are only " + TurnsManager.NUMBER_OF_POSSIBLE_IDS + " possible turns per day");
	}
}
