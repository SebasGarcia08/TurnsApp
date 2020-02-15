package model;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import CustomExceptions.*;

public class TurnsManager {

	private ArrayList<User> users;
	private ArrayList<Turn> turns;
	public Turn lastTurn;
	public static final String[] REQUIRED_FIELDS = {"name", "document number", "type of document"};

	public TurnsManager() {
		this.turns = new ArrayList<Turn>();
		this.users = new ArrayList<User>();
		this.lastTurn = new Turn("A-1", null);
	}

	/**
	 * 
	 * @param n
	 * @param id
	 * @param tod
	 * @param cpn
	 * @param a
	 */
	public void addUser(String n, String id, String tod, String cpn, String a, Turn t)
			throws UserAlreadyRegisteredException, BlankRequiredFieldException, InvalidFormatException {
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
		if (!n.matches("^[a-zA-Z]*$")) throw new  InvalidFormatException("name", "letters.");
		if (!id.matches("\\d+")) throw new InvalidFormatException("identification number", "numbers");
		if (!cpn.isBlank() && !cpn.matches("\\d+")) throw new InvalidFormatException("cellphone number", "numbers");
		users.add(new User(n, id, tod, cpn, a, t));
	}

	/**
	 * 
	 * @param id
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
	 * 
	 * @param id
	 * @return
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
	 * 
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
	
	public Turn getCurrentTurn() throws NoSuchElementException {
		Turn curr_turn;
		try {
			curr_turn = turns.stream().filter(obj -> obj.getState().contentEquals(Turn.ON_HOLD)).findFirst().get();
		} catch(NoSuchElementException e) { throw new NoSuchElementException("There are no turns registered yet."); }
		return curr_turn;
	}
	
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