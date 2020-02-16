package model;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import CustomExceptions.*;

/**
 * This class represents the turns handler and is in charge of the logic of the problem.
 * For more details about the context of the problem and the problem itself, got to: 
 * https://docs.google.com/document/d/1B5HBI3Hz4JossDKzX5d2sSnTj3zOvktyk7KiiJ_dFsY/edit
 * @author Sebastián García Acosta
 *
 */
public class TurnsManager {

	// ------------------------------------------------------------------------------------------------
	// Attributes
	// ------------------------------------------------------------------------------------------------
	
	/*
	 * ArrayList containing the <br>unique</br> users registered. 
	 */
	private ArrayList<User> users;
	
	/**
	 * ArrayList containing the turns registered. 
	 * Its length could be longer than users ArrayList due to the fact that a user can have multiple turns throughout the
	 * application life.  
	 */
	private ArrayList<Turn> turns;
	
	/**
	 * Turn, this attribute is used to indicate what is the last turn assigned. Next turns assigned will be based on this.
	 */
	public Turn lastTurn;
	
	/**
	 * This final array is used to indicate the required fields needed to register a user. 
	 */
	public static final String[] REQUIRED_FIELDS = {"names", "surnames", "type of document", "document number"};

	// ------------------------------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------------------------------
	
	/**
	 * Creates an TurnsManager object
	 * <b>post: </b> a TurnsManager object was created and its attributes were initialized. 
	 */
	public TurnsManager() {
		this.turns = new ArrayList<Turn>();
		this.users = new ArrayList<User>();
		this.lastTurn = new Turn("A-1", null);
	}

	// ------------------------------------------------------------------------------------------------
	// Methods
	// ------------------------------------------------------------------------------------------------

	/**
	 * Adds a user to the turns manager if and only if potential user's fields meet the following rules: <br>
	 * @param n, String, name. REQUIRED FIELD. Must be alphabetic.
	 * @param s, String, surnames. REQUIRED FIELD. Must be alphabetic.
	 * @param id, String id. REQUIRED FIELD. Must be numerical.
	 * @param tod, String, type of document. REQUIRED FIELD. Possible values: CC, CE, PA, TI, RC (see User class for detailed explanation).
	 * @param cpn, String cell-phone number. Must be numerical.
	 * @param a, String, address. Optional field.
	 * @param t, Turn, turn assigned to user. Initially is null.
	 * <b>All parameters (n, s, id, cpn, a, t) are not null. <b>
	 * <b>post:</b> User was added successfully if all required fields were not blank, all fields were in their corresponding format, and its id was unique. Otherwise, the corresponding exception was thrown.    
	 * @throws UserAlreadyRegisteredException if another user with same id is found.
	 * @throws BlankRequiredFieldException if one of the REQUIRED FIELDS indicated is blank. 
	 * @throws InvalidInputException if one of the numeric fields indicated above as numerical does not contain only numbers.
	 */
	public void addUser(String n, String s, String id, String tod, String cpn, String a, Turn t)
			throws UserAlreadyRegisteredException, BlankRequiredFieldException, InvalidInputException {
		// ------------------------------------------------------------------------------------------------
		// Validate that user is not already registered
		// ------------------------------------------------------------------------------------------------		
		if (searchUser(id) != null)	throw new UserAlreadyRegisteredException(id);

		// ------------------------------------------------------------------------------------------------
		// Validate that required fields are not blank
		// ------------------------------------------------------------------------------------------------		
		String[] required_args = {n, s, tod, id};
		Map<String, String> requiredFields = IntStream.range(0, REQUIRED_FIELDS.length).boxed()
											.collect( Collectors.toMap(i -> REQUIRED_FIELDS[i], i -> required_args[i]));
		
		ArrayList<String> blankFieldsList = requiredFields.entrySet().stream()
										.filter(item -> item.getValue().isBlank())
										.map(Map.Entry::getKey)
										.collect(Collectors.toCollection(ArrayList::new));
		
		if (!blankFieldsList.isEmpty())	throw new BlankRequiredFieldException(blankFieldsList);
		
		// ------------------------------------------------------------------------------------------------
		// Validate that alphabetic fields contains only letters
		// ------------------------------------------------------------------------------------------------		
		String[] alphabeticFields = {n, s, tod};
		Map<String, String> alphabeticFieldsDict = IntStream.range(0, alphabeticFields.length).boxed()
			    .collect(Collectors.toMap(i -> REQUIRED_FIELDS[i], i -> alphabeticFields[i]));		

		ArrayList<String> wrongAlphabeticFields = alphabeticFieldsDict.entrySet().stream()
												  .filter( item -> !item.getValue()
														  			.replaceAll("\\s+","")
														  			.chars()
														  			.allMatch(Character::isLetter))
												  .map(Map.Entry::getKey)
												  .collect(Collectors.toCollection(ArrayList::new));
		
		if(!wrongAlphabeticFields.isEmpty()) throw new InvalidInputException(wrongAlphabeticFields, "alphabetic characters");

		// ------------------------------------------------------------------------------------------------
		// Validate that numeric fields contain only numbers
		// ------------------------------------------------------------------------------------------------
		ArrayList<String> invalidNumericFields = new ArrayList<String>();
		if (!id.matches("\\d+")) invalidNumericFields.add("document number");
		if (!cpn.isBlank() && !cpn.matches("\\d+")) invalidNumericFields.add("cellphone number");
		
		if(!invalidNumericFields.isEmpty()) throw new InvalidInputException(invalidNumericFields, "numerical characters");
		users.add(new User(n, s, id, tod, cpn, a, t));
	}
	
	/**
	 * Searches a user by its id. 
	 * @param id, String, identification number of user.
	 * @return User object if found; otherwise, null.
	 */
	public User searchUser(String id) {
		User found = null;
		boolean wasFound = false;
		for(int i=0; i < users.size() && !wasFound; i++) {
			User turn_i = users.get(i);
			if(turn_i.getId().equalsIgnoreCase(id)) {
				found = turn_i;
				wasFound = true;
			}
		}
		return found;
	}
	
	/**
	 * Searches a Turn by its id. 
	 * @param id, String, identification code of turn to be searched.
	 * @return Turn object if found; otherwise, null.
	 */
	public Turn searchTurn(String id) {
		Turn found = null;
		boolean wasFound = false;
		for(int i=0; i < turns.size() && !wasFound; i++) {
			Turn turn_i = turns.get(i);
			if(turn_i.getId().equalsIgnoreCase(id)) {
				found = turn_i;
				wasFound = true;
			}
		}
		return found;
	}

	/**
	 * Registers next available turn to user with id specified.
	 * @param id, String, identification number of user to whom the turn will be assigned. Cannot be null.
	 * <b>post: <b> turn was registered successfully if parameter was not null, user was found and didn't have a turn assigned already; otherwise, corresponding exception was thrown.
	 * After setting turn to user, lasTurn was updated.
	 * @throws UserNotFoundException if user with the specified id is not found.
	 * @throws UserAlreadyHasATurnException, if user already has a turns assigned.
	 */
	public void registerTurn(String id) throws UserAlreadyHasATurnException, UserNotFoundException {
		User usr = searchUser(id);
		if(usr == null) 				throw new UserNotFoundException(id);
		else if (usr.getTurn() != null) throw new UserAlreadyHasATurnException(usr);
		else {
			Turn turn = new Turn(generateNextTurnId(lastTurn), usr);
			usr.setTurn(turn);
			turns.add(turn);
			lastTurn.setId(turn.getId());
		}
	}

	/**
	 * Generates next turn id ranging from A00 to Z99.
	 * @param currentTurn, Turn, previous turn, based on this the next turn will be generated. 
	 * E.g. if currentTurn was A0, next turn will be A1; if currentTurn was Z99, next turn will be A00.
	 * @return String, id of the next turn. Its first character is a letter and the rest are numbers.
	 */
	public String generateNextTurnId(Turn currentTurn) {
		String currTurnId = currentTurn.getId();
		char letter = currTurnId.charAt(0);
		int number = Integer.parseInt(currTurnId.substring(1, currTurnId.length()));
		int ASCIICurrLetter = (int) letter;
		int nextASCIILetter = (ASCIICurrLetter == 90) ? 65 : ASCIICurrLetter+1;
		int nextTurnNumber = (number < 99) ? number + 1 : 0;
		char nextTurnLetter = (number < 99) ? letter : (char) nextASCIILetter;
		String nextTurnId = String.valueOf(nextTurnLetter) + ((nextTurnNumber < 10) ? "0" + nextTurnNumber : String.valueOf(nextTurnNumber));
		return nextTurnId;
	}
	
	/**
	 * Dispatches the current turn: sets the state of current turn to either ATTENDED or USER_NOT_PRESENT. 
	 * @param state, String, possible values are: "Attended." or "User not present".
	 * <b>post:<b> After update the state of the current turn, user that had that turn could register another turn. (User's field Turn will be set to null).
	 * @throws NoSuchElementException, if there are no turns registered whose state is ON_HOLD, i.e. all turns registered were already dispatched.
	 */
	public void dispatchTurn(Turn turn, String state) throws NoSuchElementException {
		if(turn == null) throw new NoSuchElementException("Turn not found");
		turn.setState(state);
		searchUser(turn.getUser().getId()).setTurn(null);;
	}
	
	/**
	 * Consults what is the next turn to be attended.
	 * @return Turn, next turn to be attended.
	 */
	public Turn consultNextTurnToBeAttended() throws NoSuchElementException {
		Turn turn;
		try {
			turn = turns.stream().filter(obj -> obj.getState().contentEquals(Turn.ON_HOLD))
					.skip(1).findAny().get();
		} catch(NoSuchElementException e) { throw new NoSuchElementException("There are no next turn to be attended."); }
		return turn;
	}
	
	/**
	 * Return the first turn that has not be dispatched.
	 * @return Turn, the first turn that has not been dispatched.
	 * @throws NoSuchElementException, if all turns are already dispatched, i.e. there are no turns waiting to be attended.
	 */
	public Turn getCurrentTurn() throws NoSuchElementException {
		Turn curr_turn;
		try {
			curr_turn = turns.stream().filter(obj -> obj.getState().contentEquals(Turn.ON_HOLD)).findFirst().get();
		} catch(NoSuchElementException e) { throw new NoSuchElementException("There are not turns in 'On hold' state."); }
		return curr_turn;
	}
	
	// ------------------------------------------------------------------------------------------------
	// Getters and setters
	// ------------------------------------------------------------------------------------------------

	/**
	 * Returns users.
	 * @return ArrayList<User>, users.
	 */
	public ArrayList<User> getUsers() {
		return this.users;
	}

	/**
	 * Replace current users for the ones passed as parameter.
	 * @param users, ArrayList<User>.
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * Returns turns.
	 * @return ArrayList<Turn>, turns registered.
	 */
	public ArrayList<Turn> getTurns() {
		return this.turns;
	}

	/**
	 * Update turns.
	 * @param turns, ArrayList<Turn> turn.
	 */
	public void setTurns(ArrayList<Turn> turns) {
		this.turns = turns;
	}

	/**
	 * Updates current turn.
	 * @param currentTurn, Turn.
	 */
	public void setLastTurn(Turn lastTurn) {
		this.lastTurn = lastTurn;
	}

}