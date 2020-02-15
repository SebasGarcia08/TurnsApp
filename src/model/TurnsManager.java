package model;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
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
	public static final String[] REQUIRED_FIELDS = {"name", "document number", "type of document"};

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
	 * @param id, String id. REQUIRED FIELD. Must be numerical.
	 * @param tod, String, type of document. REQUIRED FIELD. Possible values: CC, CE, PA, TI, RC (see User class for detailed explanation).
	 * @param cpn, String cell-phone number. Must be numerical.
	 * @param a, String, address. Optional field.
	 * @param t, Turn, turn assigned to user. Initially is null.
	 * <b>post:</b> User was added successfully if all required fields were not blank, all fields were in their corresponding format, and its id was unique. Otherwise, the corresponding exception was thrown.    
	 * @throws UserAlreadyRegisteredException if another user with same id is found.
	 * @throws BlankRequiredFieldException if one of the REQUIRED FIELDS indicated is blank. 
	 * @throws InvalidInputException if one of the numeric fields indicated above as numerical does not contain only numbers.
	 */
	public void addUser(String n, String id, String tod, String cpn, String a, Turn t)
			throws UserAlreadyRegisteredException, BlankRequiredFieldException, InvalidInputException {
		Map<String, String> requiredFields = new HashMap<String, String>();
		String[] required_args = {n, id, tod};
		
		for(int i = 0; i < REQUIRED_FIELDS.length; i++)
			requiredFields.put(REQUIRED_FIELDS[i], required_args[i]);
		
		ArrayList<String> blankFieldsList = requiredFields.entrySet().stream()
										.filter(item -> item.getValue().isBlank())
										.map(Map.Entry::getKey)
										.collect(Collectors.toCollection(ArrayList::new));
		if (searchUser(id) != null) throw new UserAlreadyRegisteredException(id);
		if (!blankFieldsList.isEmpty()) throw new BlankRequiredFieldException(blankFieldsList);
		if (!n.matches("^[a-zA-Z]*$")) throw new  InvalidInputException("name", "letters.");
		if (!id.matches("\\d+")) throw new InvalidInputException("identification number", "numbers");
		if (!cpn.isBlank() && !cpn.matches("\\d+")) throw new InvalidInputException("cellphone number", "numbers");
		users.add(new User(n, id, tod, cpn, a, t));
	}

	/**
	 * Searches a user by its id. 
	 * @param id, String, identification number of user.
	 * @return User object if found; otherwise, null.
	 */
	public User searchUser(String id) {
		User found = null;
		for(User u : users) {
			if(u.getId().equals(id)) {
				found = u;
				break;
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
		for(Turn u : turns) {
			if(u.getId().equals(id)) {
				found = u;
				break;
			}
		}
		return found;
	}

	/**
	 * Add a user to 
	 * @param id
	 * @throws UserAlreadyHasATurnException
	 * @throws UserNotFoundException
	 */
	public void registerTurn(String id) throws UserAlreadyHasATurnException, UserNotFoundException {
		User usr = searchUser(id);
		if(usr == null) 				throw new UserNotFoundException(id);
		else if (usr.getTurn() != null) throw new UserAlreadyHasATurnException(usr);
		else {
			Turn turn = new Turn(generateNextTurnId(lastTurn), usr);
			usr.setTurn(turn);
			turns.add(turn);
			lastTurn = new Turn(generateNextTurnId(lastTurn), null);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String generateNextTurnId(Turn currentTurn) {
		String currTurn = currentTurn.getId();
		char letter = currTurn.charAt(0);
		int number = Integer.parseInt(currTurn.substring(1, currTurn.length()));
		int ASCIICurrLetter = (int) letter;
		int nextASCIILetter = (ASCIICurrLetter == 90) ? 65 : ASCIICurrLetter;
		int nextTurnNumber = (number < 99) ? number + 1 : number - 99;
		char nextTurnLetter = (number < 99) ? letter : (char) nextASCIILetter;
		String nextTurnId = String.valueOf(nextTurnLetter) + String.valueOf(nextTurnNumber);
		return nextTurnId;
	}
	
	/**
	 * 
	 * @param turn
	 */
	public void dispatchTurn(String state) throws NoSuchElementException {
		Turn currTurn = getCurrentTurn();
		currTurn.setState(state);
		searchUser(currTurn.getUser().getId()).setTurn(null);;
	}
	
	/**
	 * 
	 * @return
	 * @throws NoSuchElementException
	 */
	public Turn getCurrentTurn() throws NoSuchElementException {
		Turn curr_turn;
		try {
			curr_turn = turns.stream().filter(obj -> obj.getState().contentEquals(Turn.ON_HOLD)).findFirst().get();
		} catch(NoSuchElementException e) { throw new NoSuchElementException("There are no turns registered yet."); }
		return curr_turn;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<User> getUsers() {
		return this.users;
	}

	/**
	 * 
	 * @param users
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Turn> getTurns() {
		return this.turns;
	}

	/**
	 * 
	 * @param turns
	 */
	public void setTurns(ArrayList<Turn> turns) {
		this.turns = turns;
	}

	/**
	 * 
	 * @param currentTurn
	 */
	public void setCurrentTurn(Turn currentTurn) {
		this.lastTurn = currentTurn;
	}

}